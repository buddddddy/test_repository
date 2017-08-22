/**
 * Created by PBorisov on 09.06.2017.
 */

import { combineReducers } from 'redux';

export default function StubReducer (initialState) {
    function stub (state = initialState, action = { type : null }) { return state; }
    return combineReducers({ stub });
}
