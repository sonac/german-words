import * as React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import { Word, CoreState } from "state/core/types";
import { BasicActionCreator } from "state/types";
import { getWord } from "state/core/actions";

const styles = require("./styles.css");

interface Data {
  word: Word;
  guessed: boolean;
}

interface Actions {
  getWord: BasicActionCreator;
}

interface Props {
  data: Data;
  actions: Actions;
}

type State = {};

class ResultPage extends Component<Props, State> {
  onClick = () => {
    this.props.actions.getWord();
  };
  render() {
    if (this.props.data.word === null) {
      return <div className={styles.body}>...loading</div>;
    }
    return (
      <div className={styles.result}>
        {this.props.data.guessed
          ? `Correct! The word is ${this.props.data.word.article}
            ${this.props.data.word.german} and it means
            ${this.props.data.word.english}`
          : `WRONG! The word is ${this.props.data.word.article} 
            ${this.props.data.word.german} 
            and it means ${this.props.data.word.english}`}
        <div className={styles.nextWord} onClick={this.onClick}>
          Next Word
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state: { core: CoreState }): Data => ({
  word: state.core.word,
  guessed: state.core.guessed
});

const mapDispatchToProps = {
  getWord
};

const mergeProps = (data: Data, actions: Actions): Props => ({
  data,
  actions
});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
  mergeProps
)(ResultPage);
