import React from 'react';

const RetailInfo = () => {
    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8 text-center">
                    <h1 className="mb-4">Kazakhstan online retail</h1>
                    <p className="lead mb-5">
                        {`
                            Each user needs to sort products according to some true and significant metric.
                        It is also important for sellers to be above other sellers in the search and content
                        of the online marketplace. Based on loyalty data, this thesis project will provide
                        a metric that aggregates all of a user’s loyalty actions over time.
                            This project includes in the calculation of the loyalty index: the mean square
                        between the dates of purchases in days starting from the first purchase with an
                        upper limit (this metric displays long intervals between purchases, which distinguishes an honest user from the seller’s machinations to get more fake sales),
                        the percentage of distinctive sellers who have an order was purchased (The more
                        different sellers the buyer experiences, the more loyal to the marketplace he becomes).
                        `}

                    </p>
                    <div className="row">
                        <div className="col">
                            <h5 className="mt-3">Kazakhstan online retail</h5>
                            <img src={`${process.env.PUBLIC_URL}/info/info_retail.png`} alt="Retail" className="img-fluid" />
                            <p>
                                {`
                                S. Y. Barykin et al., “Development of the kazakhstan digital retail chains with-
                                in the eaeu e-commerce market,” Academy of Strategic Management Journal,
                                vol. 20, no. Special Issue 2, 2021.
                                `}
                            </p>
                        </div>
                    </div>
                    <div className="row">
                        <div className="col">
                            <h5 className="mt-3">Multicrop agricultural products</h5>
                            <img src={`${process.env.PUBLIC_URL}/info/info_products.jpg`} alt="Retail" className="img-fluid" />
                            <p>
                                {`
                                 Shiv_D24Coder, “multicrop-agricultural-products-dataset?select=oil-
                                 production-by-country-2023.csv,”
                                `}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RetailInfo;
