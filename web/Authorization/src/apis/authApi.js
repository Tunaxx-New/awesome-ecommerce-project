// authApi.js

// const fetchJson = async (url, options) => {
//   const response = await fetch(baseUrl + url, options)
//   if (!response.ok) {
//     throw new Error(`Request failed: ${response.status} ${response.statusText}`);
//   }
//   return await response.json();
// };

// const baseUrl = 'https://087b-5-34-1-61.ngrok-free.app';

const baseUrl = "http://104.248.234.194:8080";
// const baseUrl = "/api";

const fetchJson = async (url, options) => {
  const response = await fetch(baseUrl + url, options);
  if (!response.ok) {
    throw new Error(
      `Request failed: ${response.status} ${response.statusText}`,
    );
  }
  return await response.json();
};

export const registerUser = async (userData) => {
  return await fetchJson("/public/register", {
    method: "POST",
    // mode: "cors",
    headers: {
      // Authorization:
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  });
};

export const sendForgotPasswordEmail = async (email) => {
  return await fetchJson("/public/forgot/send", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email }),
  });
};

export const checkForgotPasswordToken = async (token) => {
  return await fetchJson("/public/forgot/check", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ token }),
  });
};

export const authenticateUser = async (credentials) => {
  return await fetchJson("/public/authenticate", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credentials),
  });
};

// import { registerUser, sendForgotPasswordEmail, checkForgotPasswordToken, authenticateUser } from './authApi';

// // Example usage:
// async function authenticate() {
//   try {
//     const registrationData = { /* user registration data */ };
//     const registrationResponse = await registerUser(registrationData);
//     console.log('Registration response:', registrationResponse);

//     const forgotPasswordEmail = 'example@example.com';
//     const forgotPasswordEmailResponse = await sendForgotPasswordEmail(forgotPasswordEmail);
//     console.log('Forgot password email response:', forgotPasswordEmailResponse);

//     const forgotPasswordToken = 'example-token';
//     const checkForgotPasswordTokenResponse = await checkForgotPasswordToken(forgotPasswordToken);
//     console.log('Check forgot password token response:', checkForgotPasswordTokenResponse);

//     const credentials = { /* user credentials */ };
//     const authenticationResponse = await authenticateUser(credentials);
//     console.log('Authentication response:', authenticationResponse);
//   } catch (error) {
//     console.error('Error:', error.message);
//   }
// }

// authenticate();
