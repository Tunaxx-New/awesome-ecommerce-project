import React from "react";
import { Footer, Navbar } from "../commons";
import { useSelector, useDispatch } from "react-redux";
import { addCart, delCart } from "../redux/action";
import { Link } from "react-router-dom";

import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAsyncError } from "../commons";
import api from "../apis/api";
import imageApi from "../apis/imageApi";
import { Table } from "../components";

const Cart = () => {
  const state = useSelector((state) => state.handleCart);
  const dispatch = useDispatch();

  const [cart, setCart] = useState({});

  const [updated, setUpdated] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [cleared, setCleared] = useState(false);

  const throwAsyncError = useAsyncError();
  const navigate = useNavigate();

  const joinAddress = (addressObj) => {
    return `${addressObj.streetAddress}, ${addressObj.city}, ${addressObj.state}, ${addressObj.country}, ${addressObj.postalCode}`;
  };

  async function updateCartItems(data) {
    for (const cartItem of data.cartItems) {
      cartItem.product.image_filename = await imageApi(cartItem.product.image_filename);
    }
    return data;
  }

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      try {
        let data = await api('/api/private/buyer/cart/', {
          method: "GET",
          headers: {
            "Content-Type": "application/json"
          }
        });
        if (data) {
          data = await updateCartItems(data);
          setCart(data);
        }
      } catch (error) {
        navigate("/");
        //throwAsyncError(error);
      } finally {
        setIsLoading(false);
      }
    }
    fetchData();
  }, [cleared]);

  const clearCart = async () => {
    setIsLoading(true);
    try {
      let data = await api('/api/private/buyer/cart/clear', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        }
      });
    } catch (error) {
      throwAsyncError(error);
    } finally {
      setCleared(!cleared);
      setIsLoading(false);
    }
  }

  useEffect(() => {

  }, [updated])

  const EmptyCart = () => {
  
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
            <h4 className="p-3 display-5">Your Cart is Empty</h4>
            <Link to="/" className="btn  btn-outline-dark mx-4">
              <i className="fa fa-arrow-left"></i> Continue Shopping
            </Link>
          </div>
        </div>
      </div>
    );
  };

  const addItem = (product) => {
    //dispatch(addCart(product));
  };
  const removeItem = async (product) => {
    //dispatch(delCart(product));
  };

  const changeItem = async (product, increment) => {
    //dispatch(delCart(product));
    const cartItems_ = [...cart.cartItems];
    cartItems_.forEach(cartItem => {
      cartItem.product.seller = null;
      cartItem.product.categories = null;
      cartItem.product.tags = null;
      if (cartItem.id == product.id) {
        cartItem.amount += increment;
        if (cartItem.amount < 0) {
          cartItem.amount = 0;
        }
      }
    });

    setIsLoading(true);

    try {
      let data = await api('/api/private/buyer/cart/change', {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(cartItems_)
      });
      if (data) {
        data = await updateCartItems({ cartItems: data });
        setUpdated(!updated);
      }
    } catch (error) {
      throwAsyncError(error);
    } finally {
      setIsLoading(false);
    }
  };

  const ShowCart = () => {
    let subtotal = 0;
    let shipping = 0.0;
    let totalItems = 0;
    cart.cartItems.map((item) => {
      return (subtotal += item.product.price * item.amount);
    });

    cart.cartItems.map((item) => {
      return (totalItems += item.amount);
    });
    return (
      <>
        {isLoading && (
          <div className="loading-back">
            <div className="loading-indicator">
              <div className="loading-circle"></div>
              <p>Processing...</p>
            </div>
          </div>
        )}
        <section className="h-100 gradient-custom">
          <div className="container py-5">
            <div className="row d-flex justify-content-center my-4">
              <div className="col-md-8">
                <div className="card mb-4">
                  <div className="card-header py-3">
                    <h5 className="mb-0">Item List</h5>
                  </div>
                  <div className="card-body">
                    {cart.cartItems.map((item) => {
                      return (
                        <div key={item.id}>
                          <div className="row d-flex align-items-center">
                            <div className="col-lg-3 col-md-12">
                              <div
                                className="bg-image rounded"
                                data-mdb-ripple-color="light"
                              >
                                <img
                                  src={item.product.image_filename}
                                  // className="w-100"
                                  alt={item.product.name}
                                  width={"auto"}
                                  height={100}
                                />
                              </div>
                            </div>

                            <div className="col-lg-5 col-md-6">
                              <p>
                                <strong>{item.product.name}</strong>
                              </p>
                              {/* <p>Color: blue</p>
                              <p>Size: M</p> */}
                            </div>

                            <div className="col-lg-4 col-md-6">
                              <div
                                className="d-flex mb-4"
                                style={{ maxWidth: "300px" }}
                              >
                                <button
                                  className="btn px-3"
                                  onClick={() => {
                                    changeItem(item, -1);
                                  }}
                                >
                                  <i className="fas fa-minus"></i>
                                </button>

                                <p className="mx-5">{item.amount}</p>

                                <button
                                  className="btn px-3"
                                  onClick={() => {
                                    changeItem(item, 1);
                                  }}
                                >
                                  <i className="fas fa-plus"></i>
                                </button>
                              </div>

                              <p className="text-start text-md-center">
                                <strong>
                                  <span className="text-muted">{item.amount}</span>{" "}
                                  x ${item.product.price ? item.product.price : 0}
                                </strong>
                              </p>
                            </div>
                          </div>

                          <hr className="my-4" />
                        </div>
                      );
                    })}
                  </div>
                </div>
              </div>
              <div className="col-md-4">
                <div className="card mb-4">
                  <div className="card-header py-3 bg-light d-flex" style={{justifyContent:"space-between"}}>
                    <h5 className="mb-0">Order Summary</h5>
                    <button className="btn btn-dark" onClick={clearCart}>Clear</button>
                  </div>
                  <div className="card-body">
                    <ul className="list-group list-group-flush">
                      <li className="list-group-item d-flex justify-content-between align-items-center border-0 px-0 pb-0">
                        Products ({totalItems})
                        <span>${Math.round(subtotal)}</span>
                      </li>
                      <li className="list-group-item d-flex justify-content-between align-items-center px-0">
                        Shipping
                        <span>${shipping}</span>
                      </li>
                      <li className="list-group-item d-flex justify-content-between align-items-center border-0 px-0 mb-3">
                        <div>
                          <strong>Total amount</strong>
                        </div>
                        <span>
                          <strong>${Math.round(subtotal + shipping)}</strong>
                        </span>
                      </li>
                    </ul>

                    <Link
                      to="/checkout"
                      className="btn btn-dark btn-lg btn-block"
                    >
                      Go to checkout
                    </Link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>
      </>
    );
  };

  return (
    <>
      <Navbar />
      <div className="container my-3 py-3">
        <h1 className="text-center">Cart</h1>
        <hr />
        {(cart.cartItems && cart.cartItems.length > 0) ? <ShowCart /> : <EmptyCart />}
      </div>
      <Footer />
    </>
  );
};

export default Cart;
