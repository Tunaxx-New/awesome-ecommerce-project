import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../apis/api";
import { useAsyncError } from '../commons';
import { ChangeTransparentTables, LoyaltyCard, Table } from "../components";
import { BadgesGrid } from "../components";
import imageApi from "../apis/imageApi";
import { Diagram } from "../components";

const UpdPageProfile = ({ userId, type }) => {
  const [formData, setFormData] = useState({
  });
  const [orders, setOrders] = useState([]);

  const [clv, setClv] = useState();
  const [loyalty, setLoyalty] = useState();
  const [loyaltySeller, setLoyaltySeller] = useState();

  const [activeSetting, setActiveSetting] = useState(type);
  const [successMessage, setSuccessMessage] = useState(undefined);
  const throwAsyncError = useAsyncError();

  const [isLoading, setIsLoading] = useState(false);
  const [isLoadedImages, setISLoadedImages] = useState(false);

  const navigate = useNavigate();

  const handleSetActiveSetting = (setting) => {
    const tabs = document.querySelectorAll('.list-group-item');
    tabs.forEach(tab => {
      if (tab.id === `tab-${setting}`) {
        tab.classList.add('active');
      } else {
        tab.classList.remove('active');
      }
    });
    setActiveSetting(setting);
  };

  useEffect(() => {
    setActiveSetting(type);
    setIsLoading(true);
    const fetchData = async () => {
      let data = null;
      try {
        data = await api('/api/private/profile', {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          }
        });

        if (data) {
          setFormData(data.authentication);
        }

        const data2 = await api('/api/private/buyer/order/list', {
          method: "GET",
          headers: {
            "Content-Type": "application/json"
          },
          params: {
            page: 0,
            size: Number.MAX_SAFE_INTEGER
          }
        });
        console.log(data2);
        if (data2) {
          setOrders(data2.content);
        }

      } catch (error) {
        console.log(error, "ASDDSASSSS");
        navigate("/");
        throwAsyncError(error);
      }
      if (data == null)
        return;
      const formData_ = data.authentication;
      setISLoadedImages(false);
      try {
        console.log(formData_.buyer, formData, "LOADED");
        if (formData_.buyer) {
          formData_.buyer.badges = await updateBadges(formData_.buyer.badges);
          console.log(formData_, "LOADED");
          setFormData(formData_);
        }
      } catch (error) {
        throwAsyncError(error);
      } finally {
        setISLoadedImages(true);
      }

      try {
        let data = await api('/api/private/buyer/profile/CLV', {
          method: "GET",
          headers: {
            "Content-Type": "application/json"
          }
        });
        if (data)
          setClv(data);

        data = await api('/api/private/buyer/profile/loyalty', {
          method: "GET",
          headers: {
            "Content-Type": "application/json"
          }
        });

        if (data)
          setLoyalty(data);

        try {
          if (formData_.seller) {
            data = await api('/api/private/seller/profile/loyalty', {
              method: "GET",
              headers: {
                "Content-Type": "application/json"
              }
            });
          }
          if (data)
            setLoyaltySeller(data);
        } catch (error) {
          console.log(error);
        }

      } catch (error) {
        throwAsyncError(error);
      } finally {
        setIsLoading(false);
      }

    };

    fetchData();
  }, []);

  useEffect(() => {
    handleSetActiveSetting(activeSetting);
  }, []);

  /*
  useEffect(() => {
    const fetchData = async () => {
      setISLoadedImages(false);
      setIsLoading(true);
      try {
        if (formData.buyer)
          await updateBadges(formData.buyer.badges);
      } catch (error) {
        throwAsyncError(error);
      } finally {
        setIsLoading(false);
        setISLoadedImages(true);
      }
    };
    document.querySelectorAll('.item-image').forEach((image, index) => {
      image.src = formData.buyer.badges[index].imageSource;
    });
    fetchData();
  }, [activeSetting])
  */

  const [errors, setErrors] = useState({});

  function dateToFull(dateString) {
    const date = new Date(dateString);
    // Extract the date components
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed, so add 1; padStart ensures two digits
    const day = String(date.getDate()).padStart(2, '0'); // padStart ensures two digits

    // Extract the time components
    const hours = String(date.getHours()).padStart(2, '0'); // padStart ensures two digits
    const minutes = String(date.getMinutes()).padStart(2, '0'); // padStart ensures two digits
    const seconds = String(date.getSeconds()).padStart(2, '0'); // padStart ensures two digits
    const milliseconds = String(date.getMilliseconds()).padStart(6, '0'); // padStart ensures three digits

    // Create the formatted date string
    const formattedDate = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${milliseconds}`;
    return formattedDate;
  }

  const handleChange = (e) => {

    const { name, value, type, checked } = e.target;

    // Split the name attribute value by dot
    const nameParts = name.split('.');

    // Check if there are multiple parts (nested property)
    if (nameParts.length > 1) {
      // Create a nested object structure
      const nestedObject = {
        [nameParts.slice(1).join('.')]: type === "checkbox" ? checked : type === "date" ? dateToFull(value) : value
      };

      // Check if buyer already exists in formData
      if (formData[`${activeSetting}`]) {
        // If buyer exists, update the nested property
        setFormData({
          ...formData,
          [activeSetting]: {
            ...formData[`${activeSetting}`],
            ...nestedObject
          }
        });
      } else {
        // If buyer doesn't exist, create it as an object and set the nested property
        setFormData({
          ...formData,
          [activeSetting]: {
            ...nestedObject
          }
        });
      }
    } else {
      // If it's not a nested property, update directly
      setFormData({
        ...formData,
        [name]: type === "checkbox" ? checked : type === "date" ? dateToFull(value) : value
      });
    }

  };

  const validate = (formData) => {
    const newErrors = {};
    const lengthRegex = /^.{3,}$/;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!lengthRegex.test(formData.name)) {
      newErrors.firstName = "First name must be at least 3 characters long.";
    }

    if (!lengthRegex.test(formData.surname)) {
      newErrors.lastName = "Last name must be at least 3 characters long.";
    }

    if (formData.email && !emailRegex.test(formData.email)) {
      newErrors.email = "Email is not valid.";
    }

    return newErrors;
  };

  const handleSubmit = async () => {
    const validationErrors = validate(formData.buyer);
    if (Object.keys(validationErrors).length === 0) {
      try {
        await api(`/api/private/${activeSetting}/profile/change`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(formData[`${activeSetting}`])
        });
        document.querySelector('.success-message').style.top = null;
        document.querySelector('.success-message').style.animation = 'animation: fadeInOut 2s linear';
        setSuccessMessage(`Your ${activeSetting} updated successfuly!`);
        setTimeout(() => {
          setSuccessMessage(undefined);
          document.querySelector('.success-message').style.top = "-1000px";
        }, 2000);

      } catch (error) {
        console.log(error);
        throwAsyncError(error);
      }
    } else {
      setErrors(validationErrors);
    }
  }

  /*
  const handleSubmit = () => {
    const validationErrors = validate();
    if (Object.keys(validationErrors).length === 0) {
      const message = `
        First Name: ${formData.firstName}\n
        Last Name: ${formData.lastName}\n
        Email: ${formData.email}\n
        Phone: ${formData.phone}\n
        Subscribe: ${formData.subscribe ? "Yes" : "No"}
      `;
      alert(message);
      // Updating state to force re-render
      setFormData({ ...formData });
    } else {
      setErrors(validationErrors);
    }

  };
  */

  // enable server
  // const handleSubmit = () => {
  //   const validationErrors = validate();
  //   if (Object.keys(validationErrors).length === 0) {
  //     // Отправка на сервер Изменение профиля
  //    const response = await fetch(http://localhost:8080/api/private/buyer/change/,  {
  //         method: "GET",
  //         // mode: "cors",
  //         headers: {
  //           "Authorization": token_,
  //           "Content-Type": "application/json",
  //         },
  //        body: {
  //         "id": 0,
  //         "name": ${formData.firstName},
  //          ....
  //       }
  //       });
  //    //
  //     setFormData({ ...formData });
  //   } else {
  //     setErrors(validationErrors);
  //   }
  // };

  async function updateBadges(data) {
    await Promise.all(data.map(async badge => {
      //console.log(badge.imageSource, "LLL");
      //const resp = await imageApi(badge.imageSource);
      //console.log(resp, "LLL");
      //badge.imageSource = resp;
    }));
    return data;
  }

  const joinAddress = (addressObj) => {
    if (addressObj)
      return `${addressObj.streetAddress}, ${addressObj.city}, ${addressObj.state}, ${addressObj.country}, ${addressObj.postalCode}`;
    return "";
  };
  const styles = {
    container: {
      padding: '20px',
      backgroundColor: '#f8f8f8',
      borderRadius: '5px',
      boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)'
    },
    heading: {
      fontSize: '24px',
      marginBottom: '20px'
    },
    infoContainer: {
      display: "flex",
      alignContent: "center",
      alignItems: "center",
      border: '1px solid #ddd',
      padding: '20px',
      borderRadius: '5px',
      marginBottom: "8px"
    },
    link: {
      color: '#007bff',
      textDecoration: 'none',
      marginLeft: '10px'
    }
  };
  return (
    <div>
      <div className={`success-message ${successMessage ? 'animate' : ''}`} style={{ opacity: 0, top: -1000 }}>{successMessage}</div>
      {formData.id &&
        <div>
          <style>
            {`body{
    background:#eee;    
}
.widget-author {
  margin-bottom: 58px;
}
.author-card {
  position: relative;
  padding-bottom: 48px;
  background-color: #fff;
  box-shadow: 0 12px 20px 1px rgba(64, 64, 64, .09);
}
.author-card .author-card-cover {
  position: relative;
  width: 100%;
  height: 100px;
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
}
.author-card .author-card-cover::after {
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  content: '';
  opacity: 0.5;
}
.author-card .author-card-cover > .btn {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 0 10px;
}
.author-card .author-card-profile {
  display: table;
  position: relative;
  margin-top: -22px;
  padding-right: 15px;
  padding-bottom: 16px;
  padding-left: 20px;
  z-index: 5;
}
.author-card .author-card-profile .author-card-avatar, .author-card .author-card-profile .author-card-details {
  display: table-cell;
  vertical-align: middle;
}
.author-card .author-card-profile .author-card-avatar {
  width: 85px;
  border-radius: 50%;
  box-shadow: 0 8px 20px 0 rgba(0, 0, 0, .15);
  overflow: hidden;
}
.author-card .author-card-profile .author-card-avatar > img {
  display: block;
  width: 100%;
}
.author-card .author-card-profile .author-card-details {
  padding-top: 20px;
  padding-left: 15px;
}
.author-card .author-card-profile .author-card-name {
  margin-bottom: 2px;
  font-size: 14px;
  font-weight: bold;
}
.author-card .author-card-profile .author-card-position {
  display: block;
  color: #8c8c8c;
  font-size: 12px;
  font-weight: 600;
}
.author-card .author-card-info {
  margin-bottom: 0;
  padding: 0 25px;
  font-size: 13px;
}
.author-card .author-card-social-bar-wrap {
  position: absolute;
  bottom: -18px;
  left: 0;
  width: 100%;
}
.author-card .author-card-social-bar-wrap .author-card-social-bar {
  display: table;
  margin: auto;
  background-color: #fff;
  box-shadow: 0 12px 20px 1px rgba(64, 64, 64, .11);
}
.btn-style-1.btn-white {
    background-color: #fff;
}
.list-group-item i {
    display: inline-block;
    margin-top: -1px;
    margin-right: 8px;
    font-size: 1.2em;
    vertical-align: middle;
}
.mr-1, .mx-1 {
    margin-right: .25rem !important;
}

.list-group-item.active:not(.disabled) {
    border-color: #e7e7e7;
    background: #fff;
    color: #ac32e4;
    cursor: default;
    pointer-events: none;
}
.list-group-flush:last-child .list-group-item:last-child {
    border-bottom: 0;
}

.list-group-flush .list-group-item {
    border-right: 0 !important;
    border-left: 0 !important;
}

.list-group-flush .list-group-item {
    border-right: 0;
    border-left: 0;
    border-radius: 0;
}
.list-group-item.active {
    z-index: 2;
    color: #fff;
    background-color: #007bff;
    border-color: #007bff;
}
.list-group-item:last-child {
    margin-bottom: 0;
    border-bottom-right-radius: .25rem;
    border-bottom-left-radius: .25rem;
}
a.list-group-item, .list-group-item-action {
    color: #404040;
    font-weight: 600;
}
.list-group-item {
    padding-top: 16px;
    padding-bottom: 16px;
    -webkit-transition: all .3s;
    transition: all .3s;
    border: 1px solid #e7e7e7 !important;
    border-radius: 0 !important;
    color: #404040;
    font-size: 12px;
    font-weight: 600;
    letter-spacing: .08em;
    text-transform: uppercase;
    text-decoration: none;
}
.list-group-item {
    position: relative;
    display: block;
    padding: .75rem 1.25rem;
    margin-bottom: -1px;
    background-color: #fff;
    border: 1px solid rgba(0,0,0,0.125);
}
.list-group-item.active:not(.disabled)::before {
    background-color: #ac32e4;
}

.list-group-item::before {
    display: block;
    position: absolute;
    top: 0;
    left: 0;
    width: 3px;
    height: 100%;
    background-color: transparent;
    content: '';}

    .no-underline {
      text-decoration: none;
  }
  a{
    color: black;
    text-decoration: none;
  }
  .custom-control-input {
    color: var(--primary-color);
  }
}`}
          </style>
          <link
            href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"
            rel="stylesheet"
          />
          <div className="container mt-5">
            {isLoading && (
              <div className="loading-back">
                <div className="loading-indicator">
                  <div className="loading-circle"></div>
                  <p>Processing...</p>
                </div>
              </div>
            )}
            <div className="row">
              <div className="col-lg-4 pb-5">
                {/* <!-- Account Sidebar--> */}
                <div className="author-card pb-3">
                  <div
                    className="author-card-cover"
                  // style="background-image: url(https://bootdey.com/img/Content/flores-amarillas-wallpaper.jpeg);"
                  >
                    <a
                      className="btn btn-style-1 btn-white btn-sm"
                      href="#"
                      data-toggle="tooltip"
                      title=""
                      data-original-title={`You currently have ${formData.buyer.badges.length} Badges points to spend`}
                    >
                      <i className="fa fa-award text-md"></i>&nbsp;{formData.buyer.badges.length} Badges
                    </a>
                  </div>
                  <div className="author-card-profile">
                    <div className="author-card-avatar">
                      {((activeSetting == 'user') || !activeSetting || (activeSetting == 'badge')) &&
                        <img
                          src={`./avatars/images/ava_${((formData.id + 20) % 25 + 1).toString().padStart(2, '0')}.gif`}
                          alt={formData.email}
                        />
                      }
                      {activeSetting == 'seller' &&
                        <img
                          src={`./avatars/images/ava_${((formData.seller.id + 10) % 25 + 1).toString().padStart(2, '0')}.gif`}
                          alt={formData.seller.name}
                        />
                      }
                      {activeSetting == 'buyer' &&
                        <img
                          src={`./avatars/images/ava_${((formData.buyer.id) % 25 + 1).toString().padStart(2, '0')}.gif`}
                          alt={formData.buyer.name}
                        />
                      }
                      {activeSetting == 'admin' &&
                        <img
                          src={`./avatars/images/ava_admin.jpg`}
                          alt={formData.email}
                        />
                      }
                      {activeSetting == 'metric' &&
                        <img
                          src={`./avatars/images/ava_admin.jpg`}
                          alt={formData.email}
                        />
                      }
                    </div>
                    <div className="author-card-details">
                      <h5 className="author-card-name text-lg">
                        {formData.firstName}
                      </h5>
                      <span className="author-card-position">
                        Registered {new Date(formData.registeredTime).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' })}
                      </span>
                    </div>
                  </div>
                </div>
                <div className="wizard">
                  <nav className="list-group list-group-flush">
                    <a id="tab-user" className="list-group-item bg-bright" href="#" onClick={() => handleSetActiveSetting('user')}>
                      <i className="fe-icon-user text-muted"></i>User profile
                    </a>
                    <a id="tab-buyer" className="list-group-item bg-bright" href="#" onClick={() => handleSetActiveSetting('buyer')}>
                      <i className="fe-icon-user text-muted"></i>Buyer profile
                    </a>
                    <Link to="/orders-list" className="list-group-item bg-middle">
                      <div className="d-flex justify-content-between align-items-center">
                        <div>
                          <i className="fe-icon-shopping-bag mr-1 text-muted"></i>
                          <div className=" no-underline d-inline-block font-weight-medium text-uppercase ">
                            Orders List
                          </div>
                        </div>
                        <span className="badge badge-secondary">{orders.length}</span>
                      </div>
                    </Link>
                    {formData.seller &&
                      <div>
                        <a id="tab-seller" className="list-group-item bg-bright" href="#" onClick={() => handleSetActiveSetting('seller')}>
                          <i className="fe-icon-user text-muted"></i>Seller profile
                        </a>
                        <Link to="/product-list" className="list-group-item bg-middle">
                          <div className="d-flex justify-content-between align-items-center">
                            <div>
                              <i className="fe-icon-shopping-bag mr-1 text-muted"></i>
                              <div className=" no-underline d-inline-block font-weight-medium text-uppercase ">
                                Product list
                              </div>
                            </div>
                            <span className="badge badge-secondary">{formData.seller.products.length}</span>
                          </div>
                        </Link>
                      </div>
                    }
                    <a id="tab-badge" className="white-border list-group-item bg-warning" href="#" onClick={() => handleSetActiveSetting('badge')} style={{ borderLeftColor: "white!important" }}>
                      <i className="fe-icon-user text-muted"></i>Badges
                    </a>
                    <a id="tab-metric" className="list-group-item bg-light" href="#" onClick={() => handleSetActiveSetting('metric')}>
                      <i className="fe-icon-user text-muted"></i>Metrics
                    </a>
                    {(formData.roles.reduce((isAdmin = false, role) => { return isAdmin || role.name === "ADMIN" }, false)) &&
                      <a id="tab-admin" className="list-group-item bg-light" href="#" onClick={() => handleSetActiveSetting('admin')}>
                        <div className="d-flex justify-content-between align-items-center">
                          <div>
                            <i className="fe-icon-user text-muted"></i>Admin
                          </div>
                          <h6 style={{ margin: 0 }}>🛠️</h6>
                        </div>
                      </a>
                    }
                  </nav>
                </div>
              </div>
              {/* <!-- Profile Settings--> */}
              {activeSetting == 'buyer' &&
                <div className="col-lg-8 pb-5">
                  <h1>Buyer</h1>
                  <form className="row">
                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-fn">First Name</label>
                        <input
                          className="form-control"
                          type="text"
                          id="account-fn"
                          name="buyer.name"
                          value={formData.buyer.name}
                          onChange={handleChange}
                          required=""
                        />
                        {errors.firstName && (
                          <div className="text-danger">{errors.firstName}</div>
                        )}
                      </div>
                    </div>
                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-ln">Last Name</label>
                        <input
                          className="form-control"
                          type="text"
                          id="account-ln"
                          name="buyer.surname"
                          value={formData.buyer.surname}
                          onChange={handleChange}
                          required=""
                        />
                        {errors.lastName && (
                          <div className="text-danger">{errors.lastName}</div>
                        )}
                      </div>
                    </div>
                    <div className="col-md-12">
                      <div className="form-group">
                        <label htmlFor="account-email">Biography</label>
                        <textarea
                          className="form-control"
                          type="text"
                          id="account-email"
                          name="buyer.bio"
                          value={formData.buyer.bio}
                          onChange={handleChange}
                          disabled=""
                        />
                      </div>
                    </div>

                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-ln">Birthday</label>
                        <input
                          className="form-control"
                          type="date"
                          id="account-ln"
                          name="buyer.birthday"
                          value={formData.buyer.birthday ? new Date(formData.buyer.birthday).toISOString().split('T')[0] : new Date().toISOString().split('T')[0]}
                          onChange={handleChange}
                          required=""
                        />
                        {errors.lastName && (
                          <div className="text-danger">{errors.lastName}</div>
                        )}
                      </div>
                    </div>

                    <div className="col-md-3">
                      <div className="form-group">
                        <label htmlFor="account-commission">Commission</label>
                        <div class="tooltip-container">
                          <p class="beautiful-text" data-tooltip="Percentage for making purchases">{formData.buyer.commissionPercentage} %</p>
                        </div>
                      </div>
                    </div>

                    <div className="col-md-3">
                      <div className="form-group">
                        <label htmlFor="account-buyer-id">Buyer id</label>
                        <div class="tooltip-container">
                          <p class="beautiful-text" data-tooltip="Your buyer id">{formData.buyer.id}</p>
                        </div>
                      </div>
                    </div>

                    <h3 htmlFor="account-cart">Cart info</h3>
                    <div className="col-md-3">
                      <div className="form-group">
                        <label htmlFor="account-cart-id">ID</label>
                        <div class="tooltip-container">
                          <p class="beautiful-text" data-tooltip="Your cart id">{formData.buyer.cart.id}</p>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-3">
                      <div className="form-group">
                        <label htmlFor="account-cart">Items count</label>
                        <div class="tooltip-container">
                          <p class="beautiful-text" data-tooltip="Cart items count">{formData.buyer.cart.cartItems.length}</p>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-3">
                      <div className="form-group">
                        <label htmlFor="account-cart">Payment method</label>
                        <div class="tooltip-container">
                          <p class="" data-tooltip="Payment method for order">{formData.buyer.cart.paymentMethod && formData.buyer.cart.paymentMethod.title}&nbsp;</p>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-3">
                      <div className="form-group">
                        <label htmlFor="account-cart">Shipping address</label>
                        <div class="tooltip-container">
                          <p class="" data-tooltip="Shipping address for order">{formData.buyer.cart.shippingAddress && formData.buyer.cart.shippingAddress.title}&nbsp;</p>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-12">
                      <div className="form-group">
                        <label htmlFor="account-cart">Shipping address Detail</label>
                        <div class="tooltip-container">
                          <p class="" data-tooltip="Shipping address for order">{joinAddress(formData.buyer.cart.shippingAddress && formData.buyer.cart.shippingAddress.address)}&nbsp;</p>
                        </div>
                        <div class="tooltip-container">
                          <p> {formData.buyer.cart.shippingAddress && formData.buyer.cart.shippingAddress.geoLocation.latitude} {formData.buyer.cart.shippingAddress && formData.buyer.cart.shippingAddress.geoLocation.longitude}</p>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-12" style={{ width: "100px" }}>

                    </div>
                    <div className="col-md-12">
                      <p>You can change your cart in <Link to="/cart" style={{ color: "blue" }}>Cart</Link> page</p>
                    </div>

                    <div className="col-12">
                      <hr className="mt-2 mb-3" />
                      <div className="d-flex flex-wrap justify-content-between align-items-center">
                        <div className="custom-control custom-checkbox d-block">

                        </div>
                        <button
                          className="btn btn-style-1 btn-primary"
                          type="button"
                          onClick={handleSubmit}
                        >
                          Update Profile
                        </button>
                      </div>
                    </div>
                  </form>
                </div>
              }
              {activeSetting == 'user' &&
                <div className="col-lg-8 pb-5">
                  <h1>User</h1>
                  <form className="row">
                    <p>ID: {formData.id}</p>
                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-ln">Email</label>
                        <input
                          className="form-control"
                          type="text"
                          id="account-ln"
                          value={formData.email}
                          onChange={(e) => e.stopPropagation()}
                          required=""
                        />
                      </div>
                    </div>
                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-ln">Registered time</label>
                        <input
                          className="form-control beautiful-text" data-tooltip={formData.registeredTime}
                          type="date"
                          id="account-ln"
                          value={new Date(formData.registeredTime).toISOString().split('T')[0]}
                          onChange={(e) => e.stopPropagation()}
                          required=""
                        />
                      </div>
                    </div>
                  </form>
                  <div className="row">
                    <div className="col-md-6 p-2">
                      <h3>Account status</h3>
                      <div className="col p-2">
                        <div className="custom-control custom-checkbox d-block">
                          <input
                            className="custom-control-input"
                            type="checkbox"
                            name="subscribe"
                            checked={formData.enabled}
                            onClick={(e) => e.stopPropagation()}
                          />
                          <label
                            className="custom-control-label"
                            htmlFor="subscribe_me"
                          >
                            Enabled account
                          </label>
                        </div>
                      </div>
                      <div className="col p-2">
                        <div className="custom-control custom-checkbox d-block">
                          <input
                            className="custom-control-input"
                            type="checkbox"
                            name="subscribe"
                            checked={formData.credentialsNonExpired}
                            onClick={(e) => e.stopPropagation()}
                          />
                          <label
                            className="custom-control-label"
                            htmlFor="subscribe_me"
                          >
                            Non expired credentials
                          </label>
                        </div>
                      </div>
                      <div className="col p-2">
                        <div className="custom-control custom-checkbox d-block">
                          <input
                            className="custom-control-input"
                            type="checkbox"
                            name="subscribe"
                            checked={formData.accountNonExpired}
                            onClick={(e) => e.stopPropagation()}
                          />
                          <label
                            className="custom-control-label"
                            htmlFor="subscribe_me"
                          >
                            Non expired account
                          </label>
                        </div>
                      </div>
                      <div className="col p-2">
                        <div className="custom-control custom-checkbox d-block">
                          <input
                            className="custom-control-input"
                            type="checkbox"
                            name="subscribe"
                            checked={formData.accountNonLocked}
                            onClick={(e) => e.stopPropagation()}
                          />
                          <label
                            className="custom-control-label"
                            htmlFor="subscribe_me"
                          >
                            Non locked account
                          </label>
                        </div>
                      </div>
                    </div>
                    <div className="col-md-6 p-2">
                      <ChangeTransparentTables transparentCurrent={formData.authenticationTransparentPolicies} isLoading={setIsLoading}></ChangeTransparentTables>
                    </div>
                  </div>
                  <hr />
                  <div className="">
                    <Table initialData={formData.roles} initialTableName="Roles"></Table>
                  </div>
                  <div className="">
                    <Table initialData={[...formData.authenticationTransparentPolicies, { "id": "", "name": "", "type": "", "value": "", "createdTime": "" }]} initialTableName="Transparent Policies"></Table>
                  </div>
                </div>
              }
              {activeSetting == 'seller' &&
                <div className="col-lg-8 pb-5">
                  <h1>Seller</h1>
                  <form className="row">
                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-fn">First Name</label>
                        <input
                          className="form-control"
                          type="text"
                          name="seller.name"
                          value={formData.seller.name}
                          onChange={handleChange}
                          required=""
                        />
                        {errors.firstName && (
                          <div className="text-danger">{errors.firstName}</div>
                        )}
                      </div>
                    </div>
                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-ln">Last Name</label>
                        <input
                          className="form-control"
                          type="text"
                          name="seller.surname"
                          value={formData.seller.surname}
                          onChange={handleChange}
                          required=""
                        />
                        {errors.lastName && (
                          <div className="text-danger">{errors.lastName}</div>
                        )}
                      </div>
                    </div>
                    <div className="col-md-12">
                      <div className="form-group">
                        <label htmlFor="account-email">Biography</label>
                        <textarea
                          className="form-control"
                          type="text"
                          name="seller.bio"
                          value={formData.seller.bio}
                          onChange={handleChange}
                          disabled=""
                        />
                      </div>
                    </div>

                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-ln">Birthday</label>
                        <input
                          className="form-control"
                          type="date"
                          name="seller.birthday"
                          value={formData.seller.birthday ? new Date(formData.seller.birthday).toISOString().split('T')[0] : new Date().toISOString().split('T')[0]}
                          onChange={handleChange}
                          required=""
                        />
                        {errors.lastName && (
                          <div className="text-danger">{errors.lastName}</div>
                        )}
                      </div>
                    </div>

                    <div className="col-md-3">
                      <div className="form-group">
                        <label htmlFor="account-commission">Commission</label>
                        <div class="tooltip-container">
                          <p class="beautiful-text" data-tooltip="Percentage for selling purchases">{formData.seller.commissionPercentage} %</p>
                        </div>
                      </div>
                    </div>

                    <div className="col-md-3">
                      <div className="form-group">
                        <label htmlFor="account-seller-id">Seller id</label>
                        <div class="tooltip-container">
                          <p class="beautiful-text" data-tooltip="Your seller id">{formData.seller.id}</p>
                        </div>
                      </div>
                    </div>

                    <div className="col-md-6">
                      <div className="form-group">
                        <label htmlFor="account-ln">Registered time</label>
                        <input
                          className="form-control beautiful-text" data-tooltip={formData.registeredTime}
                          type="date"
                          id="account-ln"
                          value={new Date(formData.seller.registeredTime).toISOString().split('T')[0]}
                          onChange={(e) => e.stopPropagation()}
                          required=""
                        />
                      </div>
                    </div>

                    <div className="col-md-12">
                      <Table initialData={formData.seller.products} initialTableName="Products" initialItemsPerPage={5} ></Table>
                    </div>

                    <div className="col-md-12">
                      <p>You can see your products in <Link to="/" style={{ color: "blue" }}>Seller products</Link> page</p>
                    </div>

                    <div className="col-12">
                      <hr className="mt-2 mb-3" />
                      <div className="d-flex flex-wrap justify-content-between align-items-center">
                        <div className="custom-control custom-checkbox d-block">

                        </div>
                        <button
                          className="btn btn-style-1 btn-primary"
                          type="button"
                          onClick={handleSubmit}
                        >
                          Update Profile
                        </button>
                      </div>
                    </div>
                  </form>
                </div>
              }
              {(isLoadedImages && (activeSetting == 'badge') && formData.buyer && formData.buyer.badges) &&
                <div className="col-lg-8 pb-5">
                  <div><h3>Badges</h3></div>
                  <BadgesGrid badges={formData.buyer.badges}></BadgesGrid>
                </div>
              }
              {activeSetting == 'metric' &&
                <div className="col-lg-8 pb-5">
                  <div className="d-flex" style={{ flexWrap: 'wrap' }}>

                    <LoyaltyCard value={loyalty && loyalty.toFixed(2)} title="Loyalty Index Buyer"></LoyaltyCard>
                    {formData.seller && <LoyaltyCard value={loyaltySeller && loyaltySeller.toFixed(2)} title="Loyalty Index Seller"></LoyaltyCard>}
                    <LoyaltyCard value={clv.clvsAverage.toFixed(2)} title="CLV" tresholds={[2, 0]}></LoyaltyCard>
                    <LoyaltyCard value={orders.reduce((sumo, order) => { return sumo + order.orderItems.reduce((sumoi, orderItem) => { return sumoi + orderItem.price * orderItem.amount }, 0) }, 0)} title="Money spent" tresholds={[500, 100]} tresholdCoef={-1} textAdd="$"></LoyaltyCard>
                    <LoyaltyCard value={formData.buyer.badges.length} title="Badges Count" tresholds={[5, 2]}></LoyaltyCard>
                  </div>
                  <Diagram coefficient={3600000 * 2} data={
                    clv.clvsGapsStatic.map(item => ({
                      x: Number(new Date(item.dateTime) / (3600000 * 2)), // Transforming date to a readable format
                      y: -item.value // Keeping value as y
                    }))
                  } title={"CLV Order gaps static"} xAxisName={"Date"} yAxisName={"CLV index"}></Diagram>
                  <br></br>
                  <Diagram coefficient={3600000 * 2} data={
                    clv.clvsGapsDelta.map(item => ({
                      x: Number(new Date(item.dateTime) / (3600000 * 2)), // Transforming date to a readable format
                      y: -item.value // Keeping value as y
                    }))
                  } title={"CLV Order gaps delta"} xAxisName={"Date"} yAxisName={"CLV index"}></Diagram>
                </div>
              }
              {activeSetting == 'admin' &&
                <div className="col-lg-8 pb-5">
                  <Link to="https://docs.google.com/spreadsheets/d/1fku7j-Uwp1QvNj2xliEHk9F8nhjHeP-V8pS844uvebY/edit#gid=1623796006" className="btn btn-success mb-3" style={{ width: "100%" }}>
                    💻Admin panel
                  </Link>
                  <div>
                    <div style={styles.infoContainer}>
                      <img src="https://static-00.iconduck.com/assets.00/digital-ocean-color-icon-256x256-15ougnla.png" alt="Description" style={{ margin: 0, width: "32px" }} />
                      <a href="http://104.248.234.194/" style={styles.link}>104.248.234.194</a>
                    </div>
                    <div style={styles.infoContainer}>
                      <img src="https://payoneer.gallerycdn.vsassets.io/extensions/payoneer/grafana-annotations/1.3.1/1659271747890/Microsoft.VisualStudio.Services.Icons.Default" alt="Description" style={{ margin: 0, width: "32px" }} />
                      <a href="http://104.248.234.194:3000/d/f68f9853-28c8-4e0f-872e-8d8dd0e5226d/" style={{ ...styles.link, color: "orange" }}>Graphana dashboard</a>
                    </div>
                    <div style={styles.infoContainer}>
                      <img src="https://cdn.iconscout.com/icon/free/png-256/free-prometheus-282488.png?f=webp" alt="Description" style={{ margin: 0, width: "32px" }} />
                      <a href="http://104.248.234.194:9090/" style={{ ...styles.link, color: "#db4e30" }}>Prometheus container</a>
                    </div>
                    <div style={styles.infoContainer}>
                      <img src="https://cf.appdrag.com/dashboard-openvm-clo-b2d42c/uploads/minio-icon-rbzC.png" alt="Description" style={{ margin: 0, width: "32px" }} />
                      <a href="http://104.248.234.194:9001/" style={{ ...styles.link, color: "#c50036" }}>Minio files</a>
                    </div>
                    <div style={styles.infoContainer}>
                      <img src="https://static-00.iconduck.com/assets.00/swagger-icon-256x256-j80nuve7.png" alt="Description" style={{ margin: 0, width: "32px" }} />
                      <a href="http://104.248.234.194:8080/api/swagger-ui/index.html" style={{...styles.link, color:"green"}}>Swagger API</a>
                    </div>
                    <div>
                      <h2>Actuator Endpoints</h2>
                      <ul>
                        <li><a href="http://104.248.234.194:8080/api/actuator/info" style={styles.link}>Info</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/metrics" style={styles.link}>Metrics</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/env" style={styles.link}>Environment</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/beans" style={styles.link}>Beans</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/loggers" style={styles.link}>Loggers</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/trace" style={styles.link}>Trace</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/httptrace" style={styles.link}>HTTP Trace</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/mappings" style={styles.link}>Mappings</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/threaddump" style={styles.link}>Thread Dump</a></li>
                        <li><a href="http://104.248.234.194:8080/api/actuator/jvm" style={styles.link}>JVM</a></li>
                      </ul>
                    </div>

                  </div>
                </div>
              }
            </div>
          </div>
        </div>}
    </div>
  );
};

export default UpdPageProfile;
