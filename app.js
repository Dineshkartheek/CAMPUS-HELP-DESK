const express = require("express");
const http = require("node:http");
const https = require("node:https");
const path = require("node:path");
const configHandler = require("./api/config");

const app = express();
const webDirectory = path.join(__dirname, "web");
const backendUrl = new URL(
  process.env.CAMPUSCARE_BACKEND_URL || "http://localhost:8080"
);

app.get("/config.js", configHandler);

app.use("/api", (req, res) => {
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

  proxyRequest.on("error", () => {
    if (!res.headersSent) {
      res.status(502).type("text/plain").send(
        "CAMPUSCARE API is unavailable. Start the Java backend or check CAMPUSCARE_BACKEND_URL."
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
