/**
 * Created by PBorisov on 24.03.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import { Provider }              from 'react-redux';
import * as queryString          from 'query-string';
import $                         from 'jquery';
import moment                    from 'moment';
import { appRequests, appStart } from './actions/common_utils.js';
import KiZInfoView               from './components/KiZInfoView.jsx';

const kizId = queryString.parse(location.search).kizId;

function main (appInfo, userInfo, kizInfo) {
    //userInfo = !!userInfo && userInfo[0] || null;
    //kizInfo  = !!kizInfo  && kizInfo[0]  || null;
    userInfo = userInfo || {};
    kizInfo  = kizInfo  || {};
    if (!!Object.keys(kizInfo).length) {
        kizInfo.id   = kizId;
        kizInfo.name = kizId;
    }
    $('#mdlp-user-info').attr('href', userInfo.link).text(userInfo.name);
    ReactDOM.render(
        <KiZInfoView kizInfo={kizInfo}/>,
        document.getElementById('react-container')
    );
}

appStart([ appRequests.GET_KIZ_FULL_INFO({ kizId }) ], main);
