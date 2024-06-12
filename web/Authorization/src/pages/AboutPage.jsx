import React from "react";
import { Footer, Navbar } from "../commons";
import { Link } from "react-router-dom";
const AboutPage = () => {
  return (
    <>
      <Navbar />
      <div className="container my-3 py-3">
        <h1 className="text-center">About Us</h1>
        <hr />
        <p className="lead text-center">
          At Astana IT University, we are committed to leveraging technology for
          sustainable development.
          <br />
          Our Microgreens Online Store project creates an innovative marketplace
          dedicated to environmentally friendly products, connecting
          eco-conscious consumers with ethical producers. This community-driven
          platform supports local farmers and entrepreneurs, promoting
          sustainable agricultural practices. We prioritize transparency,
          fairness, and environmental responsibility to encourage informed and
          responsible consumer choices. Through this initiative, we aim to
          contribute to the global movement towards sustainability and ethical
          commerce.
        </p>

        <h2 className="text-center py-4">Our Products</h2>
        <div className="row">
          <div className="col-md-3 col-sm-6 mb-3 px-3">
            <Link to="/">
              <div className="card h-100">
                <img
                  className="card-img-top img-fluid"
                  src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEB4cmvbXh6JPXZnbAawxKnYhtfxQX57u6DA&s"
                  alt=""
                  height={160}
                />

                <div className="card-body">
                  <h5 className="card-title text-center">Home</h5>
                </div>
              </div>
            </Link>
          </div>
          <div className="col-md-3 col-sm-6 mb-3 px-3">
            <Link to="/profile">
              <div className="card h-100">
                <img
                  className="card-img-top img-fluid"
                  src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEB4cmvbXh6JPXZnbAawxKnYhtfxQX57u6DA&s"
                  alt=""
                  height={160}
                />
                <div className="card-body">
                  <h5 className="card-title text-center">Profile</h5>
                </div>
              </div>
            </Link>
          </div>

          <div className="col-md-3 col-sm-6 mb-3 px-3">
            <Link to="/product-list">
              <div className="card h-100">
                <img
                  className="card-img-top img-fluid"
                  src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEB4cmvbXh6JPXZnbAawxKnYhtfxQX57u6DA&s"
                  alt=""
                  height={160}
                />
                <div className="card-body">
                  <h5 className="card-title text-center">Product List</h5>
                </div>
              </div>
            </Link>
          </div>
          <div className="col-md-3 col-sm-6 mb-3 px-3">
            <Link to="/orders-list">
              <div className="card h-100">
                <img
                  className="card-img-top img-fluid"
                  src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEB4cmvbXh6JPXZnbAawxKnYhtfxQX57u6DA&s"
                  alt=""
                  height={160}
                />
                <div className="card-body">
                  <h5 className="card-title text-center">Orders</h5>
                </div>
              </div>
            </Link>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default AboutPage;
