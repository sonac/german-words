import * as React from "react";
import * as ReactDOM from "react-dom";
import { store } from "state/store";
import App from "components/App/index";
import { Provider } from "react-redux";

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById("root")
);
