/**
 * Created by PBorisov on 17.03.2017.
 */

import { combineReducers } from 'redux';
import $                   from 'jquery';

export default function FilteredListReducer (initialState) {
    function events (state = initialState, action = { type : null }) {
        return (action.type != 'DO_NOTHING') ? $.extend({}, state, action.payload) : state;
    }
    return combineReducers({ events });
}
