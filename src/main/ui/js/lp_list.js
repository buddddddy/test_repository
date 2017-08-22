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
import LPListView                from './components/LPListView.jsx';
import MDLPStore                 from './stores/MDLPStore.js';
import MDLPContainer             from './containers/MDLPContainer.js';
import FilteredListActions       from './actions/FilteredListActions.js';
import FilteredListReducer       from './reducers/FilteredListReducer.js';

function main (appInfo, userInfo, forms) {
    //userInfo = (userInfo instanceof Array) ? (userInfo[0] || null) : (userInfo || null);
    //forms    = (forms    instanceof Array) ? (forms[0]    || null) : (forms    || null);
    userInfo = userInfo || {};
    forms    = forms    || [];
    const hForms = forms.reduce((h, item) => {
        h[item.id] = { 'title' : item.title };
        return h;
    }, {});
    /* Create filter model object */
    const filterModel = new FilterModel({
        userId : userInfo.id,
        name   : 'lp_list',
        rest   : appRequests.GET_LP_LIST,
        params : [
            { type : 'text', name : 'GTIN'        , title : 'GTIN'                                      },
            { type : 'text', name : 'name'        , title : 'Наименование ЛП'                           },
            { type : 'text', name : 'producerInn' , title : 'ИНН производителя / импортера'             },
            { type : 'text', name : 'regOwnerName', title : 'Наименование держателя рег. удостоверения' },
            { type : 'text', name : 'maxPrice'    , title : 'Предельная цена'                           },
            { type : 'date', name : 'expDateFrom' , title : 'с'                                         },
            { type : 'date', name : 'expDateTo'   , title : 'по'                                        }
        ].concat(
            forms.map((item) => {
                return { type : 'check', name : 'forms', id : item.id, title : item.title };
            })
        )
    });
    /* Create store object */
    const store = MDLPStore(FilteredListReducer, {
        events : { filter : filterModel.state('editval'), list : [], total : 0, lazyMode : true, request : false }
    });
    /* Create view container object */
    const View = MDLPContainer('events', FilteredListActions(filterModel), LPListView);
    /* Grid table columns */
    const columns = [
        {title : 'GTIN'                         , width : '10%', minWidth : '80px', key : 'GTIN'},
        {title : 'Торговое наименование'        , width : '10%', minWidth : '80px', key : 'name'},
        {title : 'ИНН производителя / импортера', width : '10%', minWidth : '115px', key : 'producerINN'},
        {title : 'Лек. форма'          , width : '10%', minWidth : '80px', rnd : (row) => {
            return hForms.hasOwnProperty(row.form) ? hForms[row.form].title : row.form;
        }},
        {title : 'Наименование упаковщика в потребительскую упаковку', width : '20%', minWidth : '100px', key : 'regOwnerName'},
        {title : 'Регистрационный номер'                             , width : '10%', minWidth : '50px', key : 'regNumber'},
        {title : 'Дата государственной регистрации'                  , width : '5%', minWidth : '80px', rnd : (row) => {
            let d = !!row.regDate && moment(row.regDate);
            return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : row.regDate;
        }},
        {title : 'Срок действия РУ'                                  , width : '5%', minWidth : '80px', rnd : (row) => {
            if (row.regExpirationDateIsUnlimited) return 'бессрочный';
            let d = !!row.regExpirationDate && moment(row.regExpirationDate);
            return (!!d && d.isValid()) ? d.format('DD.MM.YYYY') : row.regExpirationDate;
        }},
        {title : 'Наличие в ЖНВЛП'              , width : '1%', minWidth : '5px', align : 'center', rnd : (row) => {
            return <span className={`mdlp-bool ${row.gnvlp ? 'mdlp-bool-true' : 'mdlp-bool-false'}`}></span>;
        }},
        {title : 'Предельная цена'              , width : '5%', minWidth : '100px', key : 'maxPrice'}
    ];
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View columns={ columns }/></Provider>,
        document.getElementById('react-container')
    );
}

appStart([ appRequests.GET_LP_FORMS_LIST() ], main);
