import axios from 'axios';

const baseURL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';
const username = process.env.REACT_APP_API_USERNAME || 'user';
const password = process.env.REACT_APP_API_PASSWORD || 'password';

const encodeCredentials = (value) => {
  if (typeof window !== 'undefined' && window.btoa) {
    return window.btoa(value);
  }
  return Buffer.from(value, 'utf-8').toString('base64');
};

const apiClient = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json'
  }
});

apiClient.interceptors.request.use((config) => {
  if (!config.headers.Authorization) {
    const token = encodeCredentials(`${username}:${password}`);
    config.headers.Authorization = `Basic ${token}`;
  }
  return config;
});

export default apiClient;
