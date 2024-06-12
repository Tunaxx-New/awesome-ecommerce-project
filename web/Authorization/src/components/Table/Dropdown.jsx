import React, { useState, useEffect } from 'react';

const Dropdown = ({ column, options = [], handleFilter }) => {
	const useDropdown = () => {
	  const [isOpen, setIsOpen] = useState(false);

	  const toggleDropdown = (toggle) => {
		setIsOpen(!toggle ? toggle : !isOpen);
	  };

	  return {
		isOpen,
		toggleDropdown,
	  };
	};
	const { isOpen: isOpen, toggleDropdown: toggleDropdown } = useDropdown();
	  const handleOptionClick = (option) => {
		option.selected = !option.selected;
	  };
  return (
	<div className={`dropdown ${isOpen ? 'open' : ''}`} onMouseLeave={() => { toggleDropdown(false); handleFilter(column, options); }}>
      <button onClick={() => toggleDropdown(column)}>Open Dropdown</button>
      <div className="dropdown-content">
        {options.map((option, index) => (
          <label key={index}>
            <input type="checkbox"
            onChange={() => handleOptionClick(option)}
			/>
            {option.title}
          </label>
        ))}
      </div>
    </div>
  );
};

export default Dropdown;