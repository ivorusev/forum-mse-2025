import { Link, useLocation } from "react-router-dom";

export default function ErrorPage({ statusCode }: { statusCode?: number }) {

  const location = useLocation();

  const getMessage = () => {
    switch (statusCode) {
      case 404:
        return "Page not found.";
      case 500:
        return "Internal server error.";
      case 403:
        return "Access denied.";
      case 400:
        return "Bad request.";
      case 503:
        return "Service unavailable.";
      default:
        return "An unexpected error occurred.";
    }
  };

  return (
    <div className="container text-center mt-5">
      <h1>
        Error {statusCode || "Unknown"}
      </h1>
      <p className="d-none">{getMessage()}</p>
      <p className="d-none">
        URL: <code>{location.pathname}</code>
      </p>
      <p>
	  {"Go back "} 
        <Link to="/" className="btn btn-secondary btn-sm">
          Home
        </Link>
      </p>
    </div>
  );
}
