/**
 * Created by PBorisov on 16.06.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import { Provider }              from 'react-redux';
import * as queryString          from 'query-string';
import $                         from 'jquery';
import moment                    from 'moment';
import { appRequests, appStart } from './actions/common_utils.js';
import FilterModel               from './stores/FilterModel.js';
import ReestrESKLPView           from './components/ReestrESKLPView.jsx';
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
        rest   : appRequests.GET_REESTR_ESKLP,
        params : [
            { type : 'text' , name : 'REG_ID'         , title : 'Номер РУ'                  },
            { type : 'text' , name : 'REG_HOLDER'     , title : 'Наименование держателя РУ' },
            { type : 'text' , name : 'PROD_NAME'      , title : 'Наименование ЛП'           },
            { type : 'text' , name : 'PROD_SELL_NAME' , title : 'Торговое наименование ЛП'  },
            { type : 'text' , name : 'REG_HOLDER_CODE', title : 'Код держателя РУ'          },
            { type : 'date' , name : 'REG_DATE'       , title : 'с'                         },
            { type : 'date' , name : 'REG_END_DATE'   , title : 'по'                        },
            { type : 'combo', name : 'REG_STATUS'     , title : 'Статус действия РУ'        }
        ]
    });
    /* Create store object */
    const store = MDLPStore(FilteredListReducer, {
        events : { filter : filterModel.state('editval'), list : [], total : 0, lazyMode : true, request : false, itemCard : {} }
    });
    /* Create actions object */
    const filterActions = FilteredListActions(filterModel);
    /* Create view container object */
    const View = MDLPContainer('events', { ...filterActions, ...ItemCardActions }, ReestrESKLPView);
    /* Grid table columns */
    const columns = [
        {title : 'Номер РУ'                        , width : '8%', minWidth : '80px', key : 'reg_id'},
        {title : 'Наименование держателя РУ'       , width : '8%', minWidth : '100px', key : 'REG_HOLDER'},
        {title : 'Наименование ЛП'                 , width : '8%', minWidth : '100px', key : 'PROD_NAME'},
        {title : 'Торговое наименование ЛП'        , width : '8%', minWidth : '100px', key : 'PROD_SELL_NAME'},
        {title : 'Адрес'                , width : '8%', minWidth : '115px', key : 'ADDRESS'},
        {title : 'Масса/объем в первичной уп-ке', width : '8%', minWidth: '80px', key : 'PROD_PACK_1_SIZE'},
        {title : 'Количество ед. изм. дозировки ЛП', width : '8%', minWidth : '100px', key : 'PROD_D_NAME'},
        {title : 'Вторичная (потреб.) упаковка', width : '8%', minWidth : '80px', key : 'PROD_PACK_2_NAME'},
        {title : 'Дата гос. регистрации', width : '8%', minWidth : '110px', rnd : (row) => {
            const key = 'REG_DATE';
            const str = ((row[key] instanceof Object) && row[key].hasOwnProperty('$date'))
                ? row[key]['$date'] : row[key];
            const d = !!str && moment(str);
            return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : str;
        }},
        {title : 'Статус действия РУ', width : '8%', minWidth : '110px', rnd : (row) => {
            return (row.REG_STATUS == '0') ? 'Действующее' : (row.REG_STATUS == '1') ? 'Недействующее' : row.REG_STATUS;
        }}
    ];
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View columns={ columns }/></Provider>,
        document.getElementById('react-container')
    );
}

appStart([], main);
