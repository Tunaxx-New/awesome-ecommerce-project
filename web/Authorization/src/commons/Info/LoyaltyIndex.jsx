import React from 'react';

const LoyaltyIndex = () => {
    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8 text-center">
                    <h1 className="mb-4">Loyalty Index</h1>
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
                        <div className="col-md-4 mb-4">
                            <h5 className="mt-3">Components of the Loyalty Index</h5>
                            <p>
                                The fidelity index is derived from a combination of more than one metric
                                provided Strong honest consumer evaluation. These metrics are carefully selected
                                for transparency the use of truth intervenes and prevents abusive behavior. The
                                main features add:
                            </p>
                        </div>
                    </div>
                    <div className="row">
                        <div>
                            <p>
                                {`
                                    Frequency of purchase: This metric calculates suggest square c program talk
                                    time Shopping dates, start with the person’s first purchase. by reading the
                                    time In the periods between successive purchases, the platform can identify
                                    patterns Consistent behavioral buying that is better than routine, sporadic or
                                    haphazard Characteristics. A smaller mean c programming language means
                                    higher fidelity, which means. The customer comes back regularly to shop.
                                    Vendor sorts: Another vital factor is the proportion of specificity Vendors
                                    from which the consumer has bought goods. This metric indicates the patron
                                    engagement throughout the platform that wants to be with the unmarried
                                    supplier. One is higher than that The percentage of service provider variety
                                    indicates that the consumer seeks out and buys from distinct traders, indicating consideration and emblem loyalty as an entire. This rating metric is
                                    essential to differentiate the proper customers Definitely for the interrupters
                                    Sales variety for specific supplier. One is higher than that The percentage
                                    of service provider variety indicates that the consumer seeks out and buys
                                    from distinct traders, indicating consideration and emblem loyalty as an entire. This rating metric is essential to differentiate the proper customers
                                    Definitely for the interrupters Sales variety for specific supplier.
                                `}
                            </p>
                        </div>
                    </div>
                    <div className="row">
                        <div>
                            <p>
                                {`
                                    The implementation of a Loyalty Index offers severa benefits that make contributions to the overall health and growth of the e-commerce platform. By the use
                                    of metrics which can be hard to control, along with the suggest square c program
                                    languageperiod between purchases and the range of sellers, the Loyalty Index efficaciously reduces manipulative practices. This creates a fairer marketplace in
                                    which real interactions are rewarded, improving the platform’s integrity.
                                    When consumers recognize that the platform makes use of a complicated metric to highlight sincere dealers, their self warranty in making purchases will growth.
                                    This accelerated agree with ends in better client retention fees and further commonplace transactions. The Loyalty Index also provides precious statistics-driven
                                    insights into client behavior, which may be used to refine the platform’s algorithms, advertising techniques, and client engagement initiatives.
                                    By fostering actual engagement and decreasing manipulative practices, the
                                    Loyalty Index contributes to the lengthy-term sustainability and boom of the
                                    market. A reliable client base and a recognition for fairness entice new users and
                                    keep present ones, using standard platform success. The aggregate of decreased fraudulent activities, prolonged customer self assurance, precious behavioral insights, and sustainable boom underscores the extensive benefits of implementing
                                    a Loyalty Index.
                                    
                                `}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoyaltyIndex;
