import * as React from "react";
import { Component } from "react";
import { BrowserRouter as Router } from "react-router-dom";
import { connect } from "react-redux";
import Body from "../Body";

const styles = require("./styles.css");

interface Data {}

interface Actions {}

interface Props {}

type State = {};

export default class App extends Component<Props, State> {
  render() {
    return (
      <Router>
        <div className={styles.app}>
          <Body />
        </div>
      </Router>
    );
  }
}
