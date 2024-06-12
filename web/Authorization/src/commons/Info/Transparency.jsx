import React from 'react';

const Transparent = () => {
    const badges = [
        {
            id: 1,
            created_time: '25.04.2024',
            description: 'AVAILABLE_PRODUCT_ORDERS',
            title: '25%'
        },
        {
            id: 2,
            created_time: '25.04.2024',
            description: 'AVAILABLE_PRODUCT_ORDERS_DATE',
            title: '5%'
        },
        {
            id: 3,
            created_time: '25.04.2024',
            description: 'AVAILABLE_PRODUCT_ORDERS_PRICE',
            title: '5%'
        },
        {
            id: 4,
            created_time: '25.04.2024',
            description: 'AVAILABLE_PRODUCT_ORDERS_BUYER',
            title: '10%'
        }
    ];
    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8 text-center">
                    <h1 className="mb-4">Transparent Policies</h1>
                    <p className="lead mb-5">
                        {`
                            The ability to include multiple transparent contracts will be available to buyers
                            and sellers. Based on these contracts, the server database and server framework
                            will only produce and display user data that has been agreed upon. The transparency policy option will only grant access to particular information with the
                            simultaneous consent of the buyer and seller. Transparency policies are the same
                            for both parties. The more users see transparent data, the more it builds trust
                            in the buyer and the platform. Which accordingly increases the loyalty of the
                            buyer and the marketplace. Each transparency policy will have its own weight.
                            The following transparency flags were used in this project. These flags set the
                            visibility of the fields corresponding to the name to users of the entire site.
                        `}

                    </p>
                    <div className="row">
                        <div className="col">
                            <h5 className="mt-3">Badges table</h5>
                            <p>
                                {`
                                Transparency policies are put into place to make sure that all parties have easy
                                access to information about transactions, which boosts user confidence in the
                                platform. This section explores the idea of transparency policies in more detail,
                                as well as the importance and application of these policies in our project.
                                Adopting transparency policies benefits sellers and buyers in various ways and
                                makes the marketplace more dependable and trustworthy.
                                The transparency system increases user loyalty. The transparency policy allows you to directly see important information about sellers, their products, order availability, prices and customer reviews. This solution helps to increase the project’s loyalty index. Buyers feel much more informed in their shopping experience with such a system. Also, increasing loyalty is a significant advantage in
                                the interaction between buyer - platform - seller.
                                The project team took into account the article described above, and accepts
                                the importance of such a value as loyalty. Buyers are more likely to return to
                                a platform where their interests are protected, and the same goes for sellers.
                                Confidence and success in a product transaction depends on the operation of such
                                a platform agreement system, the buyer is now clearly aware of his purchase,
                                which enhances the user experience.
                                The seller, in their interest to sell, provides access to transparency on the
                                product page and potentially increases their reputation. Order records and customer evaluations create a greater informative user experience, attracting extra
                                customers to your services. The person has all of the tools to expose the reliability
                                of the transaction and the pleasant in their products. This characteristic creates
                                differentiation and opposition among sellers.
                                Finally, transparency policies help resolve disputes fairly. With a complete
                                record of transactions, disputes have greater evidentiary value. This way they
                                can be resolved effectively. This clarity helps mitigate conflicts and ensure that
                                decisions are based on evidence, thereby maintaining integrity. platforms.
                                The benefits to buyers and sellers of including these policies are described in
                                the next section about the loyalty index
                                `}
                            </p>
                            <div className="container mt-5">
                                <h2 className="mb-4 text-center">TP List</h2>
                                <table className="table table-striped">
                                    <thead className="thead-dark">
                                        <tr>
                                            <th scope="col">ID</th>
                                            <th scope="col">Created Time</th>
                                            <th scope="col">Title</th>
                                            <th scope="col">-Percentage of commission</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {badges.map(badge => (
                                            <tr key={badge.id}>
                                                <td>{badge.id}</td>
                                                <td>{badge.created_time}</td>
                                                <td>{badge.description}</td>
                                                <td>{badge.title}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Transparent;
