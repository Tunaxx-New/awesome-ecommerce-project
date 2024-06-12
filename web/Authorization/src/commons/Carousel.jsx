// CarouselComponent.js

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ProductCard, Tag } from '../components';

const CarouselComponent = ({ items, itemsPerPage = 3, title }) => {
    const [currentSlide, setCurrentSlide] = useState(0);
    const [animating, setAnimating] = useState(false);

    const nextSlide = () => {
        //if (animating) return;
        //setAnimating(true);
        if (isNaN(currentSlide))
            setCurrentSlide(0);
        setCurrentSlide((prev) => (prev + 1) % Math.ceil(items.length / itemsPerPage));
    };

    const prevSlide = () => {
        //if (animating) return;
        //setAnimating(true);
        if (isNaN(currentSlide))
            setCurrentSlide(0);
        setCurrentSlide((prev) => (prev - 1 + Math.ceil(items.length / itemsPerPage)) % Math.ceil(items.length / itemsPerPage));
    };


    return (
        <div>
            <h3 style={{textAlign:"center"}}>{title}</h3>
            <style>
                {`
                /* CarouselComponent.css */

                .carousel-container {
                    padding: 16px;
                    position: relative;
                    width: 100%;
                    max-width: 1000px;
                    margin: auto;
                    transition: transform 0.1s ease-in-out;
                    overflow: hidden;
                }
                
                .carousel-wrapper {
                    display: flex;
                    transition: transform 0.1s ease-in-out;
                    width: 100%;
                }
                
                .carousel-item {
                    flex: 0 0 auto;
                    width: 100%;
                    transition: transform 1s ease-in-out;
                }
                
                .carousel-item:not(.active) {
                    opacity: 0;
                    pointer-events: none;
                }
                
                .carousel-control {
                    position: absolute;
                    top: 50%;
                    transform: translateY(-50%);
                    background-color: rgba(0, 0, 0, 0.5);
                    border: none;
                    color: white;
                    font-size: 2rem;
                    cursor: pointer;
                    z-index: 1;
                }
                
                .carousel-control.prev {
                    left: 10px;
                }
                
                .carousel-control.next {
                    right: 10px;
                }
                `}
            </style>

            <div className="carousel-container">
               
                <div className="carousel-wrapper">
                    <div className='carousel-container' style={{
                        transform: `translateX(-${currentSlide * 100}%)`,
                        display:"flex",
                        overflow:"hidden"
                    }}>
                        {items.map((product, index) => (
                            <ProductCard product={product}></ProductCard>
                        ))}
                    </div>
                </div>
               
            </div>
        </div >
    );
};

export default CarouselComponent;
