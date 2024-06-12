import React from 'react';

const Badges = () => {
    const badges = [
        {
            id: 1,
            created_time: '#ЗНАЧ!',
            description: 'You are successfully registered to GreenShop marketplace! asd',
            image_source: 'registeredBadge.png',
            title: 'Registered user'
        },
        {
            id: 2,
            created_time: '#ЗНАЧ!',
            description: 'You bought your first product! That badge decrease commission by 10% once',
            image_source: 'firstOrder.png',
            title: 'First order'
        },
        {
            id: 3,
            created_time: '#ЗНАЧ!',
            description: 'You placed your first product on GreenShop marketplace! That badge decrease commission by 25% once',
            image_source: 'firstProduct.png',
            title: 'First product'
        },
        {
            id: 4,
            created_time: '#ЗНАЧ!',
            description: 'You filled cart with products!',
            image_source: 'firstCartItem.png',
            title: 'First cart item'
        },
        {
            id: 5,
            created_time: '#ЗНАЧ!',
            description: 'You bought products on sum higher than 100$! Thank you for that expensive purchase. Each badge will decrease 25% per each commission calculation',
            image_source: 'largeWallet.png',
            title: 'Large wallet'
        },
        {
            id: 6,
            created_time: '#ЗНАЧ!',
            description: 'You bought products on sum less than 1$! Thank you for that precise purchase. Each badge will decrease 20% per each commission calculation',
            image_source: 'perciseChoice.png',
            title: 'Precise choice'
        },
        {
            id: 7,
            created_time: '#ЗНАЧ!',
            description: 'You bought product as first consumer!',
            image_source: 'firstOnProduct.png',
            title: 'First!'
        }
    ];

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-8 text-center">
                    <h1 className="mb-4">Badges</h1>
                    <p className="lead mb-5">
                        {`
                            There are badges that decrease commission for buyer
                        `}

                    </p>
                    <div className="row">
                        <div className="col">
                            <h5 className="mt-3">Badges table</h5>
                            <div className="container mt-5">
                                <h2 className="mb-4 text-center">Badge List</h2>
                                <table className="table table-striped">
                                    <thead className="thead-dark">
                                        <tr>
                                            <th scope="col">ID</th>
                                            <th scope="col">Created Time</th>
                                            <th scope="col">Description</th>
                                            <th scope="col">Title</th>
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

export default Badges;
