import React from "react";

import { Route, Link, BrowserRouter, Routes } from "react-router-dom";
import "./App.css";
import LoginPage from "./views/LoginPage";
import RegistrationPage from "./views/RegisterPage";
import Navbar from "./views/NavBar";
function App() {
  return (
    <BrowserRouter>
      <div>
        <Navbar />
      </div>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegistrationPage />} />
        {/* <Route path="/profile" element={<Profile />} /> */}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
