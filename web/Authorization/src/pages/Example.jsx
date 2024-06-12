import React, { useState, useEffect } from "react";
import SwaggerUI from "swagger-ui-react";
import "swagger-ui-react/swagger-ui.css";

const Example = () => {
  const [swaggerData, setSwaggerData] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetch(
          "http://localhost:3000/public/hello-world!"
        );
        if (!response.ok) {
          throw new Error("Failed to fetch Swagger documentation");
        }
        console.log(response)
        // setSwaggerData(data);
      } catch (error) {
        setError(error);
        setLoading(false);
        return;
      }
   
    };

    fetchData();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div>Error: {error.message}</div>;
  }

  return (
    <div>
      
    </div>
  );
};

export default Example;
