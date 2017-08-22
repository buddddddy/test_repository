/**
 * Created by PBorisov on 17.03.2017.
 */

import { Ajax } from './common_utils.js';
import moment   from 'moment';

export default function FilteredListActions (filterModel) {
    const type = 'MDLP_FILTERED_LIST';

    function changeFilterType (newType) {
        filterModel.changeType(newType);
        return refresh();
    }

    function setFilterParam (param) {
        filterModel.setEdit(param);
        return { type, payload : { filter : filterModel.state('editval') } };
    }

    function apply () {
        return function (dispatcher, getState) {
            dispatcher({ type, payload : { request : true } });
            Ajax(filterModel.rest(filterModel.serialize('editval', 1))).done((resp) => {
                filterModel.edit2cur().setNPage(1).save();
                dispatcher({ type, payload : {
                    request : false,
                    filter  : filterModel.state('editval'),
                    list    : resp.list || [],
                    total   : resp.total,
                    lazyMode: !resp.hasOwnProperty('lazyMode') ? true : !!resp.lazyMode
                } });
            }).fail((error) => {
                dispatcher({ type, payload : { request : false } });
            });
        };
    }

    function refresh (newType) {
        const oldType          = !!arguments.length ? filterModel.changeType(newType) : filterModel.type;
        const filterWillChange = (newType !== oldType);
        return function (dispatcher, getState) {
            dispatcher({ type, payload : { request : true } });
            Ajax(filterModel.rest(filterModel.serialize('curval'))).done((resp) => {
                const payload = {
                    request : false,
                    list    : resp.list || [],
                    total   : resp.total,
                    lazyMode: !resp.hasOwnProperty('lazyMode') ? true : !!resp.lazyMode
                };
                if (filterWillChange) payload.filter = filterModel.state('editval');
                dispatcher({ type, payload });
            }).fail((error) => {
                filterWillChange && filterModel.changeType(oldType);
                dispatcher({ type, payload : { request : false } });
            });
        };
    }

    function toPage (nPage) {
        return function (dispatcher, getState) {
            dispatcher({ type, payload : { request : true } });
            Ajax(filterModel.rest(filterModel.serialize('curval', nPage))).done((resp) => {
                filterModel.setNPage(nPage).save();
                dispatcher({ type, payload : {
                    request : false,
                    filter  : filterModel.state('editval'),
                    list    : resp.list || [],
                    total   : resp.total,
                    lazyMode: !resp.hasOwnProperty('lazyMode') ? true : !!resp.lazyMode
                } });
            }).fail((error) => {
                dispatcher({ type, payload : { request : false } });
            });
        };
    }

    return { changeFilterType, setFilterParam, apply, refresh, toPage };
}
