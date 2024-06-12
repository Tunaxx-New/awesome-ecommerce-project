import logo from "./logo.svg";
import "./App.css";
import { useEffect } from "react";

import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import { Provider } from "react-redux";
import store from "./redux/store";

import "./index.css";
import "../node_modules/font-awesome/css/font-awesome.min.css";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-icons/font/bootstrap-icons.css";

import {
  Home,
  AboutPage,
  ContactPage,
  Cart,
  Login,
  Register,
  Checkout,
  PageNotFound,
  OrdersListPage,
  ProductPage,
} from "./pages";

import Product from "./components/Products";
import Products from "./components/Products";
import ProfilePage from "./pages/ProfilePage";
import ListSellers from "./SellerRatings/SellerRatings";
import OrderInfo from "./pages/Orders/OrderInfo/OrderInfo";
import { useAsyncError } from './commons';
import api from './apis/api';
import CreateProduct from "./components/Seller/CreateProduct";
import ProductListPage from "./pages/ProductListPage";
import ProductReview from "./components/Product/ProductReview";
import LoyaltyIndex from "./commons/Info/LoyaltyIndex";
import CLV from "./commons/Info/CLV";
import Badges from "./commons/Info/Badges";
import Transparent from "./commons/Info/Transparency";
import RetailInfo from "./commons/Info/RetailInfo";

function App() {
  const throwAsyncError = useAsyncError();
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await api('/api/private/profile', {
          method: 'POST',
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json',
            //'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTcxNzI1ODA4MSwiZXhwIjoxNzE3MzQ0NDgxfQ.wphsyuXUZmXCamQHE6LpGg-kSUW9aOOgH8SK1E3RzKw',
          },
          /*body: JSON.stringify({
            "email": "test@gmail.com",
            "password": "Tunaxx2024!",
          })*/
        });
      } catch (error) {
        throwAsyncError(error);
      }
    }
    //fetchData();
  })

  return (
    <BrowserRouter>
      <Provider store={store}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/products" element={<Products />} />
          <Route path="/product/:id" element={<ProductPage />} />

          <Route path="/create-product/:id" element={<CreateProduct isCreate={true}/>} />

          <Route path="/about" element={<AboutPage />} />
          <Route path="/contact" element={<ContactPage />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/checkout" element={<Checkout />} />
          <Route path="/profile" element={<ProfilePage />} />

          <Route path="/product-review/:productId/:orderId" element={<ProductReview />} />
          <Route path="/product-list" element={<ProductListPage />} />
          <Route path="/orders-list" element={<OrdersListPage />} />
          {/* Add routes for registrations */}
          <Route path="/private/registerSeller" element={<Register />} />
          <Route path="/private/profile" element={<Register />} />
          <Route path="/product/*" element={<PageNotFound />} />
          <Route path="/list" element={<ListSellers />} />
          <Route path="/orders-list1" element={<OrderInfo />} />
          <Route path="*" element={<PageNotFound />} />

          <Route path="/info/loyalty" element={<LoyaltyIndex />} />
          <Route path="/info/clv" element={<CLV />} />
          <Route path="/info/badges" element={<Badges />} />
          <Route path="/info/transparent" element={<Transparent />} />
          <Route path="/info/retail" element={<RetailInfo />} />
        </Routes>
      </Provider>
    </BrowserRouter>
  );
}

export default App;
