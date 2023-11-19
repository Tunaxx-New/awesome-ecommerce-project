import React from "react";
import styles from "../assets/styles.js";

const LoginPage = () => {
  console.log(styles); // Logging styles to the console

  return (
    <div className={styles.login.cal}>
      <div className={styles.login.form}>
        <input
          className={styles.login.form__input}
          type="login"
          placeholder="login"
        />
        <input
          className={styles.login.form__input}
          type="password"
          placeholder="password"
        />
        <button className={styles.login.form__button}>Confirm</button>
      </div>
    </div>
  );
};

export default LoginPage;
