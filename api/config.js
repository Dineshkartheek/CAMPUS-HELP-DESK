module.exports = (req, res) => {
  res.setHeader("Content-Type", "application/javascript; charset=utf-8");
  res.setHeader("Cache-Control", "no-store");
  res.end(
    `window.CAMPUSCARE_API_BASE_URL = ${JSON.stringify(
      process.env.CAMPUSCARE_API_BASE_URL || ""
    )};`
  );
};
