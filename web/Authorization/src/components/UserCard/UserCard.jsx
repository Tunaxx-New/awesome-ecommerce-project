import React, { Component } from 'react';
import "./UserCard.css";

class UserCard extends Component {
    constructor(props) {
        super(props);
        this.state = {
            link: props.link,
            imageSource: props.imageSource,
            upperText: props.upperText,
            lowerText: props.lowerText,
        };
    }

    getAvataerLightColor = (role) => {
        switch (role) {
            case 'Seller':
                return 'var(--badge-color-new)';
            case 'User':
                return 'var(--badge-color-default)';
            case 'Buyer':
                return 'var(--badge-color-sale)';
            case 'Admin':
                return 'var(--badge-color-popular)';
            default:
                return 'var(--badge-color-default)';
        }
    }

    render() {
        return (
            <div className='user-card' style={{
                display: 'flex', flexDirection: 'row', alignItems: 'center', padding: "8px",
                boxShadow: `0px 0px 0px`, color: `${this.getAvataerLightColor(this.props.lowerText)}`
            }}
                onClick={() => {
                    window.location.href = this.props.link; // Update the URL with the parameter
                }}>
                <div className='hover-avatar-light-parent' style={{ display: 'inline-block', position: "relative", zIndex: 1 }}>
                    <div style={{ backgroundColor: "white", borderRadius: "50%" }}>
                        <img
                            className='user-card-img'
                            src={this.props.imageSource}
                            style={{ borderRadius: '50%', backgroundColor: 'white' }}
                        />
                    </div>
                    <div
                        className='hover-avatar-light'
                        style={{
                            position: "absolute",
                            borderRadius: '50%',
                            width: "100%",
                            height: "100%",
                            backgroundColor: `${this.getAvataerLightColor(this.props.lowerText)}`, // Red color
                            opacity: 1,
                            zIndex: -1,
                            transform: "translateY(-100%)",
                        }}
                    ></div>
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                    <span className='user-card-span' style={{ fontSize: 18, fontWeight: 'bold' }}>{this.props.upperText}</span>
                    <span className='user-card-span' style={{ fontSize: 14 }}>{this.props.lowerText}</span>
                </div>
            </div >
        );
    }
}

export default UserCard;