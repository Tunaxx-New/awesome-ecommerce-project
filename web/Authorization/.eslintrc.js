module.exports = {
  env: {
    browser: true,
    es2021: true,
    commonjs: true,
  },
  extends: ["eslint:recommended", "plugin:prettier/recommended"],
  overrides: [
    {
      env: {
        node: true,
      },
      files: [".eslintrc.{js,cjs,jsx}"],
      parserOptions: {
        sourceType: "script",
      },
    },
  ],
  parserOptions: {
    ecmaVersion: "latest",
    sourceType: "module",
  },
  plugins: ["unused-imports"],
  rules: {
    "no-unused-vars": "warn",
    "comma-dangle": "off", // eslint-disable-next-line prettier/prettier
    trailingComma: "off",
    "prettier/prettier": [
      "warn",
      {
        printWidth: 75,
        tabWidth: 2,
        arrowParens: "always",
        useTabs: false, // eslint-disable-next-line prettier/prettier
        endOfLine: "auto",
      },
    ],
    "unused-imports/no-unused-imports": "warn",
    "unused-imports/no-unused-vars": [
      "warn",
      {
        vars: "all",
        varsIgnorePattern: "^_",
        args: "after-used",
        argsIgnorePattern: "^_",
      },
    ],
  },
};
