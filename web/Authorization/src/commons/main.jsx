import { React, useState, useEffect } from "react";
import ModalWindow from "./ModalWindow/ModalWindow";
import { Link } from "react-router-dom";
const Home = () => {
  const [isModalOpen, setModalOpen] = useState(false);
  useEffect(() => {
    openModal(); // Открыть модальное окно при загрузке страницы
  }, []);
  const openModal = () => {
    setModalOpen(true);
  };

  const closeModal = () => {
    setModalOpen(false);
  };

  return (
    <>
      {/* <button onClick={openModal}>Open Modal</button> */}
      {/* <h1>React Modal Example</h1> */}
      {/* <button onClick={openModal}>Open Modal</button> */}
{/* Работает */}
      {/* <ModalWindow isOpen={isModalOpen} onClose={closeModal} centered>
        <div className="modal-content">
          <div className="modal-header d-flex justify-content-between">
            <h5 className="modal-title">Choose your role:</h5>
            <button
              type="button"
              className="btn-close"
              onClick={closeModal}
            ></button>
          </div>
          <div className="modal-body d-flex justify-content-center gap-5">
            <Link to="/private/registerSeller" className="btn btn-primary mb-2">
              Seller
            </Link>
            <Link to="/private/profile" className="btn btn-primary mb-2">
              User
            </Link>
          </div>
        </div>
      </ModalWindow> */}

      <div className="hero border-1 pb-3 mt-2">
        <div className="card bg-dark text-white border-0 mx-3">
          <img
            className="card-img img-fluid"
            src="./assets/main.jpg"
            alt="Card"
            style={{ height: "400px" }}
          />
          <div className="card-img-overlay d-flex align-items-center">
            <div className="container">
              <h5 className="card-title fs-1 text fw-lighter">
                New Season Arrivals
              </h5>
              <p className="card-text fs-5 d-none d-sm-block ">
                This is a wider card with supporting text below as a natural
                lead-in to additional content. This content is a little bit
                longer.
              </p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default Home;
