/**
 * Created by PBorisov on 19.04.2017.
 */

import { Ajax } from './common_utils.js';

export default function RegionsAnalyticsActions (model) {
    const type = 'MDLP_REGION_ANALYTICS';
    function showRegionFullInfo (item) {
        return (dispatcher, getState) => {
            const p = { type, payload : { changeId : ++getState().regionAnalytics.changeId } };
            if (!item.isEmpty()) {
                dispatcher(p);
            } else {
                // Number(item.key).toString()
                Ajax(model.rest({ code : item.key }), true).done((data) => {
                    item.setData(data);
                    window.setTimeout(() => {
                        dispatcher(p);
                    }, 500);
                }).fail((error) => {
                    item.setError(error);
                    window.setTimeout(() => {
                        dispatcher(p);
                    }, 500);
                });
            }
        };
    }

    return { showRegionFullInfo };
}
