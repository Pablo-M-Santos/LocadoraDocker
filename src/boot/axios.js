import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8040",
  headers: {
    "Content-Type": "application/json",
  },
});

const authenticate = async () => {
  try {
    const response = await api.post("/auth/login", {
      name: process.env.NAME,
      password: process.env.PASSWORD,
    });

    const token = response.data.token;
    if (token) {
      localStorage.setItem("authToken", token);
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    }
  } catch (error) {
    console.error("Authentication error:", error);
    throw error;
  }
};
console.log("Boot function:", ({ app }) => {
  app.config.globalProperties.$axios = axios;
  app.config.globalProperties.$api = api;
  app.config.globalProperties.$authenticate = authenticate;
});

export default ({ app }) => {
  app.config.globalProperties.$axios = axios;
  app.config.globalProperties.$api = api;
  app.config.globalProperties.$authenticate = authenticate;
};

export { api, authenticate };
