import React, { useState, useEffect } from 'react';
import './Table.css';
import Dropdown from './Dropdown';
import nestedKeys from '../../utils/nestedKeys';
import api from '../../apis/api';
import getValueByNestedKey from '../../utils/nestedKeys';
import { Link } from 'react-router-dom';

function Table({ initialData = [], initialItemsPerPage = 10, initialTableName = "", initialTableType = "FriendlyUI", initialEndpoint = "" }) {
	const [tableName, setTableName] = useState(initialTableName);
	const [tableType, setTableType] = useState(initialTableType);
	const [endpoint, setEndpoint] = useState(initialEndpoint);
	const [data, setData] = useState([]);
	const [itemsPerPage, setItemsPerPage] = useState(initialItemsPerPage);

	const [totalItems, setTotalItems] = useState(0);
	const [distinctKeys, setDistinctKeys] = useState([]);

	const [currentPage, setCurrentPage] = useState(1);
	const [currentPageView, setCurrentPageView] = useState(1);
	const [sortBy, setSortBy] = useState('');
	const [sortOrder, setSortOrder] = useState('');
	const [filters, setFilters] = useState([]);

	const [filterOptions, setFilterOptions] = useState([]);

	const totalPages = totalItems / itemsPerPage;

	useEffect(() => {
		const fetchData = async () => {
			if (initialData.length > 0) {
				let sortedData = [...initialData];
				if (sortBy != '') {
					sortedData = sortedData.sort((a, b) => {
						const aValue = nestedKeys.getValueByNestedKey(a, sortBy) + "";
						const bValue = nestedKeys.getValueByNestedKey(b, sortBy) + "";
						const comparison = aValue.localeCompare(bValue);
						if (sortOrder === "asc") {
							return comparison;
						} else {
							return -comparison;
						}
					});
				}

				let filteredData = [...sortedData];
				if (filterOptions.length > 0) {
					filteredData = filteredData.filter((item, index) => {
						let isAllFalse = false;
						if (filterOptions[index]) {
							filterOptions[index].every(filter => {
								isAllFalse = isAllFalse || filter.selected;
							});

							if (!isAllFalse)
								return true;

							return filterOptions[index].every(filter => {
								const itemValue = nestedKeys.getValueByNestedKey(item, filter.key) + "";
								console.log(itemValue, filter, itemValue.includes(filter.title));
								return itemValue.includes(filter.title);
							});
						}
					});
				}

				setData(filteredData);
				setTotalItems(initialData.length);
			} else {
				const sort = sortBy && sortOrder ? `${sortBy},${sortOrder}` : "";
				//`${initialEndpoint}?page=${currentPage}&size=${itemsPerPage}&sort=${sort}`
				/*
				const response = await api("/api" + initialEndpoint, {
					method: "GET",
					withCredentials: true,
					headers: {
						'Content-Type': 'application/json',
					},
				});
				*/
				//return response;
			}
			//setItemsPerPage(initialItemsPerPage);
		}
		fetchData();
	}, [itemsPerPage, initialItemsPerPage, sortOrder, sortBy, initialEndpoint, filterOptions]);

	useEffect(() => {
		if (distinctKeys.length <= 0) {
			const distinctKeysSet = new Set();
			data.forEach(obj => {
				nestedKeys.getNestedKeys(obj).forEach(key => {
					distinctKeysSet.add(key);
				});
			});
			const distinctKeys_ = Array.from(distinctKeysSet);
			setDistinctKeys(distinctKeys_);
		}
		if (filterOptions.length <= 0) {
			const filterSets = [];
			distinctKeys.forEach(key => {
				const filtersSet = new Set();
				data.forEach(obj_ => {
					filtersSet.add({ 'title': `${nestedKeys.getValueByNestedKey(obj_, key)}`, 'selected': false, 'key': `${key}` });
				});
				filterSets.push(Array.from(filtersSet));
			})
			setFilterOptions(filterSets);
		}
	}, [data, distinctKeys]);

	const getComputedStyleValue = (element, propertyName) => {
		return getComputedStyle(element).getPropertyValue(propertyName);
	};

	useEffect(() => {
		if (tableType == "Developer") {
			document.documentElement.style.setProperty('--primary-color', rgbToGrayscale('#046A37'));
			document.documentElement.style.setProperty('--light-green', rgbToGrayscale('#FAFFFD'));
			document.documentElement.style.setProperty('--light-green-hover', rgbToGrayscale('#CBDBCA'));
			document.documentElement.style.setProperty('--bright-green', rgbToGrayscale('#EBFAF6'));
			document.documentElement.style.setProperty('--dark-green-hover', rgbToGrayscale('#02452B'));
			document.documentElement.style.setProperty('--green-hover', rgbToGrayscale('#CADEDA'));
			document.documentElement.style.setProperty('--disabled-bg', rgbToGrayscale('#60807B'));
			document.documentElement.style.setProperty('--light-gray', rgbToGrayscale('#E3E8E6'));
		}
	}, [tableType])

	const rgbToGrayscale = (rgb) => {
		// Extract red, green, and blue channels
		const red = parseInt(rgb.slice(1, 3), 16);
		const green = parseInt(rgb.slice(3, 5), 16);
		const blue = parseInt(rgb.slice(5, 7), 16);

		// Convert to grayscale by averaging the channels
		const grayscale = Math.round(0.299 * red + 0.587 * green + 0.114 * blue);

		// Convert grayscale value back to hexadecimal
		const grayscaleHex = grayscale.toString(16).padStart(2, '0');

		return `#${grayscaleHex}${grayscaleHex}${grayscaleHex}`;
	};

	const handlePageChange = (pageNumber) => {
		setCurrentPage(pageNumber);
	};

	const handleSort = (column, order) => {
		if (column == sortBy && order == sortOrder) {
			setSortBy("");
			setSortOrder("");
		} else {
			setSortBy(column);
			setSortOrder(order);
		}
	};

	const arrowDirection = (column) => {
		if (column === sortBy) {
			return (
				<div className="sort-buttons">
					<button
						className={`sort-button ${sortOrder === 'asc' ? 'active' : ''}`}
						onClick={() => handleSort(column, 'asc')}
					>
						<i className={`sort-icon bi ${sortOrder === 'asc' ? 'bi-caret-up-fill' : 'bi-caret-up'}`}></i>
					</button>
					<button
						className={`sort-button ${sortOrder === 'desc' ? 'active' : ''}`}
						onClick={() => handleSort(column, 'desc')}
					>
						<i className={`sort-icon bi ${sortOrder === 'desc' ? 'bi-caret-down-fill' : 'bi-caret-down'}`}></i>
					</button>
				</div>
			);
		}
		return (
			<div className="sort-buttons">
				<button className="sort-button"
					onClick={() => handleSort(column, 'asc')}
				>
					<i className="bi bi-caret-up sort-icon"></i>
				</button>
				<button className="sort-button"
					onClick={() => handleSort(column, 'desc')}
				>
					<i className="bi bi-caret-down sort-icon"></i>
				</button>
			</div>
		);
	};

	const currentData = data;

	if (currentData.length === 0) {
		return <p>No data available.</p>;
	}

	const handleFilter = (column, filters) => {
		setFilters({ 'column': column, 'filters': filters });
	};

	const startIndex = (currentPage - 1) * itemsPerPage;
	const endIndex = Math.min(startIndex + itemsPerPage, totalItems);

	return (
		<div className="main">
			<div className="pagination">
				<div className="title">
					<h3>{tableName}</h3>
				</div>
			</div>

			<div style={{ overflowX: "auto" }}>
				<table>
					<thead>
						<tr>
							{distinctKeys.map((key, index) => (
								<th>
									<div class="th-header">
										<button className="sort-button">{arrowDirection(key)}</button>
										<span class="th-label">{key}</span>
										{/*<Dropdown column="id" options={filterOptions[index]} handleFilter={handleFilter} />*/}
									</div>
								</th>
							))}
						</tr>
					</thead>
					<tbody>
						{currentData.slice(startIndex, endIndex).map((item, index) => (
							<tr key={index}>
								{distinctKeys.map((key, rowIndex) => {
									const value = nestedKeys.getValueByNestedKey(item, key); // Define value outside of JSX
									return (
										<td key={rowIndex}>
											{/* Check if the value contains newline characters */}
											{(value && (typeof value === 'string' || Array.isArray(value)) && value.includes('\\n')) ? (
												/* If the value contains newline characters, split it into separate lines */
												value.split('\\n').map((line, index) => (
													<React.Fragment key={index}>
														{/* Render each line within a <div> */}
														<div>{line}</div>
													</React.Fragment>
												))
											) : (value.includes('http') ?
												(<img className='image-blob' src={value.replaceAll("\"", "")} />) :
												((value.includes('❌')) ? (
													<Link className='btn btn-outline-dark' to={"/product-review/" + value.replaceAll("\"", "").split(",")[1] + "/" + value.replaceAll("\"", "").split(",")[2]}>
														{value.replaceAll("\"", "").split(",")[0]} Make Review
													</Link>
												) :
													(value.replaceAll("\"", ""))))}
										</td>
									);
								})}
							</tr>
						))}
					</tbody>
				</table>
			</div>

			{totalPages > 1 &&
				<div className="pagination">
					<div className="controls">
						<button
							onClick={() => handlePageChange(1)}
							disabled={currentPage === 1}
						>
							<i class="bi bi-chevron-double-left"></i>
						</button>
						<button
							onClick={() => handlePageChange(currentPage - 1)}
							disabled={currentPage === 1}
						>
							<i class="bi bi-chevron-left"></i>
						</button>

						{/* Display 2-3 page numbers to the left of the input */}
						{Array.from({ length: Math.min(2, currentPage - 1) }, (_, i) => (
							<button
								key={i}
								onClick={() => handlePageChange(currentPage - 1 - i)}
							>
								{currentPage - 1 - i}
							</button>
						)).reverse()}

						<input
							type="number"
							id="inputPage"
							value={currentPage}
							onChange={(e) => {
								const pageNumber = Number(e.target.value);
								if (pageNumber >= 1 && pageNumber <= totalPages) {
									handlePageChange(pageNumber);
								}
							}}
							min="1"
							max={totalPages}
						/>

						{/* Display 2-3 page numbers to the right of the input */}
						{Array.from({ length: Math.min(2, totalPages - currentPage) }, (_, i) => (
							<button
								key={i}
								onClick={() => handlePageChange(currentPage + 1 + i)}
							>
								{currentPage + 1 + i}
							</button>
						))}

						<button
							onClick={() => handlePageChange(currentPage + 1)}
							disabled={currentPage >= totalPages}
						>
							<i class="bi bi-chevron-right"></i>
						</button>
						<button
							onClick={() => handlePageChange(totalPages)}
							disabled={currentPage >= totalPages}
						>
							<i class="bi bi-chevron-double-right"></i>
						</button>
					</div>
				</div>}
		</div>
	);
}

export default Table;
