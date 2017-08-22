/**
 * Created by PBorisov on 09.06.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import { Provider }              from 'react-redux';
import $                         from 'jquery';
import moment                    from 'moment';
import * as queryString          from 'query-string';
import { appRequests, appStart } from './actions/common_utils.js';
import FilterModel               from './stores/FilterModel.js';
import MemberCardView            from './components/MemberCardView.jsx';
import MDLPStore                 from './stores/MDLPStore.js';
import MDLPContainer             from './containers/MDLPContainer.js';
import FilteredListActions       from './actions/FilteredListActions.js';
import FilteredListReducer       from './reducers/FilteredListReducer.js';

const producerInn = queryString.parse(location.search).producerInn;

function main (appInfo, userInfo, statuses, producerInfo, lpForms, regAnalInfo) {
    //userInfo = (userInfo instanceof Array) ? (userInfo[0] || null) : (userInfo || null);
    //statuses = (statuses instanceof Array) ? (statuses[0] || null) : (statuses || null);
    userInfo = userInfo || {};
    statuses = statuses || [];
    const statusMap = statuses.reduce((h, item) => {
        h[item.id] = { 'title' : item.title };
        return h;
    }, {});
    const hForms = lpForms.reduce((h, item) => {
        h[item.id] = { 'title' : item.title };
        return h;
    }, {});
    /* Create filter model object */
    const filterModel = new FilterModel({
        userId : userInfo.id,
        name   : 'member_kiz_list',
        types  : {
            FACTORY : appRequests.GET_KIZ_LIST,
            FILIALS : appRequests.GET_MEMBER_FILIALS_LIST,
            SAFE_WAREHOUSES: appRequests.GET_SAFE_WAREHOUSES,
            LP      : appRequests.GET_LP_LIST
        },
        type   : 'FACTORY',
        pageSize : 10,
        params : [
            { filterTypes : ['FILIALS', 'LP', 'SAFE_WAREHOUSES'], type : 'text', name : 'producerInn' , title : 'ИНН производителя / импортера', value : producerInn },
            { filterTypes : ['FACTORY'], type : 'text', name : 'ownerInn'    , title : 'ИНН производителя / импортера', value : producerInn },
            { filterTypes : ['FACTORY'], type : 'text', name : 'SGTIN'       , title : 'SGTIN'                         },
            { filterTypes : ['FACTORY'], type : 'text', name : 'seriesNumber', title : 'Номер серии'                   },
            { filterTypes : ['FACTORY'], type : 'text', name : 'GTIN'        , title : 'GTIN'                          },
            { filterTypes : ['FACTORY'], type : 'text', name : 'name'        , title : 'Наименование товара'           },
            { filterTypes : ['FACTORY'], type : 'date', name : 'regDateFrom' , title : 'с'                             },
            { filterTypes : ['FACTORY'], type : 'date', name : 'regDateTo'   , title : 'по'                            },

            { filterTypes : [ 'FILIALS' ], type : 'text', name : 'PLACE', title : 'Адреса мест осуществления деятельности' },

            { filterTypes : [ 'SAFE_WAREHOUSES' ], type : 'text', name : 'PLACE', title : 'Адреса мест ответственного хранения' },

            { filterTypes : [ 'LP' ], type : 'text', name : 'GTIN'        , title : 'GTIN'                                      },
            { filterTypes : [ 'LP' ], type : 'text', name : 'name'        , title : 'Наименование ЛП'                           },
            { filterTypes : [ 'LP' ], type : 'text', name : 'regOwnerName', title : 'Наименование держателя рег. удостоверения' },
            { filterTypes : [ 'LP' ], type : 'text', name : 'maxPrice'    , title : 'Предельная цена'                           },
            { filterTypes : [ 'LP' ], type : 'date', name : 'expDateFrom' , title : 'с'                                         },
            { filterTypes : [ 'LP' ], type : 'date', name : 'expDateTo'   , title : 'по'                                        }
        ].concat(
            statuses.map((item) => {
                return { filterTypes : ['FACTORY'], type : 'check', name : 'statuses', id : item.id, title : item.title };
            })
        ).concat(
            lpForms.map((item) => {
                return { filterTypes : [ 'LP' ], type : 'check', name : 'forms', id : item.id, title : item.title };
            })
        )
    });
    /* Create store object */
    const store = MDLPStore(FilteredListReducer, {
        events : { filter : filterModel.state('editval'), list : [], total : 0, lazyMode : true, request : false }
    });
    /* Create view container object */
    const View = MDLPContainer('events', FilteredListActions(filterModel), MemberCardView);
    /* Grid table columns */
    const columns = {
        FACTORY : [
            {title : 'SGTIN'                        , width : '30%', minWidth : '215px', key : 'SGTIN'},
            {title : 'Номер серии'                  , width :  '5%', minWidth :  '50px', key : 'seriesNumber'},
            {title : 'ИНН текущего владельца'       , width : '10%', minWidth : '115px', key : 'ownerINN'},
            {title : 'GTIN'                         , width : '10%', minWidth : '140px', key : 'GTIN'},
            {title : 'Наименование товара'          , width : '30%', minWidth : '200px', key : 'name'},
            {title : 'Статус'                       , width :  '5%', minWidth : '100px', rnd : (row) => {
                return statusMap.hasOwnProperty(row.statusId) ? statusMap[row.statusId].title : row.statusId;
            }},
            {title : 'Дата регистрации'             , width : '10%', minWidth : '100px', rnd : (row) => {
                let d = !!row.registrationDate && moment(row.registrationDate);
                return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : row.registrationDate;
            }}
        ],
        FILIALS : [
            { title : 'Адреса мест осуществления деятельности', width : '100%', minWidth : '400px', key : 'PLACE' }
        ],
        SAFE_WAREHOUSES: [
            {title: 'Адреса мест ответственного хранения', width: '100%', minWidth: '400px', key: 'PLACE'}
        ],
        LP : [
            {title : 'GTIN'                         , width : '10%', minWidth : '140px', key : 'GTIN'},
            {title : 'Наименование ЛП'              , width : '10%', minWidth : '100px', key : 'name'},
            {title : 'ИНН производителя / импортера', width : '10%', minWidth : '115px', key : 'producerINN'},
            {title : 'Лекарственная форма'          , width : '10%', minWidth : '100px', rnd : (row) => {
                return hForms.hasOwnProperty(row.form) ? hForms[row.form].title : row.form;
            }},
            {title : 'Наименование держателя РУ'    , width : '40%', minWidth : '200px', key : 'regOwnerName'},
            {title : 'Срок действия РУ'             , width : '10%', minWidth : '110px', rnd : (row) => {
                if (row.regExpirationDateIsUnlimited) return 'бессрочный';
                let d = !!row.regExpirationDate && moment(row.regExpirationDate);
                return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : row.regExpirationDate;
            }},
            {title : 'Предельная цена'              , width : '10%', minWidth : '100px', key : 'maxPrice'}
        ]
    };
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View columns={ columns } member={ producerInfo } regAnalInfo={ regAnalInfo }/></Provider>,
        document.getElementById('react-container')
    );
}

const rests = [
    appRequests.GET_KIZ_STATS_LIST(),
    appRequests.GET_MEMBER_BYINN_INFO({ producerInn }),
    appRequests.GET_LP_FORMS_LIST()
];
(producerInn == '4428000115') && rests.push( appRequests.GET_REGION_ANALYTICS({ code : '44' }) );
appStart(rests, main);
