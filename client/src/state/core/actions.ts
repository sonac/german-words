import * as utils from "../utils";
import { Word } from "./types";
import { WordData, ErrorData } from "state/types";

export const getWord = utils.createActionCreator("GET_WORD");

export const getWordSuccess = utils.createActionCreator(
  "GET_WORD_SUCCESS",
  (wordData: Word): WordData => ({
    wordData
  })
);

export const getWordError = utils.createActionCreator(
  "GET_WORD_ERROR",
  (error: string): ErrorData => ({
    error
  })
);
