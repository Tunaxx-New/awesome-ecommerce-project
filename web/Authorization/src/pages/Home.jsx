import { useEffect, useState } from "react";
import { Navbar, Main, Footer, Products, ModalWindow } from "../commons";
import { ProductCard } from "../components";
import Carousel from "../commons/Carousel";
import api from "../apis/api";
// import "./styles.css"
// import { Product } from "../components";
function Home() {

  const [navUpdateTrigger, setNavUpdateTrigger] = useState(0);

  useEffect(() => {
    getProducts();
    fetch('/api/public/hello-world!')
      .then(response => response.json())
      .then(data => console.log(data))
      .catch(error => console.error('Error:', error));
  }, [])

  const updateNavbar = () => {
    setNavUpdateTrigger((prev) => prev + 1); // Increment to change the state
  };

  const [productsRecent, setProductsRecent] = useState([]);
  const getProducts = async () => {
    try {
      const response = await api("/api/public/product/list?page=0&size=10&sort=createdTime,desc", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });
      setProductsRecent(response.content);
    } catch (error) {
      console.error("Error fetching products:", error);
    }
  };
  useEffect(() => {
    console.log(productsRecent);
  }, [productsRecent])

  return (
    <>
      <Navbar navUpdateTrigger={navUpdateTrigger} />
      <ModalWindow />
      <Main />
      <Carousel items={productsRecent} title={"Recent products"}></Carousel>
      <Products updateNavbar={updateNavbar} />
      {/* <Footer /> */}
    </>
  );
}

export default Home;
