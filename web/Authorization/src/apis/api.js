import Cookies from "universal-cookie";

const api = async (route, request) => {
  const cookies = new Cookies();

  // Adding Authorization Bearer from cookies, if route is not public
  if (!route.includes("public")) {
    const token = cookies.get("Authorization");
    request.headers = {
      ...request.headers,
      Authorization: token,
    };
  }

  // Request processing
  const response = await fetch(route, request);

  // Response processing, saving token into cookies
  let responseJson = {};
  let responseText = "";
  try {
    if (response.headers.get('Content-Type').includes('application/json')) {
      responseJson = await response.json();
    } else {
      responseText = await response.text();
    }
  } catch (error) {
    () => {};
  }

  if (!response.ok || (responseJson && responseJson.error) || (response.status > 390 || response.status < 200)) {
    responseJson.text = responseText;
    throw new Error(
      `${response.status}~${response.statusText}~${JSON.stringify(responseJson)}`,
    );
  }

  if (responseJson.data && responseJson.data.token)
    cookies.set(
      "Authorization",
      `Bearer ${responseJson.data.token}`,
      { path: "/" },
    );

  return responseText == "" ? responseJson : responseText;
};

export default api;
