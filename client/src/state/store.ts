import {
  StoreCreator,
  createStore,
  combineReducers,
  applyMiddleware,
  compose
} from "redux";
import { routerMiddleware, connectRouter } from "connected-react-router";
import { createBrowserHistory } from "history";
import coreMiddleware from "./core/middleware";
import coreReducer from "./core/reducer";

export const customHistory = createBrowserHistory();

declare global {
  interface Window {
    devToolsExtension: Function;
  }
}

const finalCreateStore = compose<any>(
  applyMiddleware(coreMiddleware, routerMiddleware(customHistory)),
  window.devToolsExtension
    ? window.devToolsExtension({
        name: "MyApp",
        actionsBlacklist: ["REDUX_STORAGE_SAVE"]
      })
    : f => f
)(createStore) as StoreCreator;

export const store = finalCreateStore(
  combineReducers({ router: connectRouter(customHistory), core: coreReducer })
);
