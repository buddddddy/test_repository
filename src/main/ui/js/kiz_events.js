/**
 * Created by PBorisov on 27.03.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import * as queryString          from 'query-string';
import $                         from 'jquery';
import moment                    from 'moment';
import { appRequests, appStart } from './actions/common_utils.js';
import KiZEventsView             from './components/KiZEventsView.jsx';

const kizId = queryString.parse(location.search).kizId;

function main (appInfo, userInfo, typesList, eventsInfo, kizInfo) {
    userInfo   = userInfo   || {};
    typesList  = typesList  || [];
    eventsInfo = $.extend({ list : [] }, (eventsInfo || {}));
    kizInfo    = kizInfo    || {};
    $('#mdlp-user-info').attr('href', userInfo.link).text(userInfo.name);
    const types = typesList.reduce((h, item) => {
        h[item.id] = { 'title' : item.title, 'class' : item.class, 'state' : item.state };
        return h;
    }, {});
    /* Grid table columns */
    const columns = [
        {title : 'Тип события'   , width : '50%', minWidth : '250px', rnd : (row) => {
            const cancelled = !!row.cancellationInfo.cancelled   ? 'cancelled'    : 'active';
            return (
                <div className={`mdlp-kiz_events-${cancelled}`}>
                    {types.hasOwnProperty(row.eventTypeId) ? types[row.eventTypeId].title : row.eventTypeId}
                </div>
            )
        }},
        {title : 'Дата и время'  , width : '25%', minWidth : '100px', rnd : (row) => {
            let d = !!row.dateTime && moment(row.dateTime);
            const cancelled = !!row.cancellationInfo.cancelled   ? 'cancelled'    : 'active';
            return (
                <div className={`mdlp-kiz_events-${cancelled}`}>
                    {(!!d && d.isValid()) ? d.format('DD.MM.YYYY') : row.dateTime}
                </div>
            )
        }},
        {title : 'ИНН участников', width : '25%', minWidth : '115px', rnd : (row) => {
            const fromColor = !!row.memberToINN   ? 'red'    : 'green';
            const toColor   = !!row.memberFromINN ? 'yellow' : 'green';
            const cancelled = !!row.cancellationInfo.cancelled   ? 'cancelled'    : 'active';
            return (
                <div className={`mdlp-kiz_events-${cancelled}`}>
                    { !!row.memberFromINN && <span className={`mdlp-kiz_events-moving from ${fromColor}`}>{row.memberFromINN}</span> }
                    { !!row.memberToINN   && <span className={`mdlp-kiz_events-moving to ${toColor}`}>{row.memberToINN}</span> }
                </div>
            );
        }}
        //{title : 'Вложение'      , width : '85px', rnd : (row) => {
        //    return (
        //        <a className="mdlp-doc_download_link" href={ row.inside }></a>
        //    );
        //}, align : 'center'}
    ];
    ReactDOM.render(
        <KiZEventsView kizInfo={ kizInfo } columns={ columns } list={ eventsInfo.list } />,
        document.getElementById('react-container')
    );
}

appStart([
    appRequests.GET_EVENTS_TYPES(),
    appRequests.GET_EVENTS_LIST({ kizId, pageSize : 1000 }),
    appRequests.GET_KIZ_SHORT_INFO({ kizId })
], main);
