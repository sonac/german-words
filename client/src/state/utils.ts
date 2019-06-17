import { Action } from "redux";
import {
  ReducerDescriptor,
  BasicReducer,
  UntypedActionCreator0,
  UntypedActionCreator1,
  UntypedActionCreator2,
  UntypedActionCreator3,
  TypedFunc,
  ActionCreator0,
  ActionCreator1,
  ActionCreator2,
  ActionCreator3
} from "state/types";

export function createReducerFromDescriptor(
  descriptor: ReducerDescriptor,
  initState: object
): BasicReducer {
  return (state: object = initState, action: Action): object => {
    const actionHandler = descriptor[action.type];
    return actionHandler ? actionHandler(state, action) : state;
  };
}

export function createActionCreator<R extends object>(
  type: string,
  creator?: UntypedActionCreator0<R>
): ActionCreator0<R>;
export function createActionCreator<T1, R extends object>(
  type: string,
  creator?: UntypedActionCreator1<T1, R>
): ActionCreator1<T1, R>;
export function createActionCreator<T1, T2, R extends object>(
  type: string,
  creator?: UntypedActionCreator2<T1, T2, R>
): ActionCreator2<T1, T2, R>;
export function createActionCreator<T1, T2, T3, R extends object>(
  type: string,
  creator?: UntypedActionCreator3<T1, T2, T3, R>
): ActionCreator3<T1, T2, T3, R>;

export function createActionCreator(type, creator) {
  const typedCreator = function(): Action {
    return { ...(creator ? creator.apply(creator, arguments) : {}), type };
  } as TypedFunc;

  typedCreator.type = type;

  return typedCreator;
}
