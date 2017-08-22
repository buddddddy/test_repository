/**
 * Created by PBorisov on 29.06.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import { Provider }              from 'react-redux';
import * as queryString          from 'query-string';
import $                         from 'jquery';
import moment                    from 'moment';
import { appRequests, appStart } from './actions/common_utils.js';
import FilterModel               from './stores/FilterModel.js';
import ReestrPharmView           from './components/ReestrPharmView.jsx';
import MDLPStore                 from './stores/MDLPStore.js';
import MDLPContainer             from './containers/MDLPContainer.js';
import FilteredListActions       from './actions/FilteredListActions.js';
import ItemCardActions           from './actions/ItemCardActions.js'
import FilteredListReducer       from './reducers/FilteredListReducer.js';

function main (appInfo, userInfo) {
    userInfo = userInfo || {};
    /* Create filter model object */
    const filterModel = new FilterModel({
        userId : userInfo.id,
        name   : 'reestr_esklp',
        rest   : appRequests.GET_REESTR_PHARM,
        params : [
            { type : 'text' , name : 'INN'       , title : 'ИНН'                  },
            { type : 'text' , name : 'ORG_NAME'  , title : 'Название организации' },
            { type : 'text' , name : 'L_NUM'     , title : 'Номер лицензии'       },
            { type : 'date' , name : 'START_DATE', title : 'с'                    },
            { type : 'date' , name : 'END_DATE'  , title : 'по'                   },
            { type : 'combo', name : 'L_STATUS'  , title : 'Статус лицензии'      }
        ]
    });
    /* Create store object */
    const store = MDLPStore(FilteredListReducer, {
        events : { filter : filterModel.state('editval'), list : [], total : 0, lazyMode : true, request : false, itemCard : {} }
    });
    /* Create actions object */
    const filterActions = FilteredListActions(filterModel);
    /* Create view container object */
    const View = MDLPContainer('events', { ...filterActions, ...ItemCardActions }, ReestrPharmView);
    /* Grid table columns */
    const columns = [
        {title : 'Номер лицензии'                  , width : '10%', minWidth : '100px', key : 'L_NUM'},
        {title : 'Дата начала действия лицензии'   , width : '10%', minWidth : '110px', rnd : (row) => {
            const str = ((row.START_DATE instanceof Object) && row.START_DATE.hasOwnProperty('$date'))
                ? row.START_DATE['$date'] : row.START_DATE;
            const d = !!str && moment(str);
            return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : str;
        }},
        {title : 'Дата окончания действия лицензии', width : '10%', minWidth : '110px', rnd : (row) => {
            const str = ((row.END_DATE instanceof Object) && row.END_DATE.hasOwnProperty('$date'))
                ? row.END_DATE['$date'] : row.END_DATE;
            if (!str) return React.createElement('span', {style : {color: '#00FF00'}}, 'бессрочное');
            const d = !!str && moment(str);
            return (!!d && d.isValid())
                ? ((d.valueOf() < moment().valueOf())
                    ? React.createElement('span', {style : {color: '#FF0000'}}, d.format('DD.MM.YYYY'))
                    : d.format('DD.MM.YYYY')
            )
            : str;
        }},
        {title : 'Статус лицензии'                 , width : '10%', minWidth : '100px', rnd : (row) => {
            return (row.L_STATUS == '0') ? 'Действующая' : (row.L_STATUS == '1') ? 'Недействующая' : row.L_STATUS;
        }},
        {title : 'Адрес'                 , width : '20%', minWidth : '100px', rnd : (row) => {
            return (row.ADDRESS || {}).PLACE || '';
        }},
        {title : 'ИНН'                             , width : '10%', minWidth : '140px', key : 'inn'},
        {title : 'Название организации'            , width : '30%', minWidth : '200px', key : 'ORG_NAME'}
    ];
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View columns={ columns }/></Provider>,
        document.getElementById('react-container')
    );
}

appStart([], main);
