/**
 * Created by PBorisov on 09.06.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import { Provider }              from 'react-redux';
import $                         from 'jquery';
import moment                    from 'moment';
import { appRequests, appStart } from './actions/common_utils.js';
import StubView                  from './components/StubView.jsx';
import MDLPStore                 from './stores/MDLPStore.js';
import MDLPContainer             from './containers/MDLPContainer.js';
import StubActions               from './actions/StubActions.js';
import StubReducer               from './reducers/StubReducer.js';

function main (appInfo, userInfo) {
    //userInfo = (userInfo instanceof Array) ? (userInfo[0] || null) : (userInfo || null);
    userInfo = userInfo || {};
    /* Create store object */
    const store = MDLPStore(StubReducer, { stub : {} });
    /* Create view container object */
    const View = MDLPContainer('stub', StubActions({}), StubView);
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View /></Provider>,
        document.getElementById('react-container')
    );
}

appStart([], main);
