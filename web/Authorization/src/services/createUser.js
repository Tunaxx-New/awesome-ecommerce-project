import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

// Define a service using a base URL and expected endpoints

export const cardsApi = createApi({
  reducerPath: "cardsApi",
  baseQuery: fetchBaseQuery({ baseUrl: "http://localhost:8080" }),
  endpoints: (builder) => ({
    getCards: builder.query({
      query: () => ({
        url: "public/hello-world!",
        method: "GET",
      }),
    }),
  }),
});

export const { useGetCardsQuery } = cardsApi;
