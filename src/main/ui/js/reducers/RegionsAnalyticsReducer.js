/**
 * Created by PBorisov on 18.04.2017.
 */

import { combineReducers } from 'redux';
import $                   from 'jquery';

export default function RegionsAnalyticsReducer (initialState) {
    function regionAnalytics (state = initialState, action = { type : null }) {
        const upd = action.payload || {};
        return (action.type === 'MDLP_REGION_ANALYTICS') ? { ...state, ...upd } : state;
    }
    return combineReducers({ regionAnalytics });
}
