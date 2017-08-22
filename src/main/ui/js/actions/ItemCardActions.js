/**
 * Created by PBorisov on 16.06.2017.
 */

import { Ajax } from './common_utils.js';
import moment   from 'moment';

const type = 'MDLP_ITEM_CARD';

function itemCardRequestData (rest) {
    return function (dispatcher, getState) {
        dispatcher({ type, payload : { request : true } });
        Ajax(rest).done((resp) => {
            dispatcher({ type, payload : { request : false, itemCard : resp } });
        }).fail((error) => {
            dispatcher({ type, payload : { request : false, error } });
        });
    };
}

export default { itemCardRequestData };
