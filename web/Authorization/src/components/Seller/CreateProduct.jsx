import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useNavigate } from "react-router-dom";
import api from "../../apis/api";
import { useAsyncError } from '../../commons';
// import Select from 'react-select'; // Import react-select


// Original class component
const CreateProduct = ({ params, product, isCreate = false, closeTab }) => {
    // Accessing params from props
    const { id: paramId } = useParams();
    const id = Number(paramId);
    const [formData, setFormData] = useState({
        id: id,
        name: '',
        price: '201',
        expirationDate: new Date().toISOString().split('T')[0] + ''
    });
    const [imagePreview, setImagePreview] = useState(null);
    const [metaData, setMetaData] = useState(null);

    const navigate = useNavigate();
    const throwAsyncError = useAsyncError();
    const [isLoading, setIsLoading] = useState(false);
    const [loadingText, setLoadingText] = useState('Processing...');

    const saveUpdate = async (product_, isCreate_) => {
        //if (isNaN(id))
        //    isCreate_ = true;
        if (isCreate_)
            delete product_.id;

        let isError = false;

        if (imagePreview && imagePreview.name) {
            product_.image_filename = imagePreview.name;

            try {
                const formData_ = new FormData();
                formData_.append('file', imagePreview);
                const response = await api('/api/private/images/upload', {
                    method: "POST",
                    body: formData_
                });
                if (response)
                    setFormData(response);
            } catch (error) {
                console.log(error);
                throwAsyncError(error);
                isError = true;
            }
        }

        try {
            const response = await api('/api/private/seller/product/add', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(product_)
            });
            if (response)
                setFormData(response);
        } catch (error) {
            console.log(error);
            throwAsyncError(error);
            isError = true;
        }
        if (!isError)
            if (isCreate_) {
                navigate("/");
            } else {
                closeTab();
            }
    }

    const getMetaData = async () => {
        try {
            const response = await api('/api/public/metadata', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            });
            if (response)
                setMetaData(response);
        } catch (error) {
            console.log(error);
        }
    }

    useEffect(() => {
        setIsLoading(true);
        if (product) {
            console.log(product, "PPP");
            let formData_ = { ...product };
            formData_.categories = product.categories.map(category => ({
                id: category.id,
                label: category.name,
                value: category.name
            }));
            formData_.tags = product.tags.map(tag => ({
                id: tag.id,
                label: tag.title,
                value: tag.title
            }));
            setFormData(formData_);
        }
        getMetaData();
        setIsLoading(false);
    }, [])

    function dateToFull(dateString) {
        const date = new Date(dateString);
        // Extract the date components
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-indexed, so add 1; padStart ensures two digits
        const day = String(date.getDate()).padStart(2, '0'); // padStart ensures two digits

        // Extract the time components
        const hours = String(date.getHours()).padStart(2, '0'); // padStart ensures two digits
        const minutes = String(date.getMinutes()).padStart(2, '0'); // padStart ensures two digits
        const seconds = String(date.getSeconds()).padStart(2, '0'); // padStart ensures two digits
        const milliseconds = String(date.getMilliseconds()).padStart(6, '0'); // padStart ensures three digits

        // Create the formatted date string
        const formattedDate = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${milliseconds}`;
        return formattedDate;
    }

    const handleChange = (e) => {

        const { name, value, type, checked } = e.target;

        // Split the name attribute value by dot
        const nameParts = name.split('.');

        // Check if there are multiple parts (nested property)
        if (nameParts.length > 1) {
            // Create a nested object structure
            const nestedObject = {
                [nameParts.slice(1).join('.')]: type === "checkbox" ? checked : type === "date" ? dateToFull(value) : value
            };

            // Check if buyer already exists in formData
            if (formData[`${name}`]) {
                // If buyer exists, update the nested property
                setFormData({
                    ...formData,
                    [name]: {
                        ...formData[`${name}`],
                        ...nestedObject
                    }
                });
            } else {
                // If buyer doesn't exist, create it as an object and set the nested property
                setFormData({
                    ...formData,
                    [name]: {
                        ...nestedObject
                    }
                });
            }
        } else {
            // If it's not a nested property, update directly
            setFormData({
                ...formData,
                [name]: type === "checkbox" ? checked : type === "date" ? dateToFull(value) : value
            });
        }
    };

    const handleChangeMultiSelectionCategory = selectedOptions => {
        // Update formData with the selected options
        setFormData({
            ...formData,
            categories: selectedOptions
        });
    };
    const handleChangeMultiSelectionTag = selectedOptions => {
        // Update formData with the selected options
        setFormData({
            ...formData,
            tags: selectedOptions
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        await saveUpdate(formData, isCreate);
    };

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setImagePreview(e.target.files[0]);
            setFormData({
                ...formData,
                imageName: file.name
            });
        }
    };

    return (
        <div className="container d-flex justify-content-center align-items-center vh-100">
            {isLoading && (
                <div className="loading-back">
                    <div className="loading-indicator">
                        <div className="loading-circle"></div>
                        <p>{loadingText}</p>
                    </div>
                </div>
            )}
            <div className="card p-4 shadow-lg" style={{ maxWidth: '800px', width: '100%' }}>
                <h2 className="card-title text-center mb-4">Create New Product</h2>
                {!isNaN(id) &&
                    <p className="text-left text-muted">Product ID: {id}</p>
                }
                <form onSubmit={handleSubmit}>
                    <div className="row">
                        <div className="col-md-4">
                            <div className="image-upload border rounded-3 mb-4 d-flex justify-content-center align-items-center"
                                style={{ height: '200px', cursor: 'pointer', backgroundColor: '#f8f9fa' }}
                                onClick={() => document.getElementById('imageInput').click()}>
                                {imagePreview ? (
                                    <img src={URL.createObjectURL(imagePreview)} alt="Product Preview" style={{ maxHeight: '100%', maxWidth: '100%' }} />
                                ) : (
                                    <span className="text-muted">Click to upload image</span>
                                )}
                            </div>
                            <input
                                type="file"
                                id="imageInput"
                                name="productImage"
                                accept="image/*"
                                className="form-control"
                                style={{ display: 'none' }}
                                onChange={handleImageChange}
                            />
                        </div>
                        <div className="col-md-8">
                            <div className="form-group mb-3">
                                <label htmlFor="productName" className="form-label">Product Name</label>
                                <input
                                    type="text"
                                    id="productName"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleChange}
                                    className="form-control"
                                    required
                                />
                            </div>
                            <div className="form-group mb-3">
                                <label htmlFor="productDescription" className="form-label">Product Description</label>
                                <textarea
                                    id="productDescription"
                                    name="description"
                                    value={formData.description}
                                    onChange={handleChange}
                                    className="form-control"
                                    rows="3"
                                    maxlength ="255"
                                    required
                                ></textarea>
                            </div>
                            <div className="form-group mb-3">
                                <label htmlFor="productDescription" className="form-label">Product Characteristics</label>
                                <textarea
                                    id="productCharacteristics"
                                    name="characteristics"
                                    value={formData.characteristics}
                                    onChange={handleChange}
                                    className="form-control"
                                    rows="3"
                                    maxlength ="255"
                                    required
                                ></textarea>
                            </div>
                            <div className="form-group mb-3">
                                <label htmlFor="product-expiration">Expiration date</label>
                                <label htmlFor="product-expiration" className="form-label">Expiration Date</label>
                                <input
                                    className="form-control beautiful-text" data-tooltip={formData.expirationDate}
                                    type="date"
                                    name="expirationDate"
                                    id="product-expiration"
                                    value={new Date(formData.expirationDate ? formData.expirationDate : new Date().getTime()).toISOString().split('T')[0]}
                                    onChange={handleChange}
                                    required=""
                                />
                            </div>
                            <div className="form-group mb-3">
                                <label htmlFor="productPrice" className="form-label">Product Price</label>
                                <div className='d-flex' style={{ alignItems: "center" }}>
                                    <input
                                        type="number"
                                        id="productPrice"
                                        name="price"
                                        value={formData.price}
                                        onChange={handleChange}
                                        className="form-control"
                                        required
                                    />
                                    <p style={{ marginBottom: 0, marginLeft: 8 }}>$</p>
                                </div>
                            </div>

                            <div className="form-group mb-3">
                                <label htmlFor="address" className="form-label">
                                    Categories
                                </label>
                                <Select
                                    name="select-input"
                                    isMulti
                                    className="basic-multi-select"
                                    id="address"
                                    options={metaData && metaData.categories && metaData.categories.map(category => ({
                                        id: category.id,
                                        label: category.name,
                                        value: category.name
                                    }))}
                                    value={formData.categories}
                                    onChange={handleChangeMultiSelectionCategory}
                                    classNamePrefix="select"
                                    required
                                >
                                </Select>
                                <div className="invalid-feedback">
                                    Please enter your categories.
                                </div>
                            </div>
                            <div className="form-group mb-3">
                                <label htmlFor="tags" className="form-label">
                                    Tags
                                </label>
                                <Select
                                    name="select-input"
                                    isMulti
                                    className="basic-multi-select"
                                    id="tags"
                                    options={metaData && metaData.tags && metaData.tags.map(tag => ({
                                        id: tag.id,
                                        label: tag.title,
                                        value: tag.title
                                    }))}
                                    value={formData.tags}
                                    onChange={handleChangeMultiSelectionTag}
                                    classNamePrefix="select"
                                    required
                                >
                                </Select>
                                <div className="invalid-feedback">
                                    Please enter your tags.
                                </div>
                            </div>

                        </div>
                    </div>
                    <button type="submit" className="btn btn-primary w-100">Create Product</button>
                </form>
            </div>
        </div>
    );
}

export default CreateProduct;