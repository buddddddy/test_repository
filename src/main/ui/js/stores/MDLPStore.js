/**
 * Created by PBorisov on 19.04.2017.
 */

import { createStore, applyMiddleware } from 'redux';
import createLogger                     from 'redux-logger';
import thunk                            from 'redux-thunk';

export default function MDLPStore (reducer, initialState) {
    return createStore(
        reducer(initialState),
        initialState,
        applyMiddleware(thunk, createLogger())
    );
}
