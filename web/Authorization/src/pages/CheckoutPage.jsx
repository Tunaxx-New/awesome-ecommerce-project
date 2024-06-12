import React from "react";
import { Footer, Navbar } from "../commons";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";

import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAsyncError } from "../commons";
import api from "../apis/api";
import imageApi from "../apis/imageApi";
import { Table } from "../components";

const Checkout = () => {
  const state = useSelector((state) => state.handleCart);
  const countries = ["Kazakhstan", "Russia", "USA", "China", "Brazil"];

  const [cart, setCart] = useState({});
  const [profile, setProfile] = useState({});
  const [isLoading, setIsLoading] = useState(false);
  const [metaData, setMetaData] = useState({});
  const throwAsyncError = useAsyncError();
  const navigate = useNavigate();

  async function updateCartItems(data) {
    // Requesting image urls
    for (const cartItem of data.cartItems) {
      cartItem.product.image_filename = await imageApi(cartItem.product.image_filename);
    }
    return data;
  }

  useEffect(() => {
    setIsLoading(true);
    const fetchData = async () => {
      try {
        let data = await api('/api/private/profile', {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          }
        });
        if (data) {
          setProfile(data.authentication);
          //setOrders(data.orders);
        }

        data = await api('/api/private/buyer/cart/', {
          method: "GET",
          headers: {
            "Content-Type": "application/json"
          }
        });
        if (data) {
          //data = await updateCartItems(data);
          //console.log(data);
          setCart(data);
        }

        data = await api('/api/public/metadata', {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          }
        });
        if (data)
          setMetaData(data);
      } catch (error) {
        navigate("/");
        //throwAsyncError(error);
      } finally {
        setIsLoading(false);
      }
    }
    fetchData();
  }, []);

  async function makeOrder(shippindAddressId, paymentMethodId) {
    setIsLoading(true);
    try {
      let data = await api('/api/private/buyer/cart/changeCart', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          "shippingAddress": {
            "id": shippindAddressId
          },
          "paymentMethod": {
            "id": paymentMethodId
          }
        })
      });
      data = await api('/api/private/buyer/order/create', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        }
      });
      data = await api('/api/private/buyer/cart/clear', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        }
      });
      setIsLoading(false);
      navigate("/");
    } catch (error) {
      throwAsyncError(error);
    }
  }

  const EmptyCart = () => {

    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
    }, 2000);
    return (
      <div className="container">
        {isLoading && (
          <div className="loading-back">
            <div className="loading-indicator">
              <div className="loading-circle"></div>
              <p>Processing...</p>
            </div>
          </div>
        )}
        <div className="row">
          <div className="col-md-12 py-5 bg-light text-center">
            <h4 className="p-3 display-5">No item in Cart</h4>
            <Link to="/" className="btn btn-outline-dark mx-4">
              <i className="fa fa-arrow-left"></i> Continue Shopping
            </Link>
          </div>
        </div>
      </div>
    );
  };


  const ShowCheckout = () => {
    let subtotal = 0;
    let shipping = 30.0;
    let totalItems = 0;

    const [formData, setFormData] = useState({
      firstName: '',
      lastName: '',
      email: '',
      address: 1,
      paymentMethod: 1,
      ccName: '',
      ccNumber: '',
      ccExpiration: '',
      ccCvv: ''
    });

    const joinAddress = (addressObj) => {
      return `${addressObj.streetAddress}, ${addressObj.city}, ${addressObj.state}, ${addressObj.country}, ${addressObj.postalCode}`;
    };

    const handleChange = (e) => {
      const { id, value } = e.target;
      console.log(id, value);
      const parsedValue = isNaN(value) ? value : Number(value);
      setFormData((prevData) => ({
        ...prevData,
        [id]: parsedValue
      }));
    };

    const handleSubmit = async (e) => {
      e.preventDefault();
      await makeOrder(formData.address, formData.paymentMethod);
    }

    state.map((item) => {
      return (subtotal += item.price * item.qty);
    });

    state.map((item) => {
      return (totalItems += item.qty);
    });

    const productSum = Math.round(cart.cartItems.reduce((sum, cartItem) => { console.log(cartItem.product.price * cartItem.amount, "LOL");return sum += cartItem.product.price * cartItem.amount }, 0));
    const commission = productSum * (profile.buyer.commissionPercentage / 100);
    const allSum = productSum + commission;

    return (
      <>
        <div className="container py-5">
          {isLoading && (
            <div className="loading-back">
              <div className="loading-indicator">
                <div className="loading-circle"></div>
                <p>Processing...</p>
              </div>
            </div>
          )}
          <div className="row my-4">
            <div className="col-md-5 col-lg-4 order-md-last">
              <div className="card mb-4">
                <div className="card-header py-3 bg-light">
                  <h5 className="mb-0">Order Summary</h5>
                </div>
                <div className="card-body">
                  <ul className="list-group list-group-flush">
                    <li className="list-group-item d-flex justify-content-between align-items-center border-0 px-0 pb-0">
                      Products ({cart.cartItems.reduce((sum, orderItem) => {return sum += orderItem.amount}, 0)})
                      <span>${productSum}</span>
                    </li>
                    <li className="list-group-item d-flex justify-content-between align-items-center px-0">
                      Commission {profile.buyer.commissionPercentage}%
                      <span>${commission.toFixed(2)}</span>
                    </li>
                    <li className="list-group-item d-flex justify-content-between align-items-center border-0 px-0 mb-3">
                      <div>
                        <strong>Total amount</strong>
                      </div>
                      <span>
                        <strong>${allSum}</strong>
                      </span>
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div className="col-md-7 col-lg-8">
              <div className="card mb-4">
                <div className="card-header py-3">
                  <h4 className="mb-0">Billing address</h4>
                </div>
                <div className="card-body">
                  <form className="needs-validation" onSubmit={handleSubmit} noValidate>
                    <div className="row g-3">
                      <div className="col-sm-6 my-1">
                        <label htmlFor="firstName" className="form-label">
                          First name
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="firstName"
                          value={formData.firstName}
                          onChange={handleChange}
                          required
                        />
                        <div className="invalid-feedback">
                          Valid first name is required.
                        </div>
                      </div>

                      <div className="col-sm-6 my-1">
                        <label htmlFor="lastName" className="form-label">
                          Last name
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="lastName"
                          value={formData.lastName}
                          onChange={handleChange}
                          required
                        />
                        <div className="invalid-feedback">
                          Valid last name is required.
                        </div>
                      </div>

                      <div className="col-12 my-1">
                        <label htmlFor="email" className="form-label">
                          Email
                        </label>
                        <input
                          type="email"
                          className="form-control"
                          id="email"
                          value={formData.email}
                          onChange={handleChange}
                          placeholder="you@example.com"
                          required
                        />
                        <div className="invalid-feedback">
                          Please enter a valid email address for shipping updates.
                        </div>
                      </div>

                      <div className="col-12 my-1">
                        <label htmlFor="address" className="form-label">
                          Shipping address
                        </label>
                        <select
                          name="select-input"
                          className="form-control"
                          id="address"
                          value={formData.address}
                          onChange={handleChange}
                          required
                        >
                          {metaData && metaData.shippingAddresses && metaData.shippingAddresses.map((value, index) => (
                            <option key={index} value={value.id}>{joinAddress(value.address)}</option>
                          ))}
                        </select>
                        <div className="invalid-feedback">
                          Please enter your shipping address.
                        </div>
                      </div>

                      <div className="col-12 my-1">
                        <label htmlFor="payment-method" className="form-label">
                          Payment method
                        </label>
                        <select
                          name="select-input"
                          className="form-control"
                          id="paymentMethod"
                          value={formData.paymentMethod}
                          onChange={handleChange}
                          required
                        >
                          {metaData && metaData.paymentMethods && metaData.paymentMethods.map((value, index) => (
                            <option key={index} value={value.id} className={`${value.id === 1 ? 'bg-warning' : value.id === 2 ? 'bg-danger text-light' : 'bg-info'}`}>
                              {value.title}
                            </option>
                          ))}
                        </select>
                        <div className="invalid-feedback">
                          Please enter your payment method.
                        </div>
                      </div>
                    </div>

                    <hr className="my-4" />

                    <h4 className="mb-3">Payment</h4>

                    <div className="row gy-3">
                      <div className="col-md-6">
                        <label htmlFor="ccName" className="form-label">
                          Name on card
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="ccName"
                          value={formData.ccName}
                          onChange={handleChange}
                          required
                        />
                        <small className="text-muted">
                          Full name as displayed on card
                        </small>
                        <div className="invalid-feedback">
                          Name on card is required
                        </div>
                      </div>

                      <div className="col-md-6">
                        <label htmlFor="ccNumber" className="form-label">
                          Credit card number
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="ccNumber"
                          value={formData.ccNumber}
                          onChange={handleChange}
                          required
                        />
                        <div className="invalid-feedback">
                          Credit card number is required.
                        </div>
                      </div>

                      <div className="col-md-3">
                        <label htmlFor="ccExpiration" className="form-label">
                          Expiration
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="ccExpiration"
                          value={formData.ccExpiration}
                          onChange={handleChange}
                          required
                        />
                        <div className="invalid-feedback">
                          Expiration date required.
                        </div>
                      </div>

                      <div className="col-md-3">
                        <label htmlFor="ccCvv" className="form-label">
                          CVV
                        </label>
                        <input
                          type="text"
                          className="form-control"
                          id="ccCvv"
                          value={formData.ccCvv}
                          onChange={handleChange}
                          required
                        />
                        <div className="invalid-feedback">
                          Security code required.
                        </div>
                      </div>
                    </div>

                    <hr className="my-4" />

                    <button className="w-100 btn btn-primary" type="submit">
                      Continue to checkout
                    </button>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </>
    );
  };
  return (
    <>
      <Navbar />
      <div className="container my-3 py-3">
        <h1 className="text-center">Checkout</h1>
        <hr />
        {(cart.cartItems && cart.cartItems.length > 0) ? <ShowCheckout /> : <EmptyCart />}
      </div>
      <Footer />
    </>
  );
};

export default Checkout;
