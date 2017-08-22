/**
 * Created by PBorisov on 09.06.2017.
 */

import { Ajax } from './common_utils.js';

export default function StubActions (model) {
    const type = 'MDLP_REGION_ANALYTICS';
    function stub (item) {
        return { type, payload : {} };
    }

    return { stub };
}
