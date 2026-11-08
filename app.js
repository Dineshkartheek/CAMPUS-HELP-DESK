const express = require("express");
const path = require("node:path");
const configHandler = require("./api/config");

const app = express();
const webDirectory = path.join(__dirname, "web");

app.get("/config.js", configHandler);

app.use(express.static(webDirectory, { index: false }));
app.use((req, res, next) => {
  if (req.method !== "GET" && req.method !== "HEAD") {
    next();
    return;
  }

  res.sendFile(path.join(webDirectory, "index.html"));
});

module.exports = app;
