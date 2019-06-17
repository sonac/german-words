import { getWord, getWordSuccess, getWordError } from "./actions";
import { Word } from "./types";

export default ({ getState, dispatch }) => next => action => {
  if (action.type === getWord.type) {
    fetch("/word")
      .then(response => response.json())
      .then(
        (word: Word): void => {
          dispatch(getWordSuccess(word));
        }
      )
      .catch(error => {
        console.error(error);
        dispatch(getWordError(error));
      });
  }
  return next(action);
};
