/**
 * Created by PBorisov on 28.03.2017.
 */

import React, { Component }      from 'react';
import ReactDOM                  from 'react-dom';
import { Provider }              from 'react-redux';
import $                         from 'jquery';
import moment                    from 'moment';
import { appRequests, appStart } from './actions/common_utils.js';
import FilterModel               from './stores/FilterModel.js';
import MembersListView           from './components/MembersListView.jsx';
import MDLPStore                 from './stores/MDLPStore.js';
import MDLPContainer             from './containers/MDLPContainer.js';
import FilteredListActions       from './actions/FilteredListActions.js';
import FilteredListReducer       from './reducers/FilteredListReducer.js';
import VMapConstants             from './components/CommonVMap/VMapRussia.js';

function main (appInfo, userInfo) {
    //userInfo = (userInfo instanceof Array) ? (userInfo[0] || null) : (userInfo || null);
    userInfo = userInfo || {};
    //const regions    = VMapConstants.regions.map((r, i) => { return { id : r.key, title : r.value }; });
    const regionsMap = VMapConstants.regions.reduce((h, item) => {
        h[item.key] = { 'title' : item.value };
        return h;
    }, {});
    /* Create filter model object */
    const filterModel = new FilterModel({
        userId : userInfo.id,
        name   : 'members_list',
        types  : { EGR : appRequests.GET_MEMBERS_LIST, RAFP : appRequests.GET_MEMBERS_LIST },
        type   : 'EGR',
        params : [
            { filterTypes : [ 'EGR' ] , type : 'combo', name : 'federal_code', title : 'Субъект РФ'   },
            { filterTypes : [ 'EGR' ] , type : 'text' , name : 'INN'         , title : 'ИНН'          },
            { filterTypes : [ 'EGR' ] , type : 'text' , name : 'nameEGR'     , title : 'Наименование' },
            { filterTypes : [ 'EGR' ] , type : 'text' , name : 'KPP'         , title : 'КПП'          },
            { filterTypes : [ 'EGR' ] , type : 'text' , name : 'OGRN'        , title : 'ОГРН'         },
            { filterTypes : [ 'RAFP' ], type : 'text' , name : 'nameRAFP'    , title : 'Наименование' },
            { filterTypes : [ 'RAFP' ], type : 'text' , name : 'ITIN'        , title : 'ITIN'         }
        ]
    });
    /* Create store object */
    const store = MDLPStore(FilteredListReducer, {
        events : { filter : filterModel.state('editval'), list : [], total : 0, lazyMode : true, request : false }
    });
    /* Create view container object */
    const View = MDLPContainer('events', FilteredListActions(filterModel), MembersListView);
    /* Grid table columns */
    const columns = {
        EGR  : [
            { title : 'Наименование организации', width : '50%', minWidth : '200px', key : 'name'         },
            { title : 'ИНН'                     , width : '10%', minWidth : '115px', key : 'INN'          },
            { title : 'КПП'                     , width : '10%', minWidth : '115px', key : 'KPP'          },
            { title : 'ОГРН'                    , width : '10%', minWidth : '115px', key : 'OGRN'         },
            { title : 'Владелец сертификата'    , width : '20%', minWidth : '200px', key : 'certOwnerFIO' }
        ],
        RAFP : [
            { title : 'Наименование организации', width : '70%', minWidth : '600px', key : 'name' },
            { title : 'ITIN'                    , width : '30%', minWidth : '400px', key : 'ITIN' }
        ]
    };
    /* Render ! */
    ReactDOM.render(
        <Provider store={store}><View columns={ columns }/></Provider>,
        document.getElementById('react-container')
    );
}

appStart([], main);
