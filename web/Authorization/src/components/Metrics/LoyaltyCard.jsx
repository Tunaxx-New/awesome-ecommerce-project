import React from 'react';

const LoyaltyIndexCard = ({ value = 0, title = "", tresholds = [0.5, 0.25], tresholdCoef = 1, textAdd = "" }) => {
  const color = value * tresholdCoef > tresholds[0] * tresholdCoef ? "#4caf50" : value * tresholdCoef > tresholds[1] * tresholdCoef ? "#ec942c" : "#e54141";
  const colorBack = value * tresholdCoef > tresholds[0] * tresholdCoef ? "#398938" : value * tresholdCoef > tresholds[1] * tresholdCoef ? "#c17821" : "#b33535";
  return (
    <div>
      <style>
        {`
            /* CSS */
            .card {
              border: 1px solid #e0e0e0;
              border-radius: 8px;
              box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
              background-color: #f9f9f9;
              margin: 20px;
              width: 300px;
            }
            
            .card-header {
              padding: 12px;
              color: #ffffff;
              text-align: center;
              border-top-left-radius: 8px;
              border-top-right-radius: 8px;
            }
            
            .card-body {
              padding: 20px;
            }
            
            .loyalty-index-content {
              display: flex;
              justify-content: space-between;
              align-items: center;
            }
            
            .loyalty-index-value {
              font-size: 48px;
              font-weight: bold;
            }
            
            .loyalty-index-description {
              font-size: 16px;
            }
            
            .card-footer {
              padding: 12px;
              text-align: center;
            }
            #loyalty-card-${title.replace(/\s/g, "")} {
                background-color: ${color};
                border:0;
            }
            #loyalty-card-${title.replace(/\s/g, "")}:hover {
                background-color: ${colorBack};
                border:0;
            }
            `}
      </style>
      <div className="card">
        <div className="card-header" style={{ backgroundColor: color }}>
          <h5 className="card-title">{title}</h5>
        </div>
        <div className="card-body">
          <div className="loyalty-index-content">
            <div className="loyalty-index-value" style={{ color: color }}>{value}{textAdd}</div>
            <div className="loyalty-index-description" style={{ color: color }}>{value * tresholdCoef > tresholds[0] * tresholdCoef ? "Hight" : value * tresholdCoef > tresholds[1] * tresholdCoef ? "Medium" : "Low"}</div>
          </div>
        </div>
        <div className="card-footer">
          <button id={`loyalty-card-${title.replace(/\s/g, "")}`} className="btn btn-success">Learn More</button>
        </div>
      </div>
    </div>
  );
};

export default LoyaltyIndexCard;
