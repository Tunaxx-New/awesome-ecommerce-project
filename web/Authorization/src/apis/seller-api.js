// sellerApi.js

const baseUrl = "http://localhost:3000";

const fetchJson = async (url, options) => {
  const response = await fetch(baseUrl + url, options);
  if (!response.ok) {
    throw new Error(
      `Request failed: ${response.status} ${response.statusText}`,
    );
  }
  return await response.json();
};

export const changeSellerProfile = async (profileData) => {
  return await fetchJson("/private/seller/profile/change", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(profileData),
  });
};

export const agreeTransparentPolicies = async () => {
  return await fetchJson(
    "/private/seller/profile/changeTransparentPolicies",
    {
      method: "POST",
    },
  );
};

export const registerSellerAccount = async (sellerData) => {
  return await fetchJson("/private/registerSeller", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(sellerData),
  });
};

export const getSellerProfile = async () => {
  return await fetchJson("/private/seller/profile", {
    method: "GET",
  });
};

// import { changeSellerProfile, agreeTransparentPolicies, registerSellerAccount, getSellerProfile } from './sellerApi';

// // Пример использования функций
// async function fetchData() {
//   try {
//     const profileData = await getSellerProfile();
//     console.log('Seller profile:', profileData);

//     const registrationData = { /* данные для регистрации продавца */ };
//     const registrationResponse = await registerSellerAccount(registrationData);
//     console.log('Registration response:', registrationResponse);
//   } catch (error) {
//     console.error('Error:', error.message);
//   }
// }

// fetchData();
