import { boot } from "quasar/wrappers";
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8040",
  headers: {
    "Content-Type": "application/json",
  },
});

const token = localStorage.getItem("authToken");
if (token) {
  api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

const authenticate = () => {
  return api
    .post("/auth/login", {
      name: "admin",
      password: "12345678",
    })
    .then((response) => {
      const token = response.data.token;
      if (token) {
        localStorage.setItem("authToken", token);
        api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
        return response;
      }
    })
    .catch((error) => {
      console.error(
        "Erro na autenticação:",
        error.response ? error.response.data : error.message
      );
      throw error;
    });
};

export default boot(({ app }) => {
  app.config.globalProperties.$axios = axios;
  app.config.globalProperties.$api = api;
  app.config.globalProperties.$authenticate = authenticate;
});

export { api, authenticate };
