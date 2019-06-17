import { Action } from "redux";
import { createReducerFromDescriptor } from "../utils";
import { CoreState as State } from "./types";
import { getWord, getWordSuccess, getWordError } from "./actions";
import { WordAction, ErrorAction } from "state/types";

const initState: State = {
  isLoading: true,
  error: null,
  word: null
};

export default createReducerFromDescriptor(
  {
    [getWord.type]: (state: State, _: Action): State => ({
      ...state,
      isLoading: true
    }),
    [getWordSuccess.type]: (state: State, action: WordAction): State => ({
      ...state,
      isLoading: false,
      word: action.wordData
    }),
    [getWordError.type]: (state: State, action: ErrorAction): State => ({
      ...state,
      isLoading: false,
      error: action.error
    })
  },
  initState
);
