import React, { useRef, useEffect, useState } from 'react';

const Diagram = ({ data, xAxisName, yAxisName, title, coefficient, zoomFactor_ = { x: 0.5, y: 0.5 }, margin_ = { x: -25, y: -25 }, backgroundColor = "#ddd" }) => {
  const svgRef = useRef(null);
  const [zoomFactor, setZoomFactor] = useState(zoomFactor_);
  const [margin, setMargin] = useState(margin_);

  // Function to format integer date to a string (YYYY.MM.DD)
  const formatDateFromInteger = (integerDate) => {
    const date = new Date(integerDate);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}.${month}.${day}`;
  };

  const handleZoomFactorChange = (axis, value) => {
    setZoomFactor(prevZoomFactor => ({
      ...prevZoomFactor,
      [axis]: value
    }));
  };

  const handleMarginChange = (axis, value) => {
    setMargin(prevMargin => ({
      ...prevMargin,
      [axis]: value
    }));
  };

  useEffect(() => {
    const svg = svgRef.current;
    if (svg) {
      const svgWidth = svg.clientWidth;
      const svgHeight = svg.clientHeight;
      const dataRangeX = Math.max(...data.map(point => point.x)) - Math.min(...data.map(point => point.x));
      const dataRangeY = Math.max(...data.map(point => point.y)) - Math.min(...data.map(point => point.y));

      const marginPercentageX = margin.x; // 5% margin on x-axis
      const marginPercentageY = margin.y; // 5% margin on y-axis

      const xZoom = 1 / zoomFactor.x + dataRangeX;
      const yZoom = 1 / zoomFactor.y + dataRangeY;
      const xMargin = (dataRangeX * xZoom * marginPercentageX) / 100;
      const yMargin = (dataRangeY * yZoom * marginPercentageY) / 100;

      // Calculate grid spacing
      const minX = Math.min(...data.map(point => point.x)) + xMargin;
      const maxX = Math.max(...data.map(point => point.x)) + xMargin;
      const minY = Math.min(...data.map(point => point.y)) + yMargin;
      const maxY = Math.max(...data.map(point => point.y)) + yMargin;

      // Calculate grid spacing based on SVG width and data range
      const xGridSpacing = svgWidth / ((Math.max(...data.map(point => point.x)) - Math.min(...data.map(point => point.x))) * xZoom);
      const yGridSpacing = svgHeight / ((Math.max(...data.map(point => point.y)) - Math.min(...data.map(point => point.y))) * yZoom);
      const maxValue = 10;//Math.max(...data.map(point => Math.max(point.x, point.y)));

      // Clear existing content
      svg.innerHTML = '';

      // Helper function to create SVG elements
      const createSvgElement = (type, attributes) => {
        const element = document.createElementNS('http://www.w3.org/2000/svg', type);
        Object.entries(attributes).forEach(([key, value]) => element.setAttribute(key, value));
        return element;
      };

      const numGridLinesX = Math.ceil(dataRangeX / xGridSpacing);
      const numGridLinesY = Math.ceil(dataRangeY / yGridSpacing);

      // Create and append grid lines and values
      const createGrid = () => {
        const gridLines = [];
        const gridValuesX = [];
        const gridValuesY = [];

        for (let i = -maxValue; i <= 2 * maxValue; i++) {
          const valueX = minX + i * ((maxX - minX) / 10) - xMargin;
          const valueY = minY + i * ((maxY - minY) / 10) - yMargin;
          // Horizontal grid lines
          gridLines.push(
            createSvgElement('line', {
              x1: 0,
              y1: svgHeight - (valueY - minY) * yGridSpacing,
              x2: svgWidth,
              y2: svgHeight - (valueY - minY) * yGridSpacing,
              stroke: '#ccc',
              strokeWidth: '1',
              strokeDasharray: '5',
            })
          );

          // Grid values for y-axis
          const textY = createSvgElement('text', {
            x: '10',
            y: svgHeight - (valueY - minY) * yGridSpacing + 5,
            textAnchor: 'start',
            alignmentBaseline: 'middle',
            fontSize: '12',
          });
          textY.textContent = Math.round(valueY);
          gridValuesY.push(textY);
          // Vertical grid lines
          gridLines.push(
            createSvgElement('line', {
              x1: (valueX - minX) * xGridSpacing,
              y1: 0,
              x2: (valueX - minX) * xGridSpacing,
              y2: svgHeight,
              stroke: '#ccc',
              strokeWidth: '1',
              strokeDasharray: '5',
            })
          );

          // Grid values for x-axis
          console.log(xGridSpacing);
          if (i % Math.round(1000 / xGridSpacing) == 0) {
            const textX = createSvgElement('text', {
              x: (valueX - minX) * xGridSpacing,
              y: svgHeight - 12,
              textAnchor: 'middle',
              alignmentBaseline: 'middle',
              fontSize: '12',
            });
            textX.textContent = formatDateFromInteger(valueX * coefficient);
            gridValuesX.push(textX);

            const textBackground = createSvgElement('rect', {
              x: (valueX - minX) * xGridSpacing - 25, // Adjust as needed
              y: svgHeight - 20, // Adjust as needed
              width: 50, // Adjust as needed
              height: 20, // Adjust as needed
              fill: backgroundColor, // Adjust as needed
              opacity: '0.7', // Adjust as needed
            });
            svg.appendChild(textBackground);

            const textXupd = createSvgElement('text', {
              x: (valueX - minX) * xGridSpacing,
              y: svgHeight,
              textAnchor: 'middle',
              alignmentBaseline: 'middle',
              fontSize: '12',
            });
            textXupd.textContent = new Date(valueX * coefficient).toLocaleTimeString('en-US', { hour: 'numeric', hour12: true });
            svg.appendChild(textXupd);
          }
        }

        // Append grid lines and values to SVG
        gridLines.forEach(line => svg.appendChild(line));
        gridValuesY.forEach(text => svg.appendChild(text));
        gridValuesX.forEach(text => svg.appendChild(text));
      };

      // Create and append data lines and circles
      const createDataPoints = () => {
        const lines = [];
        const circles = [];

        for (let i = 0; i < data.length - 1; i++) {
          const startPoint = data[i];
          const endPoint = data[i + 1];
          // Line between data points
          lines.push(
            createSvgElement('line', {
              x1: (startPoint.x - minX) * xGridSpacing,
              y1: svgHeight - (startPoint.y - minY) * yGridSpacing,
              x2: (endPoint.x - minX) * xGridSpacing,
              y2: svgHeight - (endPoint.y - minY) * yGridSpacing,
              stroke: 'green',
              strokeWidth: '2',
            })
          );

          // Circles at data points
          circles.push(
            createSvgElement('circle', {
              cx: (startPoint.x - minX) * xGridSpacing,
              cy: svgHeight - (startPoint.y - minY) * yGridSpacing,
              r: '3',
              fill: 'green',
            })
          );
        }

        // Append last point's circle
        if (data.length > 1) {
          const lastPoint = data[data.length - 1];
          circles.push(
            createSvgElement('circle', {
              cx: (lastPoint.x - minX) * xGridSpacing,
              cy: svgHeight - (lastPoint.y - minY) * yGridSpacing,
              r: '3',
              fill: 'green',
            })
          );
        }

        // Append to SVG
        lines.forEach(line => svg.appendChild(line));
        circles.forEach(circle => svg.appendChild(circle));
      };

      // Generate grid and data points
      createGrid();
      createDataPoints();
    }
  }, [data, coefficient, zoomFactor, margin]);

  return (
    <div style={{ backgroundColor: backgroundColor }}>
      <style>
        {`
        input {
          margin-left: 8px;
        }
        label {
          display:flex;
          align-items:center;
        }
        h3 {
          text-align: center;
        }
        .margin-div {
          margin: 16px;
        }
        `}
      </style>
      <h3>{title}</h3>
      <div style={{ width: '100%', height: '400px', position: 'relative' }}>
        <svg ref={svgRef} width="100%" height="100%">
          {/* Static elements can go here */}
          {/* Dynamic elements will be appended via useEffect */}
        </svg>
        <h6 style={{ position: 'absolute', top: "0", left: '20px', transform: 'translateX(-10px)', backgroundColor: backgroundColor, padding: "8px" }}>{yAxisName}</h6>
        <h6 style={{ position: 'absolute', bottom: "-40px", left: '50%', transform: 'translateX(-50%)', padding: "8px" }}>{xAxisName}</h6>
      </div>
      <div className="" style={{ backgroundColor: "#ddd", marginTop: "32px", justifyContent: "space-between", display: "flex", width: "100%", alignItems: "center" }}>
        <div className="margin-div">
          <h6>Zoom</h6>
          <div>
            <label>
              X
              <input
                type="number"
                value={zoomFactor.x}
                step={0.001}
                onChange={e => handleZoomFactorChange('x', parseFloat(e.target.value))}
              />
              <input
                type="range"
                min={-2}
                max={2}
                step="0.01"
                value={zoomFactor.x}
                onChange={e => handleZoomFactorChange('x', parseFloat(e.target.value))}
              />
            </label>
          </div>
          <div>
            <label>
              Y
              <input
                type="number"
                value={zoomFactor.y}
                onChange={e => handleZoomFactorChange('y', parseFloat(e.target.value))}
              />
              <input
                type="range"
                min={-2}
                max={2}
                step="0.01"
                value={zoomFactor.y}
                onChange={e => handleZoomFactorChange('y', parseFloat(e.target.value))}
              />
            </label>
          </div>
        </div>
        <div>
          <h6>Margin</h6>
          <div>
            <label>
              X
              <input
                type="number"
                value={margin.x}
                onChange={e => handleMarginChange('x', parseFloat(e.target.value))}
              />
              <input
                type="range"
                min={-100}
                max={100}
                step="0.01"
                value={margin.x}
                onChange={e => handleMarginChange('x', parseFloat(e.target.value))}
              />
            </label>
          </div>
          <div>
            <label>
              Y
              <input
                type="number"
                value={margin.y}
                onChange={e => handleMarginChange('y', parseFloat(e.target.value))}
              />
              <input
                type="range"
                min={-100}
                max={100}
                step="0.01"
                value={margin.y}
                onChange={e => handleMarginChange('y', parseFloat(e.target.value))}
              />
            </label>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Diagram;
