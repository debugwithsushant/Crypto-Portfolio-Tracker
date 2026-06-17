// src/api/exchange.js
import axios from 'axios';
import { API_BASE_URL } from './config';

const API_URL = `${API_BASE_URL}/api/exchange`;

// Helper to get the token from localStorage
const getAuthHeader = () => {
  const token = localStorage.getItem('token');
  return {
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  };
};

// Connect a new exchange
// exchangeData must contain: { exchangeName, apiKey, apiSecret, label }
export const connectExchange = (exchangeData) => {
  return axios.post(`${API_URL}/connect`, exchangeData, getAuthHeader());
};

// Get user's connected exchanges
export const getUserExchanges = () => {
  return axios.get(`${API_URL}/connected`, getAuthHeader());
};

// Delete a connected exchange (by exchange name, e.g. "BINANCE")
export const deleteExchange = (exchangeName) => {
  return axios.delete(`${API_URL}/disconnect/${exchangeName}`, getAuthHeader());
};