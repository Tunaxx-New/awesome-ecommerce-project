const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    "/api",
    createProxyMiddleware({
      target: "http://greenshop-dev-service-1:8080",
      changeOrigin: true,
    })
  );
};
