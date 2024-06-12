import React, { useState, useEffect } from "react";
import { useDispatch } from "react-redux";
import { addCart } from "../redux/action";
import { useAsyncError } from "../commons";

import Skeleton from "react-loading-skeleton";
import { Link } from "react-router-dom";
import { Navbar, Footer } from "../commons";
import "../apis/api";
import api from "../apis/api";
import ProductCard from "./Product/ProductCard";

const Products = ({ updateNavbar }) => {
  const [data, setData] = useState([]);
  const [metaData, setMetaData] = useState({});

  const [filter, setFilter] = useState([]);
  const [filterTag, setFilterTag] = useState([]);

  const [loading, setLoading] = useState(false);
  let componentMounted = true;

  const [pageCount, setPageCount] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [currentPage, setCurrentPage] = useState(0);
  const [sortKey, setSortKey] = useState("");
  const [sortOrder, setSortOrder] = useState("asc");

  const dispatch = useDispatch();
  const throwAsyncError = useAsyncError();

  useEffect(() => {
    const init = async () => {
      setLoading(true);
      try {
        getProducts("/api/public/product/list?page=0&size=1");
        const response = await api("/api/public/metadata", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        });
        if (response) {
          setMetaData(response);
        }
      } catch (error) {
        console.error("Error fetching products:", error);
        throwAsyncError(error);
      }
      setLoading(false);
    };
    init();
  }, []);

  useEffect(() => {
    const get = async () => {
      const pageableString = `page=${currentPage}&size=${pageSize}&sort=${sortKey},${sortOrder}`;
      if (filter.length == 0 && filterTag.length == 0) {
        const response = await getProducts(
          `/api/public/product/list?${pageableString}`
        );
        setData(response.content);
        setPageCount(response.totalPages);
      } else {
        const productsByTags =
          filterTag.length != 0
            ? await getProducts(
                `/api/public/product/listByTag?${pageableString}&tagsString=${filterTag.join(",")}`
              )
            : { content: [], totalElements: 0 };
        const productsByCategories =
          filter.length != 0
            ? await getProducts(
                `/api/public/product/listByCategory?${pageableString}&categoriesString=${filter.join(",")}`
              )
            : { content: [], totalElements: 0 };

        const productMap = new Map();
        const mergedProducts = [
          ...productsByTags.content,
          ...productsByCategories.content,
        ];
        let sameCount = 0;
        mergedProducts.forEach((product) => {
          if (!productMap.has(product.id)) {
            productMap.set(product.id, product);
          } else {
            sameCount++;
          }
        });
        const uniqueMergedProducts = Array.from(productMap.values());

        setData(uniqueMergedProducts);
        setPageCount(
          productsByTags.totalPages +
            productsByCategories.totalPages -
            sameCount
        );
      }
    };
    setLoading(true);
    get();
    setLoading(false);
  }, [filter, filterTag, currentPage, sortKey, sortOrder]);

  const getProducts = async (endpoint) => {
    try {
      const response = await api(endpoint, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      return response;
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  };

  useEffect(() => {}, [loading]);

  const Loading = () => (
    <>
      <div className="col-12 py-5 text-center">
        <Skeleton height={40} width={560} />
      </div>
      {[...Array(6)].map((_, index) => (
        <div className="col-md-4 col-sm-6 col-xs-8 col-12 mb-4" key={index}>
          <Skeleton height={592} />
        </div>
      ))}
    </>
  );

  const getButtonClass = (cat, filter_, color) => {
    return filter_.includes(cat)
      ? `btn btn-${color} btn-sm m-2`
      : `btn btn-outline-${color} btn-sm m-2`;
  };

  const filterProduct = (cat, add, setFilter_, filter_) => {
    const set = new Set(filter_);
    if (add) {
      set.add(cat);
    } else {
      set.delete(cat);
    }
    setFilter_(Array.from(set));
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const renderPaginationButtons = () => {
    const buttons = [];
    for (let i = 0; i < pageCount; i++) {
      buttons.push(
        <button
          key={i}
          onClick={() => handlePageChange(i)}
          className={`btn btn-outline-dark btn-sm m-2 ${currentPage === i ? "active" : ""}`}
        >
          {i + 1}
        </button>
      );
    }
    return buttons;
  };

  const ShowProducts = () => (
    <>
      <div className="buttons text-center py-5">
        <div>
          {metaData.categories &&
            metaData.categories.map((category) => (
              <button
                className={getButtonClass(category.name, filter, "dark")}
                onClick={() =>
                  filterProduct(
                    category.name,
                    !filter.includes(category.name),
                    setFilter,
                    filter
                  )
                }
              >
                {category.name}
              </button>
            ))}
        </div>
        <div>
          {metaData.tags &&
            metaData.tags.map((tag) => (
              <button
                className={getButtonClass(tag.title, filterTag, "primary")}
                onClick={() =>
                  filterProduct(
                    tag.title,
                    !filterTag.includes(tag.title),
                    setFilterTag,
                    filterTag
                  )
                }
              >
                {tag.title}
              </button>
            ))}
        </div>

        <div className="pagination-controls">
          <button
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 0}
            className="btn btn-outline-dark btn-sm m-2"
          >
            Previous
          </button>
          {renderPaginationButtons()}
          <button
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === pageCount - 1}
            className="btn btn-outline-dark btn-sm m-2"
          >
            Next
          </button>
        </div>

        <div
          className="sort-controls m-2"
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
          }}
        >
          <div>
            <button className="btn btn-outline-dark m-2">
              <i class="bi bi-arrow-clockwise"></i>
            </button>
            <label className="col-form-label-sm">Page size</label>
            <input
              type="number"
              value={pageSize}
              onChange={(e) => setPageSize(e.target.value)}
              placeholder="Go to page"
              min="1"
              max={pageCount}
              className="btn btn-outline-dark btn-sm m-2 form-control form-control-sm"
              style={{ width: "auto", padding: 0, paddingRight: "8px" }}
            />
          </div>
          <div
            className="sort-controls m-2"
            style={{ display: "flex", alignItems: "center" }}
          >
            <select
              className="form-select"
              onChange={(e) => setSortKey(e.target.value)}
              value={sortKey}
              style={{ width: "200px" }}
            >
              <option value="">Select Sort Key</option>
              <option value="createdTime">Creation time</option>
              <option value="expirationDate">Expiration date</option>
              <option value="name">Name</option>
              <option value="price">Price</option>
              <option value="seller.registeredTime">
                Seller registered time
              </option>
              {/* Add other keys as needed */}
            </select>
            <select
              className="form-select m-2"
              onChange={(e) => setSortOrder(e.target.value)}
              value={sortOrder}
              style={{ width: "150px" }}
            >
              <option value="asc">Ascending</option>
              <option value="desc">Descending</option>
            </select>
          </div>
        </div>
      </div>

      {data &&
        data.map((product) => (
          <ProductCard
            product={product}
            setIsLoading={setLoading}
            updateNavbar={updateNavbar}
          ></ProductCard>
        ))}
    </>
  );

  return (
    <>
      <div className="container my-3 py-3">
        <div className="row">
          <div className="col-12">
            <h2 className="display-5 text-center">Products</h2>
            <hr />
          </div>
        </div>
        {loading && (
          <div className="loading-back">
            <div className="loading-indicator">
              <div className="loading-circle"></div>
              <p>Processing...</p>
            </div>
          </div>
        )}
        <div className="row justify-content-center">
          {loading ? <Loading /> : <ShowProducts />}
        </div>
      </div>
      <Footer />
    </>
  );
};

export default Products;
