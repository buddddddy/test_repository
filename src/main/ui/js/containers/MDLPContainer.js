/**
 * Created by PBorisov on 19.04.2017.
 */

import { bindActionCreators } from 'redux';
import { connect }            from 'react-redux';

export default function MDLPContainer (key, actions, view) {
    return connect(function (state) {
        const st = {};
        st[key] = state[key];
        return st;
    }, function (dispatch) {
        return { actions : bindActionCreators(actions , dispatch ) };
    })(view);
}
