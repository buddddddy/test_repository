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
import ReestrGS1View             from './components/ReestrGS1View.jsx';
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
        name   : 'reestr_gs1',
        rest   : appRequests.GET_REESTR_GS1,
        params : [
            { type : 'text', name : 'PROD_COVER_GTIN'  , title : 'GTIN'                      },
            { type : 'text', name : 'TR_PARTNER_NAME'  , title : 'Наименование держателя РУ' },
            { type : 'text', name : 'PROD_NAME'        , title : 'Бренд'           },
            { type : 'text', name : 'GS1_MEMBER_GLN'   , title : 'Код производителя'         },
            { type : 'text', name : 'WEB_90000175'     , title : 'Номер РУ'                  },
            { type : 'date', name : 'WEB_90001589_from', title : 'с'                         },
            { type : 'date', name : 'WEB_90001589_to'  , title : 'по'                        }
        ]
    });
    /* Create store object */
    const store = MDLPStore(FilteredListReducer, {
        events : { filter : filterModel.state('editval'), list : [], total : 0, lazyMode : true, request : false, itemCard : {} }
    });
    /* Create actions object */
    const filterActions = FilteredListActions(filterModel);
    /* Create view container object */
    const View = MDLPContainer('events', { ...filterActions, ...ItemCardActions }, ReestrGS1View);
    /* Grid table columns */
    const columns = [
        {title : 'GTIN'                            , width : '10%', minWidth : '140px', key : 'PROD_COVER_GTIN'},
        {title : 'Наименование держателя РУ'       , width : '30%', minWidth : '200px', key : 'TR_PARTNER_NAME'},
        {title : 'Наименование ЛП'                 , width : '30%', minWidth : '100px', key : 'WEB_90000164'},
        {title : 'Бренд'                           , width : '30%', minWidth : '100px', key : 'PROD_NAME'},
        {title : 'Код производителя'               , width : '10%', minWidth : '115px', key : 'GS1_MEMBER_GLN'},
        {title : 'Номер РУ'                        , width : '10%', minWidth : '200px', key : 'WEB_90000175'},
        {title : 'Дата государственной регистрации', width : '10%', minWidth : '110px', rnd : (row) => {
            const key = 'WEB_90001589';
            const str = ((row[key] instanceof Object) && row[key].hasOwnProperty('$date'))
                ? row[key]['$date'] : row[key];
            const d = !!str && moment(str);
            return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : str;
        }}
    ];
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View columns={ columns }/></Provider>,
        document.getElementById('react-container')
    );
}

appStart([], main);
