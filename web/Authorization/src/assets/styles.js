import * as loginPageStyles from "./LoginPage.module.css";
import * as registerPageStyles from "./RegisterPage.module.css";
import * as NavBar from "./NavBar.module.css";

const combinedStyles = {
  login: loginPageStyles, // Access styles from LoginPage as styles.login
  register: registerPageStyles,
  navbar: NavBar, // Access styles from RegisterPage as styles.register
};

export default combinedStyles;
