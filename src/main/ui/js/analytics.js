/**
 * Created by PBorisov on 13.04.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import { Provider }              from 'react-redux';
import $                         from 'jquery';
import { appRequests, appStart } from './actions/common_utils.js';
import RegionsAnalyticsModel     from './stores/RegionsAnalyticsModel.js';
import AnalyticsView             from './components/AnalyticsView.jsx';
import MDLPStore                 from './stores/MDLPStore.js';
import MDLPContainer             from './containers/MDLPContainer.js';
import RegionsAnalyticsActions   from './actions/RegionsAnalyticsActions.js';
import RegionsAnalyticsReducer   from './reducers/RegionsAnalyticsReducer.js';

function main (appInfo, userInfo, analyticsCommonInfo) {
    userInfo = userInfo  || {};
    /* Create regions analytics model object */
    const model = new RegionsAnalyticsModel({
        userId : userInfo.id,
        rest   : appRequests.GET_REGION_ANALYTICS
    });
    /* Create store object */
    const store = MDLPStore(RegionsAnalyticsReducer, {
        regionAnalytics : { changeId : 1 }
    });
    /* Create view container object */
    const View = MDLPContainer('regionAnalytics', RegionsAnalyticsActions(model), AnalyticsView);
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View model={ model } analyticsCommonInfo={ analyticsCommonInfo }/></Provider>,
        document.getElementById('react-container')
    );
}

appStart([ appRequests.GET_REGION_ANALYTICS({ code : '00' }) ], main);
