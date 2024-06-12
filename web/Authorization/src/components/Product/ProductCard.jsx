import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { useAsyncError } from "../../commons";
import api from "../../apis/api";
import Tag from '../Tag/Tag';


const ProductCard = ({ product, setIsLoading, updateNavbar }) => {
  const throwAsyncError = useAsyncError();

  const addProduct = async (productId) => {
    setIsLoading(true);
    try {
      console.log(productId);
      const response = await api(`api/private/buyer/cart/add?id=${productId}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });
    } catch (error) {
      throwAsyncError(error);
    } finally {
      setIsLoading(false);
      updateNavbar();
    }
  };

  return (
    <div
      id={product.id}
      key={product.id}
      className="col-md-3 col-sm-6 col-xs-8 col-12 mb-4"
    >
      <style>
        {`
        .card-items {
          display: flex;
          align-content: flex-end;
          flex-wrap: wrap;
          justify-content: center;
          flex-direction: column;
        }
        .card-items  h5 {
          width: 100%;
          text-align: center;
        }
        .card-items  p {
          width: 100%;
          text-align: center;
        }
        `}
      </style>
      <div className="card text-center h-100" style={{ height: "300px", overflow: "hidden" }}>
        <img
          className="card-img-top"
          src={product.image_filename} // Assuming image_filename is the URL of the image
          alt={product.name}
          style={{ width: "auto", maxHeight: "100%" }}
        />
        <div className="card-body card-items h-100 m-1">
          <h5 className="card-title">{product.name || "Product Name"}</h5>
          <p className="card-text">
            {product.description.slice(0, 128) || "Product Description"}
          </p>
        </div>
        <div style={{ margin: "8px", display: "flex", justifyContent: "center" }}>
          <Tag types={product.tags.map(tag => tag.type)} texts={product.tags.map(tag => tag.title)}></Tag>
        </div>
        <div>
          <div>
            <ul className="list-group list-group-flush">
              <li className="list-group-item lead border-bottom border-top">
                $ {product.price || "0.00"}
              </li>
            </ul>
          </div>
          <div className="card-body">
            <Link to={"/product/" + product.id} className="btn btn-dark m-1">
              Buy Now
            </Link>
            {updateNavbar &&
              <button
                className="btn btn-dark m-1"
                onClick={(e) => {
                  e.preventDefault(); // Prevent any default behavior (like form submission)
                  addProduct(product.id);
                }}
              >
                Add to Cart
              </button>
            }
          </div>
        </div>
      </div>
    </div>
  );
};

ProductCard.propTypes = {
  product: PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string,
    description: PropTypes.string,
    price: PropTypes.number,
    image_filename: PropTypes.string,
  }).isRequired,
  addProduct: PropTypes.func.isRequired,
};

export default ProductCard;
