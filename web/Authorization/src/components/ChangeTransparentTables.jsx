import React, { useState, useEffect } from 'react';
import api from "../apis/api";
import { useAsyncError } from '../commons';
import ReactDOM from 'react-dom';

const ChangeTransparentTables = ({ transparentCurrent, isLoading }) => {
    const [checkboxes, setCheckboxes] = useState([]);
    const [loading, setLoading] = useState(false);
    const throwAsyncError = useAsyncError();
    const [showAlert, setShowAlert] = useState(false);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const data = await api('/api/public/metadata', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                }
            });
            if (data) {
                data.transparentPolicies.forEach((policy) => {
                    transparentCurrent.forEach((currentPolicy) => {
                        if (currentPolicy.id == policy.id)
                            policy.checked = true;
                    });
                });
                setCheckboxes(data.transparentPolicies);
            }
        } catch (error) {
            throwAsyncError(error);
        }
    };

    const submitChanges = async () => {
        setShowAlert(false);
        isLoading(true);
        const policies = [];
        checkboxes.forEach(checkbox => {
            if (checkbox.checked)
                policies.push({ "id": checkbox.id });
        });
        console.log(policies);
        try {
            const data = await api('/api/private/profile/changeTransparentPolicies', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(policies)
            });
        } catch (error) {
            throwAsyncError(error);
        }
        isLoading(false);
    }

    const handleCheckboxChange = async (checkboxId, checked) => {
        try {
            const updatedCheckboxes = checkboxes.map(checkbox =>
                checkbox.id === checkboxId ? { ...checkbox, checked } : checkbox
            );
            setCheckboxes(updatedCheckboxes);
        } catch (error) {
            console.error('Error updating checkbox:', error);
            setLoading(false);
        }
    };
    const styles = {
        alertBox: {
            border: '1px solid red',
            padding: '10px',
            borderRadius: '5px',
            marginTop: '10px',
            backgroundColor: '#fdd'
        }
    }
    return (
        <div>
            <style>
                {`
                .checkbox-container {
                    display: flex;
                    align-items: center;
                  }
                  
                  .checkbox-item {
                    display: flex;
                    align-items: center;
                  }
                  
                  input[type="checkbox"] {
                    appearance: none;
                    width: 20px;
                    height: 20px;
                    border: 2px solid #333;
                    border-radius: 4px;
                    margin-right: 8px;
                    cursor: pointer;
                  }
                  
                  input[type="checkbox"]:checked {
                    background-color: #007bff;
                    border-color: #007bff;
                  }
                  
                  input[type="checkbox"] + label {
                    cursor: pointer;
                  }
                `}
            </style>
            <div>
                {loading && <p>Loading...</p>}
                <div>
                    <h3>Transparent policies</h3>
                </div>
                <form>
                    {!loading && checkboxes.map(checkbox => (
                        <div key={checkbox.id} className="checkbox-container">
                            <input
                                className=''
                                type="checkbox"
                                id={checkbox.id}
                                checked={checkbox.checked}
                                onChange={e => handleCheckboxChange(checkbox.id, e.target.checked)}
                            />
                            <label htmlFor={checkbox.id} style={{ margin: "4px" }}>{checkbox.name}</label>
                        </div>
                    ))}
                    <button
                        className="btn btn-style-1 btn-primary"
                        type="button"
                        onClick={() => setShowAlert(true)}
                    >
                        Update Transparent Policies
                    </button>
                </form>
            </div>
            {showAlert && (
                <div style={styles.alertBox}>
                    <h6>You will <span className='text-danger'>lost</span> all commission deacreasing by submitting this button!</h6>
                    <p>That action will override all your transparent policies and lost creation time. Are you sure you want to submit these Transparent policies?</p>
                    <button onClick={submitChanges} style={styles.button}>Confirm</button>
                    <button onClick={() => setShowAlert(false)} style={styles.button}>Cancel</button>
                </div>
            )}
        </div>
    );
};

export default ChangeTransparentTables;
