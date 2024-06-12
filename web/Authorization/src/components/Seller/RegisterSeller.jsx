import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import api from "../../apis/api";
import { useAsyncError } from '../../commons';

const RegisterSeller = () => {
    const [isLoading, setisLoading] = useState(false);
    const throwAsyncError = useAsyncError();
    const navigate = useNavigate();

    const submit = async () => {
        setisLoading(true);
        try {
            const data = await api('/api/private/registerSeller', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({})
            });
        } catch (error) {
            throwAsyncError(error);
        } finally {
            setisLoading(false);
            navigate("profile?type=seller");
        }
    }

    return (
        <div>
            {isLoading && (
                <div className="loading-back">
                    <div className="loading-indicator">
                        <div className="loading-circle"></div>
                        <p>Processing...</p>
                    </div>
                </div>
            )}
            <button className="btn btn-outline-dark m-2" onClick={submit}>Become seller!</button>
        </div>
    );
};

export default RegisterSeller;
