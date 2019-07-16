import * as React from "react";
import { Component } from "react";
import { connect } from "react-redux";
import { Word, CoreState } from "state/core/types";
import { BasicActionCreator, GuessActionCreator } from "state/types";
import { guess, getWord } from "state/core/actions";

const styles = require("./styles.css");

interface Data {
  isLoading: boolean;
  word: Word;
}

interface Actions {
  guess: GuessActionCreator;
  getWord: BasicActionCreator;
}

interface Props {
  data: Data;
  actions: Actions;
}

type State = {};

class WordPage extends Component<Props, State> {
  componentDidMount() {
    this.props.actions.getWord();
  }

  onClick = (article: string) => {
    this.props.actions.guess(article === this.props.data.word.article);
  };

  render() {
    if (this.props.data.isLoading) {
      return <div className={styles.body}>...loading</div>;
    }
    return (
      <div className={styles.body}>
        <div className={styles.word}>{this.props.data.word.german}</div>
        <div className={styles.answer} onClick={_ => this.onClick("der")}>
          der
        </div>
        <div className={styles.answer} onClick={_ => this.onClick("die")}>
          die
        </div>
        <div className={styles.answer} onClick={_ => this.onClick("das")}>
          das
        </div>
      </div>
    );
  }
}

const mapStateToProps = (state: { core: CoreState }): Data => ({
  isLoading: state.core.isLoading,
  word: state.core.word
});

const mapDispatchToProps = {
  guess,
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
)(WordPage);
