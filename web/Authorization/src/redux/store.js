import { configureStore } from "@reduxjs/toolkit";
import rootReducers from "./reducer";
const store = configureStore({
  reducer: rootReducers,
});

export default store;

// import { configureStore, getDefaultMiddleware } from "@reduxjs/toolkit";
// import rootReducers from "./reducer";
// import { cardsApi } from "../services/createUser";
// const store = configureStore({
//   reducer: { rootReducers, [cardsApi.reducerPath]: cardsApi.reducer },
//   middleware: (getDefaultMiddleware)=> getDefaultMiddleware().concat(cardsApi.middleware)
// });

// export default store;
