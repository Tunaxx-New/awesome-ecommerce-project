import React from "react";
import styles from "../assets/styles.js";
console.log(styles);
const RegisterPage = (props) => {
  return (
    <div className={styles.register.cal}>
      <div className={styles.register.form}>
        <input
          className={styles.register.form__input}
          type="login"
          placeholder="login"
        />
        <input
          className={styles.register.form__input}
          type="email"
          placeholder="email"
        />
        <input
          className={styles.register.form__input}
          type="password"
          placeholder="password"
        />

        <button className={styles.register.form__button}>Confirm</button>
      </div>
    </div>
  );
};

export default RegisterPage;
