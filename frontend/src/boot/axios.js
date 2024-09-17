import { boot } from "quasar/wrappers";
import axios from "axios";

if (!process.env.BACKEND_PORT) {
  throw new Error("A variável de ambiente BACKEND_PORT não está definida!");
}

const api = axios.create({
  baseURL: `http://localhost:${process.env.BACKEND_PORT}`,
  headers: {
    "Content-Type": "application/json",
  },
});

const token = localStorage.getItem("authToken");
if (token) {
  api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

const authenticate = (username, password) => {
  return api
    .post("/auth/login", {
      name: username,
      password: password,
    })
    .then((response) => {
      const token = response.data.token;
      if (token) {
        localStorage.setItem("authToken", token);
        api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
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
