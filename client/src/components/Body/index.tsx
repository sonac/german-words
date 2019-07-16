import * as React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import { Word, CoreState } from "state/core/types";
import { BasicActionCreator } from "state/types";
import { getWord } from "state/core/actions";
import WordPage from "../WordPage";
import ResultPage from "../ResultPage";

const styles = require("./styles.css");

interface Data {
  guessed: boolean;
}

interface Actions {}

interface Props {
  data: Data;
  actions: Actions;
}

type State = {};

class Body extends Component<Props, State> {
  render() {
    return (
      <div className={styles.body}>
        {this.props.data.guessed === null ? <WordPage /> : <ResultPage />}
      </div>
    );
  }
}

const mapStateToProps = (state: { core: CoreState }): Data => ({
  guessed: state.core.guessed
});

const mapDispatchToProps = {};

const mergeProps = (data: Data, actions: Actions): Props => ({
  data,
  actions
});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
  mergeProps
)(Body);
