function getNestedKeys(jsonObject, parentKey = "") {
  let keys = [];
  for (let key in jsonObject) {
    if (Object.prototype.hasOwnProperty.call(jsonObject, key)) {
      const nestedKey = parentKey ? `${parentKey}.${key}` : key;
      if (
        typeof jsonObject[key] === "object" &&
        !Array.isArray(jsonObject[key])
      ) {
        keys.push(nestedKey);
        keys = keys.concat(getNestedKeys(jsonObject[key], nestedKey));
      } else {
        keys.push(nestedKey);
      }
    }
  }
  return keys;
}

function getValueByNestedKey(object, nestedKey) {
  const keys = nestedKey.split(".");
  let value = object;
  for (const key of keys) {
    if (value && typeof value === "object" && key in value) {
      value = value[key];
    } else {
      return undefined; // Key not found or value is not an object
    }
  }

  return JSON.stringify(value) != "{}"
    ? JSON.stringify(value)
    : `${value}`;
}

export default { getNestedKeys, getValueByNestedKey };
