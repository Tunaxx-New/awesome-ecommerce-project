import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Footer, Navbar } from "../commons";
import { authenticateUser } from "../apis/authApi"; // Import the registerUser function from authApi
import api from "../apis/api";
import Cookies from "universal-cookie";
import { useAsyncError } from "../commons";

const cookies = new Cookies();

const Login = ({ history }) => {
  // cookies

  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();
  const throwAsyncError = useAsyncError();
  const [formData, setFormData] = useState({
    // name: '',
    email: "test@gmail.com",
    password: "Tunaxx2024!",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // Call the registerUser function with the form data
      const response = await api("api/public/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
        }),
      });
      console.log("Login response:", response);
      // const authenticationResponse = await authenticateUser(formData);
      // console.log("Login response:", authenticationResponse);
      /*
      cookies.set(
        "Authorization",
        // `Bearer:
        `${response.data.token}`,
        { path: "/" }
      );
      */

      navigate("/");
      // Проверка на успешную аутентификацию
    } catch (error) {
      console.error("Error:", error.message);
      throwAsyncError(error);
    }
  };

  return (
    <>
      <Navbar />
      <div className="container my-3 py-3">
        <h1 className="text-center">Login</h1>
        <hr />
        <div class="row my-4 h-100">
          <div className="col-md-4 col-lg-4 col-sm-8 mx-auto">
            <form onSubmit={handleSubmit}>
              <div class="my-3">
                <label for="display-4">Email address</label>
                <input
                  type="email"
                  class="form-control"
                  id="Email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="name@example.com"
                  required
                />
              </div>
              <div class="my-3">
                <label for="floatingPassword display-4">Password</label>
                <input
                  type="password"
                  class="form-control"
                  id="Password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  placeholder="Password"
                  required
                />
              </div>

              <div className="my-3">
                <p>
                  New Here?{" "}
                  <Link
                    to="/register"
                    className="text-decoration-underline text-info"
                  >
                    Register
                  </Link>{" "}
                </p>
              </div>
              <div className="text-center">
                <button class="my-2 mx-auto btn btn-dark" type="submit">
                  Login
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default Login;
