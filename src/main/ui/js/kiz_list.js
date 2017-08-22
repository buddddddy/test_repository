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
import FilterModel               from './stores/FilterModel.js';
import KiZListView               from './components/KiZListView.jsx';
import MDLPStore                 from './stores/MDLPStore.js';
import MDLPContainer             from './containers/MDLPContainer.js';
import FilteredListActions       from './actions/FilteredListActions.js';
import FilteredListReducer       from './reducers/FilteredListReducer.js';

function main (appInfo, userInfo, statuses) {
    //userInfo = (userInfo instanceof Array) ? (userInfo[0] || null) : (userInfo || null);
    //statuses = (statuses instanceof Array) ? (statuses[0] || null) : (statuses || null);
    userInfo = userInfo || {};
    statuses = statuses || [];
    const statusMap = statuses.reduce((h, item) => {
        h[item.id] = { 'title' : item.title };
        return h;
    }, {});
    /* Create filter model object */
    const filterModel = new FilterModel({
        userId : userInfo.id,
        name   : 'kiz_list',
        rest   : appRequests.GET_KIZ_LIST,
        pageSize: 10,
        params : [
            { type : 'text', name : 'SGTIN'             , title : 'SGTIN'                         },
            { type : 'text', name : 'seriesNumber'      , title : 'Номер серии'                   },
            { type : 'text', name : 'ownerInn'          , title : 'ИНН текущего владельца'        },
            { type : 'text', name : 'GTIN'              , title : 'GTIN'                          },
            { type : 'text', name : 'SSCC'              , title : 'SSCC'                          },
            { type : 'text', name : 'name'              , title : 'Наименование товара'           },
            { type : 'date', name : 'releaseDateFrom'   , title : 'с'                             },
            { type : 'date', name : 'releaseDateTo'     , title : 'по'                            },
            { type : 'date', name : 'lastDateFrom'      , title : 'с'                             },
            { type : 'date', name : 'lastDateTo'        , title : 'по'                            }
        ].concat(
            statuses.map((item) => {
                return { type : 'check', name : 'statuses', id : item.id, title : item.title };
            })
        )
    });
    /* Create store object */
    const store = MDLPStore(FilteredListReducer, {
        events : { filter : filterModel.state('editval'), list : [], total : 0, lazyMode : true, request : false  }
    });
    /* Create view container object */
    const View = MDLPContainer('events', FilteredListActions(filterModel), KiZListView);
    /* Grid table columns */
    const columns = [
        {title : 'SGTIN'                            , width : '30%', minWidth : '215px', key : 'SGTIN'},
        {title : 'Номер серии'                      , width :  '5%', minWidth : '50px', key : 'seriesNumber'},
        {title : 'ИНН текущего владельца'           , width : '10%', minWidth : '90px', key : 'ownerINN'},
        {title : 'Наименование текущего владельца'  , width : '10%', minWidth : '90px', key : 'ownerTitle'},
        {title : 'Местонахождение ЛП'               , width : '10%', minWidth : '105px', key : 'federalSubjectName'},
        {title : 'GTIN'                             , width : '10%', minWidth : '105px', key : 'GTIN'},
        {title : 'Наименование товара'              , width : '20%', minWidth : '158px', key : 'name'},
        {title : 'Статус'                           , width :  '5%', minWidth : '75px', rnd : (row) => {
            return statusMap.hasOwnProperty(row.statusId) ? statusMap[row.statusId].title : row.statusId;
        }},
        {title : 'Дата ввода в гр. оборот'             , width : '10%', minWidth : '65px', rnd : (row) => {
            let d = !!row.releaseOperationDate && moment(row.releaseOperationDate);
            return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : row.releaseOperationDate;
        }},
        {title : 'Дата последней операции'             , width : '10%', minWidth : '65px', rnd : (row) => {
            let d = !!row.lastOperationDate && moment(row.lastOperationDate);
            return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : row.lastOperationDate;
        }},
        {title : 'Третичная упаковка'           , width :  '10%', minWidth : '75px', key : 'sscc'}
    ];
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View columns={ columns }/></Provider>,
        document.getElementById('react-container')
    );
}

appStart([ appRequests.GET_KIZ_STATS_LIST() ], main);
