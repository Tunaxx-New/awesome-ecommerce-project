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

export const addProduct = async (productData) => {
  return await fetchJson("/private/seller/product/add", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(productData),
  });
};

export const getProductById = async (id) => {
  return await fetchJson(`/public/product/${id}`);
};

export const getAllProducts = async () => {
  return await fetchJson("/public/product/list");
};

export const makeReviewOnProduct = async (id, reviewData) => {
  return await fetchJson(`/private/buyer/product/${id}/review`, {
    method: "GET", // Should this be a POST request instead?
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(reviewData),
  });
};

export const getProductOrders = async (id) => {
  return await fetchJson(`/private/buyer/product/${id}/orders`);
};

// import { addProduct, getProductById, getAllProducts, makeReviewOnProduct, getProductOrders } from './productApi';

// // Example usage:
// async function fetchData() {
//   try {
//     const newProduct = { /* product data */ };
//     const addedProduct = await addProduct(newProduct);
//     console.log('Added product:', addedProduct);

//     const productId = '123'; // ID of the product to fetch
//     const productDetails = await getProductById(productId);
//     console.log('Product details:', productDetails);

//     const allProducts = await getAllProducts();
//     console.log('All products:', allProducts);

//     const reviewData = { /* review data */ };
//     const reviewResponse = await makeReviewOnProduct(productId, reviewData);
//     console.log('Review response:', reviewResponse);

//     const orderHistory = await getProductOrders(productId);
//     console.log('Order history:', orderHistory);
//   } catch (error) {
//     console.error('Error:', error.message);
//   }
// }

// fetchData();
