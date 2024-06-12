import React, { useEffect } from "react";
import ReactDOM from "react-dom/client";
// import "../node_modules/font-awesome/css/font-awesome.min.css";
// import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter, Routes, Route, useParams, useLocation  } from "react-router-dom";
import { Provider } from "react-redux";
// import store from "./redux/store";

import {
  Navbar,
  Main,
  Product,
  Footer,
  // WishListPage,
  OrdersListPage,
} from "../commons";

import { UpdPageProfile } from "./index";
const ProfilePage = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const userId = searchParams.get('userId');
  const type = searchParams.get('type');
  
  return (
    <>
      <Navbar />
      <UpdPageProfile userId={userId} type={type} />
      <Footer />
    </>
  );
};

export default ProfilePage;
