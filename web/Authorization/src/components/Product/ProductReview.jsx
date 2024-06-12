import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { useAsyncError } from "../../commons";
import api from "../../apis/api";
import Tag from '../Tag/Tag';


const ProductReview = ({ review }) => {
    const throwAsyncError = useAsyncError();
    const navigate = useNavigate();
    const { productId: paramProductId, orderId: paramOrderId } = useParams();
    const productId = Number(paramProductId);
    const orderId = Number(paramOrderId);
    const [formData, setFormData] = useState({})

    const [isLoading, setIsLoading] = useState(false);
    const [rating, setRating] = useState(0);

    const makeReview = async (e) => {
        e.preventDefault();
        formData.rating = rating;
        try {
            const response = await api(`/api/private/buyer/product/id/review?productId=${productId}&orderId=${orderId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(formData)
            });
        } catch (error) {
            throwAsyncError(error);
        } finally {
            setIsLoading(false);
            navigate("/orders-list");
        }
    };

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;

        setFormData({
            ...formData,
            [name]: type === "checkbox" ? checked : value
        });
    };

    useEffect(() => {
        if (review) {
            setFormData(review);
            setRating(review.rating);
        }
    })

    const stars = [1, 2, 3, 4, 5];

    // Handle star click to set rating
    const handleStarClick = (rate) => {
        if (!review)
            setRating(rate);
    };

    return (
        <div>
            <div className="container mt-5">
                {!review &&
                    <h2>Submit Your Review on product #{productId} in order #{orderId}</h2>
                }
                <form onSubmit={makeReview}>
                    <div className="form-group mb-3">
                        {!review &&
                            <label htmlFor="reviewText">Review:</label>
                        }
                        <textarea
                            id="reviewText"
                            className="form-control"
                            name="comment"
                            value={formData.comment}
                            onChange={handleChange}
                            rows="4"
                            placeholder="Write your review here"
                            disabled={review ? true : false}
                        />
                    </div>
                    <div className="form-group mb-3">
                        {!review &&
                            <label>Rating:</label>
                        }
                        <div className="star-rating d-flex gap-2">
                            {stars.map((star) => (
                                <span
                                    key={star}
                                    className={`star ${star <= rating ? 'text-warning' : 'text-secondary'}`}
                                    style={{ cursor: 'pointer', fontSize: '24px' }}
                                    onClick={() => handleStarClick(star)}
                                >
                                    &#9733;
                                </span>
                            ))}
                        </div>
                    </div>
                    {!review &&
                        <button type="submit" className="btn btn-primary">Submit Review</button>
                    }
                </form>
            </div>
        </div>
    );
};

export default ProductReview;
