/**
 * Created by PBorisov on 17.03.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import { Provider }              from 'react-redux';
import $                         from 'jquery';
import moment                    from 'moment';
import { appRequests, appStart } from './actions/common_utils.js';
import FilterModel               from './stores/FilterModel.js';
import EventsListView            from './components/EventsListView.jsx';
import MDLPStore                 from './stores/MDLPStore.js';
import MDLPContainer             from './containers/MDLPContainer.js';
import FilteredListActions       from './actions/FilteredListActions.js';
import FilteredListReducer       from './reducers/FilteredListReducer.js';

function main (appInfo, userInfo, typesList) {
    userInfo  = userInfo  || {};
    typesList = typesList || [];
    const types = typesList.reduce((h, item) => {
        h[item.id] = { 'title' : item.title, 'class' : item.class, 'state' : item.state };
        return h;
    }, {});
    /* Create filter model object */
    const filterModel = new FilterModel({
        userId : userInfo.id,
        name   : 'events_list',
        rest   : appRequests.GET_EVENTS_LIST,
        params : [
            { type : 'text', name : 'memberINN' , title : 'ИНН'          },
            { type : 'text', name : 'memberName', title : 'Наименование' },
            { type : 'date', name : 'dateFrom'  , title : 'с'            },
            { type : 'date', name : 'dateTo'    , title : 'по'           }
        ].concat(
            typesList.map((item) => {
                return { type : 'check', name : 'types', id : item.id, title : item.title };
            })
        )
    });
    /* Create store object */
    const store = MDLPStore(FilteredListReducer, {
        events : { filter : filterModel.state('editval'), list : [], total : 0, lazyMode : true, request : false }
    });
    /* Create view container object */
    const View = MDLPContainer('events', FilteredListActions(filterModel), EventsListView);
    /* Grid table columns */
    const columns = [
        {title : 'Тип события'           , width : '35%', minWidth : '120px', rnd : (row) => {
            return types.hasOwnProperty(row.eventTypeId) ? types[row.eventTypeId].title : row.eventTypeId;
        }},
        {title : 'ИНН участника'         , width : '15%', minWidth : '115px', key : 'memberFromINN'},
        {title : 'Наименование участника', width : '35%', minWidth : '120px', key : 'memberFromName'},
        {title : 'Дата и время'          , width : '15%', minWidth : '100px', rnd : (row) => {
            const d = !!row.dateTime && moment(row.dateTime);
            return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : row.dateTime;
        }}
        //{title : 'Вложение'              , width : '85px', rnd : (row) => {
        //    return ( <a className="mdlp-doc_download_link" href={ row.inside }></a> );
        //}, align : 'center'}
    ];
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View columns={ columns }/></Provider>,
        document.getElementById('react-container')
    );
}

appStart([ appRequests.GET_EVENTS_TYPES() ], main);
