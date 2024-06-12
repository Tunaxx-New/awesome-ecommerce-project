import React, { Component } from "react";
import { Table } from "../../components";
import { Tag } from "../../components";
import {Link} from 'react-router-dom';

//
// Error boundary for catching errors
//
class ErrorBoundary extends Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, errors: [] };
    this.componentDidCatch = this.componentDidCatch.bind(this);
  }

  static getDerivedStateFromError(error) {
    return { hasError: true };
  }

  componentDidCatch(error, errorInfo) {
    this.setState(prevState => ({
      errors: [...prevState.errors, { error, errorInfo }]
    }));
  }

  handleGoBack = () => {
    this.setState({ hasError: false, errors: [] });
  }

  processNetworkErrorToType = (code) => {
    switch (code[0]) {
      case '2':
        return 'success';
      case '3':
        return 'warn';
      case '4':
        return 'fail';
      default:
        return '';
    }
  }

  processErrorExplanation = (code, errorJson) => {
    if (errorJson && errorJson.error && errorJson.error.exceptionMessage) {
      return (
        <div>
          <h2>{errorJson.error.error}</h2>
          <p>{errorJson.error.exceptionMessage}</p>
          {errorJson.error.column ? (<p>In field {errorJson.error.column}</p>) : ""}
        </div>
      );
    } else {
      switch (code) {
        case '403':
          return (
            <p>Unauthorized request, try to <Link to="/login">login</Link></p>
          );
      }
    }
  }

  render() {
    if (this.state.hasError && this.state.errors.length > 0) {
      let errorStrings = this.state.errors[0].error.message.split('~');
      if (!errorStrings[1])
        errorStrings[1] = '';
      if (!errorStrings[2])
        errorStrings[2] = '{}';
      console.log(errorStrings);
      // You can render any custom fallback UI
      return (
        <div>
          <h1>Something went wrong. Please try again later.</h1>
          <div key={1}>
            {this.processErrorExplanation(errorStrings[0], JSON.parse(errorStrings[2]))}
            <Tag texts={[`${errorStrings[0]} ${errorStrings[1]}`]} types={this.processNetworkErrorToType(errorStrings[0])}></Tag>
          </div>
          <button onClick={this.handleGoBack}>Go Back</button>
          <details style={{ whiteSpace: 'pre-wrap' }}>
            <h3>Error structure</h3>
            <p>{errorStrings.join(', ')}</p>
            <div>
              <h3>Error component stack</h3>
              {this.state.errors[0].errorInfo.componentStack}
            </div>
            <Table initialData={this.state.errors} initialItemsPerPage={10} initialTableName="Errors" initialTableType="Developer" />
          </details>
        </div>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
