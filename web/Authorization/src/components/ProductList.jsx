import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./OrdersList"
import { useAsyncError } from "../commons";
import api from "../apis/api";
import imageApi from "../apis/imageApi";
import { Table, Tag } from ".";
import CreateProduct from "./Seller/CreateProduct";

const ProductList = () => {
  const [formData, setFormData] = useState({
  });
  const [products, setProducts] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [update, setUpdate] = useState(false);

  const [loadingText, setLoadingText] = useState('...Loading');
  const [isLoading, setIsLoading] = useState(false);

  const throwAsyncError = useAsyncError();
  const navigate = useNavigate();

  useEffect(() => {

    const fetchData = async () => {
      try {
        let data = await api('/api/private/seller/profile', {
          method: "GET",
          headers: {
            "Content-Type": "application/json"
          },
          params: {
            page: 0,
            size: Number.MAX_SAFE_INTEGER
          }
        });

        const products = data.products;
        if (Array.isArray(products)) {
          setLoadingText('...Loading Images');
          await Promise.all(products.map(async product => {
            setIsLoading(true);
            product.image_filename = await imageApi(product.image_filename);
          }));
          setProducts(products);
          setLoadingText('...Fetching Data');
          setIsLoading(false);
        }

      } catch (error) {
        //navigate("/");
        throwAsyncError(error);
      }
    };
    setIsLoading(true);
    fetchData();
    setIsLoading(false);
  }, [update]);

  function closeChange() {
    setSelectedProduct(null);
    setUpdate(!update);
  }

  return (
    <div>
      {isLoading && (
        <div className="loading-back">
          <div className="loading-indicator">
            <div className="loading-circle"></div>
            <p>{loadingText}</p>
          </div>
        </div>
      )}
      <link
        href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css"
        rel="stylesheet"
      />
      {(selectedProduct != null) &&
        <div style={{ position: "fixed", zIndex: "999999", left: "50%", top: "50%", transform: "translateX(-50%) translateY(-50%)" }}>
          <button className="btn btn-outline-dark" onClick={() => { setSelectedProduct(null) }} style={{ top: "50px", position: "fixed", zIndex: "999999" }}>Close</button>
          <CreateProduct product={products[selectedProduct]} isCreate={false} closeTab={closeChange}></CreateProduct>
        </div>
      }
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
                      src={`./avatars/images/ava_${((formData.buyer.id + 10) % 25 + 1).toString().padStart(2, '0')}.gif`}
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
                        Product List
                      </div>
                    </div>
                    <span class="badge badge-secondary">{products.length}</span>
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
                    <th>Product #</th>
                    <th>Created Date</th>
                    <th>Expiration Date</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Image</th>
                    <th>Price</th>
                    <th>Change</th>
                    <th>View</th>
                  </tr>
                </thead>
                <tbody>
                  {products && products.map((product, index) => {
                    return (
                      <React.Fragment key={index}>
                        <tr>
                          <td>
                            <a
                              class="navi-link"
                              href="#order-details"
                              data-toggle="modal"
                            >
                              {product.id}
                            </a>
                          </td>
                          <td>{new Date(product.createdTime).toISOString().split('T')[0]}</td>
                          <td>{new Date(product.expirationDate).toISOString().split('T')[0]}</td>
                          <td>
                            <p>{product.name}</p>
                          </td>
                          <td>
                            <p>{product.description}</p>
                          </td>
                          <td>
                            <img className="image-blob" src={product.image_filename} />
                          </td>
                          <td>
                            <span>${product.price}</span>
                          </td>
                          <td>
                            <button className="btn btn-outline-dark" onClick={() => { setSelectedProduct(index) }}>Change</button>
                          </td>
                          <td>
                            <Link to={`/product/${product.id}`} className="btn btn-outline-dark" style={{ color: "blue" }}>View</Link>
                          </td>
                        </tr>
                        <tr key={index}>
                          <td colSpan="4" className="bg-light">
                            <div>
                              <strong>Tags:</strong> <Tag types={product.tags.map(tag => tag.type)} texts={product.tags.map(tag => tag.title)}></Tag>
                            </div>
                            <div>
                              <strong>Categories:</strong> <Tag types={product.categories.map(category => category.type)} texts={product.categories.map(category => category.name)}></Tag>
                            </div>
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

export default ProductList;
