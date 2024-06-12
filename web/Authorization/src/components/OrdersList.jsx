import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./OrdersList"
import { useAsyncError } from "../commons";
import api from "../apis/api";
import imageApi from "../apis/imageApi";
import { Table } from "../components";

const OrdersList = () => {
  const [formData, setFormData] = useState({
  });
  const [orders, setOrders] = useState([]);

  const [loadingText, setLoadingText] = useState('...Loading');
  const [isLoading, setIsLoading] = useState(false);

  const throwAsyncError = useAsyncError();
  const navigate = useNavigate();

  const joinAddress = (addressObj) => {
    return `${addressObj.streetAddress}, ${addressObj.city}, ${addressObj.state}, ${addressObj.country}, ${addressObj.postalCode}`;
  };

  useEffect(() => {

    const fetchData = async () => {
      try {
        let data = await api('/api/private/buyer/order/list', {
          method: "GET",
          headers: {
            "Content-Type": "application/json"
          },
          params: {
            page: 0,
            size: Number.MAX_SAFE_INTEGER
          }
        });
        
        const orders = data.content;
        
        if (Array.isArray(orders)) {
          setLoadingText('...Loading Images');
          await Promise.all(orders.map(async order => {
            setIsLoading(true);
            await Promise.all(order.orderItems.map(async orderItem => {
              setIsLoading(true);
              orderItem.product.image_filename = await imageApi(orderItem.product.image_filename);
            }));
          }));
          setLoadingText('...Fetching Data');
        }
        

        if (orders) {
          setOrders(orders);
        }
        data = await api('/api/private/profile', {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
        });

        if (data) {
          setFormData(data.authentication);
        }

      } catch (error) {
        navigate("/");
        //throwAsyncError(error);
      }
      setIsLoading(false);
    };

    fetchData();
  }, []);

  return (
    <div>
      
      <link
        href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"
        rel="stylesheet"
      />
      {isLoading && (
        <div className="loading-back">
          <div className="loading-indicator">
            <div className="loading-circle"></div>
            <p>{loadingText}</p>
          </div>
        </div>
      )}
      <div class="container mb-4 main-container">
        <div class="row">
          <div class="col-lg-4 pb-5">
            {/* <!-- Account Sidebar--> */}
            {formData.buyer &&
              <div class="author-card pb-3">
                <div
                  class="author-card-cover"
                // style="background-image: url(https://bootdey.com/img/Content/flores-amarillas-wallpaper.jpeg);"
                >
                  <a
                    class="btn btn-style-1 btn-white btn-sm"
                    href="#"
                    data-toggle="tooltip"
                    title=""
                    data-original-title={`You currently have ${formData.buyer ? formData.buyer.badges.length : 0} Badges`}
                  >
                    <i class="fa fa-award text-md"></i>&nbsp;{formData.buyer ? formData.buyer.badges.length : 0} Badges
                  </a>
                </div>
                <div class="author-card-profile">
                  <div class="author-card-avatar">
                    <img
                      src={`./avatars/images/ava_${((formData.buyer.id) % 25 + 1).toString().padStart(2, '0')}.gif`}
                      alt={formData.buyer.name}
                      style={{ width: '400px', height: '400px' }}
                    />
                  </div>
                  <div class="author-card-details">
                    <h5 class="author-card-name text-lg">{formData.buyer.name} {formData.buyer.surname}</h5>
                    <span class="author-card-position">
                      Joined {new Date(formData.registeredTime).toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric' })}
                    </span>
                  </div>
                </div>
              </div>
            }
            <div class="wizard">
              <nav class="list-group list-group-flush">
                <a class="list-group-item active" href="#">
                  <div class="d-flex justify-content-between align-items-center">
                    <div>
                      <i class="fa fa-shopping-bag mr-1 text-muted"></i>
                      <div class="d-inline-block font-weight-medium text-uppercase">
                        Orders List
                      </div>
                    </div>
                    <span class="badge badge-secondary">{orders.length}</span>
                  </div>
                </a>
                <Link to="/profile" class="list-group-item">
                  <i class="fa fa-user text-muted"></i>Profile Settings
                </Link>
              </nav>
            </div>
          </div>
          {/* <!-- Orders Table--> */}
          <div class="col-lg-8 pb-5">
            <div class="d-flex justify-content-end pb-3">
              <div class="form-inline">
                <label class="text-muted mr-3" for="order-sort">
                  &nbsp;
                </label>
                {/*
                <label class="text-muted mr-3" for="order-sort">
                  Sort Orders
                </label>
                <select class="form-control" id="order-sort">
                  <option>All</option>
                  <option>Delivered</option>
                  <option>In Progress</option>
                  <option>Delayed</option>
                  <option>Canceled</option>
                </select>
                */}
              </div>
            </div>
            <div class="table-responsive">
              <table class="table table-hover mb-0">
                <thead>
                  <tr>
                    <th>Order #</th>
                    <th>Date Purchased</th>
                    <th>Status</th>
                    <th>Total</th>
                  </tr>
                </thead>
                <tbody>
                  {orders && orders.map((order, index) => {
                    const orderItems_ = [];
                    order.orderItems.map((item, itemIndex) => {
                      const item_ = { ...item };
                      item_.productReview = !item_.productReview ? `❌,${item.product.id},${item.id}` : "⭐".repeat(item_.productReview.rating);
                      item_.product = item_.product ? item_.product.image_filename : "";
                      delete item_.createdTime;
                      orderItems_.push(item_);
                    });
                    return (
                      <React.Fragment key={index}>
                        <tr>
                          <td>
                            <a
                              class="navi-link"
                              href="#order-details"
                              data-toggle="modal"
                            >
                              {order.id}
                            </a>
                          </td>
                          <td>{new Date(order.createdTime).toISOString().split('T')[0]}</td>
                          <td>
                            <span class="badge badge-info m-0">In Progress</span>
                          </td>
                          <td>
                            <span>${order.price}</span>
                          </td>
                        </tr>
                        <tr key={index}>
                          <td colSpan="4" className="bg-light">
                            <div>
                              <strong>Shipping Address:</strong> {joinAddress(order.shippingAddress.address)}
                            </div>
                            <div>
                              <strong>Payment Method:</strong> <span class="badge badge-warning m-0">{`${order.paymentMethod.title}`}</span>
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <td colSpan="4" className="bg-light">
                            <Table initialData={orderItems_} initialItemsPerPage={2} initialTableName="Order items"></Table>
                          </td>
                        </tr>
                      </React.Fragment>)
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div >
  );
};

export default OrdersList;
