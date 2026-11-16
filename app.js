const express = require("express");
const http = require("node:http");
const https = require("node:https");
const path = require("node:path");
const configHandler = require("./api/config");

const app = express();
const webDirectory = path.join(__dirname, "web");
const configuredBackendUrl = process.env.CAMPUSCARE_BACKEND_URL;
const isHostedRuntime =
  process.env.NODE_ENV === "production" || Boolean(process.env.VERCEL);
const backendUrl = configuredBackendUrl
  ? new URL(configuredBackendUrl)
  : isHostedRuntime
    ? null
    : new URL("http://localhost:8080");

if (backendUrl
    && (!["http:", "https:"].includes(backendUrl.protocol)
      || backendUrl.username
      || backendUrl.password
      || backendUrl.pathname !== "/"
      || backendUrl.search
      || backendUrl.hash)) {
  throw new Error(
    "CAMPUSCARE_BACKEND_URL must be an http(s) backend origin without a path, credentials, query, or fragment."
  );
}

app.get("/config.js", configHandler);

app.use("/api", (req, res) => {
  if (!backendUrl) {
    res.status(503).type("text/plain").send(
      "Java API backend is not configured. Set CAMPUSCARE_BACKEND_URL to the deployed backend origin."
    );
    return;
  }

  const transport = backendUrl.protocol === "https:" ? https : http;
  const headers = { ...req.headers, host: backendUrl.host };
  const proxyRequest = transport.request(
    {
      hostname: backendUrl.hostname,
      port: backendUrl.port || undefined,
      protocol: backendUrl.protocol,
      method: req.method,
      path: req.originalUrl,
      headers
    },
    (proxyResponse) => {
      res.writeHead(proxyResponse.statusCode || 502, proxyResponse.headers);
      proxyResponse.pipe(res);
    }
  );

  proxyRequest.on("error", (error) => {
    console.error("CAMPUSCARE API proxy request failed:", error.message);
    if (!res.headersSent) {
      res.status(502).type("text/plain").send(
        "CAMPUSCARE API is unavailable. Check that the Java backend is running and CAMPUSCARE_BACKEND_URL is correct."
      );
    } else {
      res.destroy();
    }
  });

  req.pipe(proxyRequest);
});

app.use(express.static(webDirectory, { index: false }));
app.use((req, res, next) => {
  if (req.method !== "GET" && req.method !== "HEAD") {
    next();
    return;
  }

  res.sendFile(path.join(webDirectory, "index.html"));
});

module.exports = app;
