import React from "react";
import {
  Navbar,
  Footer,
  // WishListPage,

} from "../commons";
import ProductList from "../components/ProductList";

const ProductListPage = () => {
  return (
    <>
      <Navbar />
      <ProductList></ProductList>
      <Footer />
    </>
  );
};

export default ProductListPage;
