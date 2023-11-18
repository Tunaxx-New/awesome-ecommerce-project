import React from "react";
import { NavLink } from "react-router-dom";
import styles from "../assets/styles.js";
const Navbar = () => {
  console.log;
  return (
    <div>
      <div className={styles.navbar.navbar}>
        <div className={styles.navbar.navbar__list}>
          <div className={styles.navbar.navbar__list_item}>
            <NavLink
              to="/login"
              className={({ isActive }) =>
                isActive ? `${styles.navbar.active}` : `${styles.navbar.item}`
              }
            >
              Login Page
            </NavLink>
          </div>
          <div className={styles.navbar.navbar__list_item}>
            <NavLink
              to="/register"
              className={({ isActive }) =>
                isActive ? `${styles.navbar.active}` : `${styles.navbar.item}`
              }
            >
              Register Page
            </NavLink>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Navbar;
