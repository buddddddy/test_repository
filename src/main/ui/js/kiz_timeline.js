/**
 * Created by PBorisov on 29.03.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import * as queryString          from 'query-string';
import $                         from 'jquery';
import { appRequests, appStart } from './actions/common_utils.js';
import KiZTimelineView           from './components/KiZTimelineView.jsx';

const kizId = queryString.parse(location.search).kizId;

function main (appInfo, userInfo, typesList, eventsInfo, kizInfo) {
    //userInfo   = !!userInfo   && userInfo[0]   || null;
    //typesList  = !!typesList  && typesList[0]  || null;
    //eventsInfo = !!eventsInfo && eventsInfo[0] || null;
    //kizInfo    = !!kizInfo    && kizInfo[0]    || null;
    userInfo   = userInfo   || {};
    typesList  = typesList  || [];
    eventsInfo = $.extend({ list : [] }, (eventsInfo || {}));
    kizInfo    = kizInfo    || {};
    $('#mdlp-user-info').attr('href', userInfo.link).text(userInfo.name);
    const types = typesList.reduce((h, item) => {
        h[item.id] = { 'title' : item.title, 'class' : item.class, 'state' : item.state };
        return h;
    }, {});
    ReactDOM.render(
        <KiZTimelineView kizInfo={ kizInfo } types={ types } list={ eventsInfo.list } />,
        document.getElementById('react-container')
    );
}

appStart([
    appRequests.GET_EVENTS_TYPES(),
    appRequests.GET_EVENTS_LIST({ kizId, pageSize : 1000 }),
    appRequests.GET_KIZ_SHORT_INFO({ kizId })
], main);
