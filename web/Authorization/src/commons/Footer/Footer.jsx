import React from "react";
// import mailer from "../../services/mailer";
import "./Footer";
const Footer = () => {
  return (
    <>
      <footer class="bg-dark footer">
        <div class="footer-top py-8">
          <div class="container">
            <div class="row gy-5">
              <div class="col-lg-8 pe-xxl-10">
                <div class="row gy-5">
                  <div class="col-6 col-lg-4">
                    <h5 class="text-white footer-title-01">Need Help?</h5>
                    <ul class="list-unstyled footer-link-01 m-0">
                      <li>
                        <a class="text-white text-opacity-75" href="https://github.com/Tunaxx-New/awesome-ecommerce-project">
                          <i class="bi bi-github"></i> GitHub of project
                        </a>
                      </li>
                      <li>
                        <a class="text-white text-opacity-75" href="https://astanait.edu.kz/">
                          <i class="bi bi-building"></i> AITU University
                        </a>
                      </li>
                      <li>
                        <a class="text-white text-opacity-75" href="https://t.me/Mrowa223">
                          <i class="bi bi-filetype-jsx"></i> Front end developer
                        </a>
                      </li>
                      <li>
                        <a class="text-white text-opacity-75" href="https://t.me/tunaxxnew/">
                          <i class="bi bi-filetype-java"></i> Back end developer
                        </a>
                      </li>
                    </ul>
                  </div>
                  <div class="col-6 col-lg-4">
                    <h5 class="text-white footer-title-01">About</h5>
                    <ul class="list-unstyled footer-link-01 m-0">
                      <li>
                        <a class="text-white text-opacity-75" href="/info/loyalty">
                          <i class="bi bi-bag-heart-fill"></i> Loyalty index
                        </a>
                      </li>
                      <li>
                        <a class="text-white text-opacity-75" href="/info/transparent">
                          <i class="bi bi-transparency"></i> Transparent Policies
                        </a>
                      </li>
                      <li>
                        <a class="text-white text-opacity-75" href="/info/clv">
                          <i class="bi bi-bag-plus-fill"></i> {"Customer lifetime value (CLV)"}
                        </a>
                      </li>
                      <li>
                        <a class="text-white text-opacity-75" href="/info/badges">
                          <i class="bi bi-patch-question-fill"></i> Badges
                        </a>
                      </li>
                      <li>
                        <a class="text-white text-opacity-75" href="/info/retail">
                          <i class="bi bi-shop"></i> Kazakhstan online retail
                        </a>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="col-lg-4">
                {/*
                <h5 class="text-white footer-title-01 fs-5">
                  Subscribe to our newsletter and get 15% off your next order.
                </h5>
                <div>
                  <form class="d-flex flex-row mb-2 p-1 bg-white input-group">
                    <input
                      type="email"
                      class="form-control rounded border-0"
                      placeholder="Your Email"
                    />{" "}
                    <button
                      class="btn btn-secondary flex-shrink-0"
                      type="submit"
                    >
                      Subscribe
                    </button>
                  </form>
                  <p class="fs-sm text-white text-opacity-75">
                    I agree to receive newsletters
                  </p>
                </div>
                */}
              </div>
              </div>
            </div>
          </div>
          <div class="footer-bottom small py-3 border-top border-white border-opacity-10">
            {/*
          <div class="container">
            <div class="row">
              <div class="col-md-6 text-center text-md-start py-1">
                <p class="m-0 text-white text-opacity-75">
                  © 2024 copyright by{" "}
                  <a class="text-reset" href="#">
                    eco-shop
                  </a>
                </p>
              </div>
              <div class="col-md-6 text-center text-md-end py-1">
                <ul class="nav justify-content-center justify-content-md-end list-unstyled footer-link-01 m-0">
                  <li class="p-0 mx-3 ms-md-0 me-md-3">
                    <a href="#" class="text-white text-opacity-75">
                      Privace &amp; Policy
                    </a>
                  </li>
                  <li class="p-0 mx-3 ms-md-0 me-md-3">
                    <a href="#" class="text-white text-opacity-75">
                      Faq's
                    </a>
                  </li>
                  <li class="p-0 mx-3 ms-md-0 me-md-3">
                    <a href="#" class="text-white text-opacity-75">
                      Get a Quote
                    </a>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        */}
          </div>
      </footer>
    </>
  );
};

export default Footer;
