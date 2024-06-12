import React, { useEffect, useState } from 'react'
import { NavLink } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { UserCard } from '../../components';
import api from '../../apis/api';
import { useAsyncError } from '../../commons';

import Cookies from "universal-cookie";
import { RegisterSeller } from "../../components";

const Navbar = ({ navUpdateTrigger }) => {
    const throwAsyncError = useAsyncError();
    const [formData, setFormData] = useState(undefined);
    const [orders, setOrders] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true);
        const fetchData = async () => {
            try {
                return await api('/api/private/profile', {
                    method: 'POST',
                    withCredentials: true,
                    headers: {
                        'Content-Type': 'application/json',
                    },
                });
            } catch (error) {
                console.log(formData === undefined || formData.id === undefined, "Asd");
                return undefined;
            }
        }
        fetchData().then((data) => {
            if (data) {
                setFormData(data.authentication);
                setOrders(data.orders);
            }
            setIsLoading(false);
        });
    }, [navUpdateTrigger])

    const logout = () => {
        const cookies = new Cookies();
        cookies.remove('Authorization');
        window.location.reload();
    };

    const state = useSelector(state => state.handleCart)
    const isLogged = !(formData === undefined || formData.id === undefined);
    return (
        <div className="bg-light">
            <div style={{height:"80px"}}></div>
            {isLoading && (
                <div className="loading-back">
                    <div className="loading-indicator">
                        <div className="loading-circle"></div>
                        <p>Processing...</p>
                    </div>
                </div>
            )}
            <div className="bg-light fixed-top">
                <nav className="navbar navbar-expand-lg navbar-light">
                    <div className="container">
                        <a class="navbar-brand" href="#">
                            <img src={`${process.env.PUBLIC_URL}/ecogreen.png`} width="30" height="30" class="d-inline-block align-top" alt=""/>
                        </a>
                        <NavLink className="navbar-brand fw-bold fs-4 px-2" to="/"> EcoGreen</NavLink>
                        <button className="navbar-toggler mx-2" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                            <span className="navbar-toggler-icon"></span>
                        </button>

                        <div className="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul className="navbar-nav m-auto my-2 text-center">
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/">Home </NavLink>
                                </li>
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/products">Products</NavLink>
                                </li>
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/about">About</NavLink>
                                </li>
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/contact">Contact</NavLink>
                                </li>
                                <li className="nav-item">
                                    <NavLink className="nav-link" to="/profile">Profile</NavLink>
                                </li>
                            </ul>
                            <div className="buttons text-center" style={{ display: "flex" }}>
                                {formData && formData.seller == null && <RegisterSeller></RegisterSeller>}
                                {formData && formData.seller && <NavLink to="/create-product/null" className="btn btn-outline-dark m-2"><i class="bi bi-box-seam-fill mr-1"></i> Create Product</NavLink>}
                                {!isLogged ? (
                                    <>
                                        <NavLink to="/login" className="btn btn-outline-dark m-2"><i className="fa fa-sign-in-alt mr-1"></i> Login</NavLink>
                                        <NavLink to="/register" className="btn btn-outline-dark m-2"><i className="fa fa-user-plus mr-1"></i> Register</NavLink>
                                    </>
                                ) : (
                                    <>
                                        <NavLink to="/cart" className="btn btn-outline-dark m-2"><i className="fa fa-cart-shopping mr-1"></i> Cart ({formData.buyer.cart.cartItems.length}) </NavLink>
                                        <button onClick={logout} className="btn btn-outline-dark m-2">Logout</button>
                                    </>
                                )}
                            </div>
                        </div>
                    </div>
                </nav>

            </div >
            {isLogged && (
                <div class="container d-flex flex-wrap justify-content-sm-start d-flex justify-content-md-center stick-top">
                    {formData.roles.some(role => role.name === "USER") &&
                        <UserCard imageSource={`${process.env.PUBLIC_URL}/avatars/images/ava_${((formData.id + 20) % 25 + 1).toString().padStart(2, '0')}.gif`} upperText={formData.username ? formData.username : "Unknown"} lowerText="User" link={`/profile?userId=${formData.id}&type=user`}></UserCard>
                    }
                    {formData.buyer &&
                        <UserCard imageSource={`${process.env.PUBLIC_URL}/avatars/images/ava_${(formData.buyer.id % 25 + 1).toString().padStart(2, '0')}.gif`} upperText={formData.buyer.name ? formData.buyer.name : "Unknown"} lowerText="Buyer" link={`/profile?userId=${formData.id}&type=buyer`}></UserCard>
                    }
                    {formData.seller &&
                        <UserCard imageSource={`${process.env.PUBLIC_URL}/avatars/images/ava_${((formData.seller.id + 10) % 25 + 1).toString().padStart(2, '0')}.gif`} upperText={formData.seller.name ? formData.seller.name : "Unknown"} lowerText="Seller" link={`/profile?userId=${formData.id}&type=seller`}></UserCard>
                    }
                    {formData.roles.some(role => role.name === "ADMIN") &&
                        <UserCard imageSource={`${process.env.PUBLIC_URL}/avatars/images/ava_admin.jpg`} upperText={`${formData.username ? formData.username : "Unknown"} ${formData.id}`} lowerText="Admin" link={`/profile?userId=${formData.id}&type=admin`}></UserCard>
                    }
                </div>
            )}
        </div>
    )
}

export default Navbar