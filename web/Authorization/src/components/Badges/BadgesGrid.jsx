import React, { useState } from 'react';
import './ItemGrid.css'; // Make sure this points to your CSS file

// TODO: tag.type => преобразвать в какой-нибудь цвет
const items = [
  {
    id: 1,
    title: 'Название предмета',
    description: `Дополнительная информация о предмете \n\n\n\n\n\n Скролл`,
    tags: [{
      title: 'example-new',
      type: 'new'
    }, {
      title: 'example-popular',
      type: 'popular'
    }, {
      title: 'example-sale',
      type: 'sale'
    }, {
      title: 'example-limited',
      type: 'limited'
    }, {
      title: 'example-limited',
      type: 'limited'
    }, {
      title: 'example-limited',
      type: 'limited'
    },],
    imageSrc: "https://t4.ftcdn.net/jpg/03/50/11/83/360_F_350118359_fs2GIXzHjBhStQtRXq4yI927EcSxfS9A.jpg",
    curiosities: [{
      first: 'Этот значок есть у',
      second: '16% пользователей',
      value: '0.16',
    }, {
      first: 'Это значок компании',
      second: 'Rakhat',
      link: 'https://www.google.com/',
      value: 'id компании',
    }, {
      first: 'Это значок продавца',
      second: 'Арман Т. Б.',
      link: 'https://www.google.com/',
      value: 'id продавца',
    }
    ]
  },
];

const BadgesGrid = ({ badges = items }) => {
  const [selectedItem, setSelectedItem] = useState(null);

  const onItemClick = (item) => {
    setSelectedItem(item);
  };

  const getBadgeColor = (type) => {
    switch (type) {
      case 'NEW':
        return 'var(--badge-color-new)';
      case 'POPULAR':
        return 'var(--badge-color-popular)';
      case 'SALE':
        return 'var(--badge-color-sale)';
      case 'LIMITED':
        return 'var(--badge-color-limited)';
    }
  };

  return (

    <div className="main">
      <style>
        {`

:root {
  --cell-width: 80px; /* Default cell width */
  --cell-height: 80px; /* Default cell height */
  
  --badge-color-new: #007bff;      /* Blue */
  --badge-color-popular: #28a745;  /* Green */
  --badge-color-sale: #ffc107;     /* Yellow */
  --badge-color-limited: #dc3545;  /* Red */
  --badge-color-default: #6c757d;  /* Gray */
}

.main {
  position: relative!important;
}

.grid-container {
  width: 100%;
  min-height: 200px;
  position: relative;
  display: flex;
  overflow-x: auto; /* Enable horizontal scrolling */
}

.grid-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1; /* Place behind items */
}

.grid-slot {
  background-color: #f0f0f0; /* Background color for grid slots */
}

.grid-items {
  display: grid;
  width: 100%;
  justify-items: center;
  grid-template-columns: repeat(5, 1fr); /* 3 items per row */
  grid-template-rows: repeat(5, auto); /* 4 rows */
  gap: 10px;
  z-index: 1; /* Place in front of grid slots */
}

.grid-item {
  width: var(--cell-width); /* Take full width of grid cell */
  height: var(--cell-height); /* Square items */
  border: 1px solid #ccc;
  cursor: pointer;
}

.grid-item:hover {
  background-color: #f0f0f0;
}

.item-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.tags {
  display: flex;
  justify-content: left;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.badge {
  background-color: #007bff;
  color: #fff;
  padding: 5px 10px;
  border-radius: 5px;
  display: block;
}

.item-details {
  background-color: #fff;
  border: 1px solid #ccc;
  z-index: 1000;
  width: 100%;
  height: 100%;
  padding: 8px;
}

.item-info {
  position: relative;
	display: flex;
	height: 50%;
	width: 100%;
  margin-bottom: 32px;
}

.horizontal-flex {
	display: flex;
	height: 100%;
	width: 100%;
	flex-direction: row;
}

.vertical-flex {
	display: flex;
	width: 100%;
	flex-direction: column;
}

.scrollable-vertical {
	overflow: auto;
	overflow-y: auto; /* Allows vertical scrolling when content overflows */
    overflow-x: hidden;
}

.newline-content {
  white-space: pre-line;
}

.item-details button {
  margin-top: 20px;
}

.item-image {
  width: 100%; /* Take full width of grid item */
  height: 100%; /* Take full height of grid item */
  object-fit: cover; /* Cover the entire container while maintaining aspect ratio */
}


/* Curiosities */
.info-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  box-shadow: none;
  table-layout: auto;
}

.info-table th, .info-table td {
  padding: 8px 12px;
  text-align: left;
  background-color: white;
  border: 0;
  text-align: left;
  font-weight: 200;
  color: black;
  width: auto;
}

.info-table th {
  background-color: var(--light-gray);
  text-align: right;
  width: 1%;
  white-space: nowrap;
}
      `}
      </style>
      <div className="item-info">
        {selectedItem && (
          <div className="horizontal-flex">
            <div className="vertical-flex" style={{ maxWidth: '60%', maxHeight: '100%' }}>
              <div className="item-details scrollable-vertical" style={{ minHeight: '100%', maxHeight: '50%' }}>
                <h3>{selectedItem.title}</h3>
                <p className="newline-content">{selectedItem.description}</p>
                <div className="tags">
                  {selectedItem.tags.map(tag => (
                    <span className="badge" style={{ backgroundColor: getBadgeColor(tag.type) }}>{tag.title}</span>
                  ))}
                </div>
              </div>
              {/*
              <div className="item-details" style={{ minHeight: '50%', maxHeight: '50%' }}>
                <table className="info-table">
                  <tbody>
                    {selectedItem.curiosities && selectedItem.curiosities.map(curiosity => (
                      <tr>
                        <th>{curiosity.first}</th>
                        <td>
                          {curiosity.link ? (
                            <a href={curiosity.link} target="_blank" rel="noopener noreferrer">
                              {curiosity.second}
                            </a>
                          ) : (
                            curiosity.second
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
              */}
            </div>
            <div className="" style={{ maxWidth: '40%' }}>
              <img className="item-image" src={selectedItem.imageSource} alt={selectedItem.title} />
            </div>
          </div>
        )}
      </div>
      {/* Display additional details when an item is clicked */}

      {badges &&
        <div className="grid-container">
          <div className="grid-items">
            {badges.map && badges.map((item, index) => (
              <div key={index} className="grid-item" onClick={() => onItemClick(item)}>
                <div className="item-content">
                  <img className="item-image" src={item.imageSource} alt={item.title} />
                </div>
              </div>
            ))}
          </div>

          <div className="grid-background">
            <div className="grid-items">
              {/* Grid net background with 20 slots */}
              {Array.from({ length: 20 }).map((_, index) => (
                <div key={index} className="grid-item grid-slot">
                  <div className="item-content"></div>
                </div>
              ))}
            </div>
          </div>
        </div>
      }
    </div>
  );
};

export default BadgesGrid;
