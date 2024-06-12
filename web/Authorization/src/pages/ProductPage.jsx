import React, { useEffect, useState } from "react";
import Skeleton from "react-loading-skeleton";
import { Link, useParams, useLocation, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { addCart } from "../redux/action";
import { useAsyncError } from "../commons";

import { Footer, Navbar } from "../commons";
import api from "../apis/api";
import { Tag } from "../components";
import { ProductReview } from "../components";

const ProductPage = () => {
  const { id } = useParams(); // Получаем id из параметров маршрута
  const [product, setProduct] = useState(null);
  const [orders, setOrders] = useState([]);
  const [similarProducts, setSimilarProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [loading2, setLoading2] = useState(true);
  const throwAsyncError = useAsyncError();
  const navigate = useNavigate();

  const dispatch = useDispatch();

  const addProduct = async (id) => {
    try {
      const response = await api(`/api/private/buyer/cart/add?id=${id}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });
    } catch (error) {
      throwAsyncError(error);
    } finally {
      window.location.reload();
    }
  };

  const getOrders = async (id) => {
    try {
      const response = await api(`/api/private/buyer/product/id/orders?id=${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      console.log(response, "{{{");
      setOrders(response);
    } catch (error) {
      throwAsyncError(error);
    }
  };

  useEffect(() => {
    const getProduct = async () => {
      setLoading(true);
      setLoading2(true);
      try {
        const response = await api(`/api/public/product/?id=${id}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        });
        const data = response;
        console.log(data);
        setProduct(data);

        // Дополнительный запрос для получения похожих продуктов
        // const response2 = await api(`api/public/product/${id}/similar`, {
        //   method: "GET",
        //   headers: {
        //     "Content-Type": "application/json",
        //   },
        // });
        // const data2 = await response2.json();
        // setSimilarProducts(data2);
      } catch (error) {
        console.error("Error fetching product data:", error);
      }
      setLoading(false);
      setLoading2(false);
    };
    getProduct();
    getOrders(id);
  }, [id]);

  const joinAddress = (addressObj) => {
    return `${addressObj.streetAddress}, ${addressObj.city}, ${addressObj.state}, ${addressObj.country}, ${addressObj.postalCode}`;
  };

  const Loading = () => {
    return (
      <div className="container my-5 py-2">
        <div className="row">
          <div className="col-md-6 py-3">
            <Skeleton height={400} width={400} />
          </div>
          <div className="col-md-6 py-5">
            <Skeleton height={30} width={250} />
            <Skeleton height={90} />
            <Skeleton height={40} width={70} />
            <Skeleton height={50} width={110} />
            <Skeleton height={120} />
            <Skeleton height={40} width={110} inline={true} />
            <Skeleton className="mx-3" height={40} width={110} />
          </div>
        </div>
      </div>
    );
  };

  const ShowProduct = () => {
    const [isButtonClicked, setIsButtonClicked] = useState(false);

    const styles = {
      container: {
        backgroundColor: '#f9f9f9',
        borderRadius: '10px',
        padding: '20px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        transition: 'all 0.3s ease-in-out',
      },
      image: {
        border: '1px solid #ddd',
        padding: '5px',
        backgroundColor: '#fff',
        maxWidth: '100%',
        height: 'auto',
        borderRadius: '8px',
        transition: 'transform 0.2s',
      },
      display5: {
        color: '#333',
      },
      textSuccess: {
        color: '#28a745',
      },
      textUppercase: {
        fontSize: '0.9rem',
        textTransform: 'uppercase',
        color: '#6c757d',
      },
      btnOutlineDark: {
        border: '1px solid #333',
        marginRight: '10px',
        transition: 'all 0.3s ease',
      },
      btnOutlineDarkActive: {
        border: '1px solid #333',
        color: '#fff',
        backgroundColor: '#333',
        marginRight: '10px',
        transition: 'all 0.3s ease',
      },
      btnDark: {
        backgroundColor: '#333',
        color: '#fff',
        transition: 'background-color 0.3s ease',
      },
      btnDarkHover: {
        backgroundColor: '#555',
      },
      sellerInfo: {
        marginTop: '20px',
      },
      sellerTitle: {
        color: '#6c757d',
        marginBottom: '10px',
      },
      sellerText: {
        color: '#343a40',
      },
    };


    if (!product) return null;
    return (
      <>
        <div className="container my-5 py-4" style={styles.container}>
          <div className="row">
            <div className="col-md-6 col-sm-12 py-3">
              <img
                className="img-fluid rounded shadow-sm"
                src={product.image_filename || "default.png"}
                alt={product.name || "Product Image"}
                style={styles.image}
              />
            </div>
            <div className="col-md-6 col-sm-12 py-5">
              <h4
                className="text-uppercase text-muted"
                style={styles.textUppercase}
              >
                Product ID: {product.id || "N/A"}
              </h4>
              <h1 className="display-5 fw-bold" style={styles.display5}>
                {product.name || "Product Name"}
              </h1>
              <h3 className="display-6 my-4" style={styles.textSuccess}>
                ${product.price || "0.00"}
              </h3>
              <p className="lead mb-4">
                {product.description || "No description available"}
              </p>
              <p className="text-muted">
                <strong>Created Time:</strong>{" "}
                {new Date(product.createdTime).toLocaleString() || "N/A"}
              </p>
              <p className="text-muted">
                <strong>Expiration Date:</strong>{" "}
                {product.expirationDate || "N/A"}
              </p>
              <div>
                <label><strong>Categories</strong></label>
                <Tag types={product.categories.map(category => category.type)} texts={product.categories.map(category => category.name)}></Tag>
              </div>
              <div>
                <label><strong>Tags</strong></label>
                <Tag types={product.tags.map(tag => tag.type)} texts={product.tags.map(tag => tag.title)}></Tag>
              </div>
              <div className="my-4">
                <button
                  className="btn btn-outline-dark"
                  onClick={() => addProduct(product.id)}
                  style={styles.btnOutlineDark}
                >
                  Add to Cart
                </button>
                <Link
                  to="/cart"
                  className="btn btn-dark"
                  style={styles.btnDark}
                >
                  Go to Cart
                </Link>
              </div>
              <div className="mt-5">
                <h4
                  className="text-uppercase text-muted"
                  style={styles.textUppercase}
                >
                  Seller Information
                </h4>
                <p className="lead fw-bold">
                  {product.seller?.name || "Name"}{" "}
                  {product.seller?.surname || "Surname"}
                </p>
                <p className="text-muted">
                  <strong>Bio:</strong>{" "}
                  {product.seller?.bio || "No bio available"}
                </p>
                <p className="text-muted">
                  <strong>Birthday:</strong>{" "}
                  {product.seller?.birthday
                    ? new Date(product.seller.birthday).toLocaleDateString()
                    : "N/A"}
                </p>
                <p className="text-muted">
                  <strong>Commission:</strong>{" "}
                  {product.seller?.commissionPercentage != null
                    ? `${product.seller.commissionPercentage}%`
                    : "N/A"}
                </p>
                <p className="text-muted">
                  <strong>Registered Time:</strong>{" "}
                  {product.seller?.registeredTime
                    ? new Date(product.seller.registeredTime).toLocaleString()
                    : "N/A"}
                </p>
              </div>
            </div>
          </div>
          <div>
            <h3>Reviews</h3>

            <p>Average ⭐ {(orders.reduce((acc, order) => { if (order && order.orderItems[0].productReview) {acc += order.orderItems[0].productReview.rating} return acc }, 0) / orders.length).toFixed(2)}/5</p>

            <hr></hr>
            {orders && orders.map((order, index) => (order &&
              <div>
                <div className="d-flex">
                  <h2 className="mr-2">Order #{order.id}</h2>
                  <Tag types={['new', 'popular', 'sale']} texts={[
                    order.orderItems[0].productReview && order.orderItems[0].productReview.buyer ? "Buyer" : null,
                    order.price ? "Price" : null,
                    order.createdTime ? "Date" : null
                  ]}></Tag>
                </div>
                <div>
                  <h6>Payment: {order.paymentMethod.title}</h6>
                  <h6>Address: {joinAddress(order.shippingAddress.address)}</h6>
                  {order.createdTime && <h6>Date: {new Date(order.createdTime).toLocaleString('en-GB', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' })}</h6>}
                  {order.price && <h6>Order Sum checkup: <strong>{order.price}$</strong></h6>}
                </div>
                {order.orderItems[0].productReview && order.orderItems[0].productReview.buyer &&
                  <div>
                    <div className="card user-card">
                      <div className="card-body">
                        <div className="d-flex align-items-center">
                          <img src={`${process.env.PUBLIC_URL}/avatars/images/ava_${((order.orderItems[0].productReview.buyer.id) % 25 + 1).toString().padStart(2, '0')}.gif`} alt={order.orderItems[0].productReview.buyer.name} className="avatar mr-3 image-blob" />
                          <div>
                            <h5 className="card-title mb-0">{order.orderItems[0].productReview.buyer.name}</h5>
                            <p className="card-text mb-0">{order.orderItems[0].productReview.buyer.bio}</p>
                            <p><i className="fa fa-award text-md"></i>&nbsp;{order.orderItems[0].productReview.buyer.badges.length} </p>
                          </div>
                        </div>
                        <p className="card-text mt-3">Registered: {new Date(order.orderItems[0].productReview.buyer.registeredTime).toLocaleString()}</p>
                      </div>
                    </div>
                  </div>
                }
                {order.orderItems[0].productReview &&
                  <div>
                    <ProductReview review={order.orderItems[0].productReview}></ProductReview>
                  </div>
                }
              </div>
            ))}
          </div>
        </div>
      </>
    );
  };

  const Loading2 = () => {
    return (
      <div className="my-4 py-4">
        <div className="d-flex">
          <div className="mx-4">
            <Skeleton height={400} width={250} />
          </div>
          <div className="mx-4">
            <Skeleton height={400} width={250} />
          </div>
          <div className="mx-4">
            <Skeleton height={400} width={250} />
          </div>
          <div className="mx-4">
            <Skeleton height={400} width={250} />
          </div>
        </div>
      </div>
    );
  };

  const ShowSimilarProduct = () => {
    if (!similarProducts.length) return null;

    return (
      <div className="py-4 my-4">
        <div className="d-flex">
          {similarProducts.map((item) => {
            return (
              <div key={item.id} className="card mx-4 text-center">
                <img
                  className="card-img-top p-3"
                  src={item.image_filename}
                  alt="Card"
                  height={300}
                  width={300}
                />
                <div className="card-body">
                  <h5 className="card-title">{item.name}</h5>
                </div>
                <div className="card-body">
                  <Link to={"/product/" + item.id} className="btn btn-dark m-1">
                    Buy Now
                  </Link>
                  <button
                    className="btn btn-dark m-1 pr"
                    onClick={() => addProduct(item)}
                  >
                    Add to Cart
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    );
  };

  return (
    <>
      <Navbar />
      <div className="container">
        <div className="row">{loading ? <Loading /> : <ShowProduct />}</div>
        <div className="row my-5 py-5">
          {/* <div className="d-none d-md-block">
            <h2 className="">You may also Like</h2>
            {loading2 ? <Loading2 /> : <ShowSimilarProduct />}
          </div> */}
        </div>
      </div>
      <Footer />
    </>
  );
};

export default ProductPage;
