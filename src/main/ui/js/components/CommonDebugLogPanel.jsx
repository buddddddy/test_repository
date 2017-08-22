/**
 * Created by PBorisov on 31.03.2017.
 */

import React, { Component }  from 'react';
import $                     from 'jquery';
import moment                from 'moment';
import { appRequests, Ajax } from '../actions/common_utils.js';
import JSONViewer            from '../actions/json_viewer.js';

JSONViewer($);

class CommonDebugLogItem extends Component {
    render () {
        const dt   = !!this.props.createdAt ? moment(this.props.createdAt) : null;
        const stat = (this.props.status < 300) ? 'success' : 'error';
        return (
            <div className="mdlp-common-debug-log-item">
                <p className={`mdlp-common-debug-log-item-title ${stat}`}>
                    <span className="black">{this.props.uri}&nbsp;({this.props.method})</span>,&nbsp;&nbsp;отправлен&nbsp;&nbsp;
                    <span className="black">
                        { (!!dt && dt.isValid()) ? dt.format('DD.MM.YYYY hh:mm:ss') : this.props.createdAt }
                    </span>
                </p>
                <div className="mdlp-common-debug-log-item-message out">
                    <pre ref={(it) => {
                        let str = this.props.outboundMessage, obj = null;
                        try { obj = JSON.parse(str); } catch(e) { obj = null; };
                        $(it).jsonViewer(obj || str);
                    }}></pre>
                </div>
                <div className="mdlp-common-debug-log-item-message in">
                    <pre ref={(it) => {
                        let str = this.props.inboundMessage, obj = null;
                        try { obj = JSON.parse(str); } catch(e) { obj = null; };
                        $(it).jsonViewer(obj || str);
                    }}></pre>
                </div>
                <p className={`mdlp-common-debug-log-item-status ${stat}`}>
                    { this.props.statusDescription }
                </p>
            </div>
        );
    }
}

export default class CommonDebugLogPanel extends Component {
    constructor (props) {
        super(props);
        this.state = { messList : null };
    }
    componentWillMount () {
        var starter, refresh;
        starter = () => {
            !!refresh && window.setTimeout(refresh, 5000);
        };
        refresh = () => {
            Ajax(appRequests.GET_DEBUG_LOG()).done((resp) => {
                this.setState({ messList : resp });
                !!starter && starter();
            }).fail(() => {});
        };
        refresh();
    }
    render () {
        return (
            <div className="mdlp-common-debug-log-panel">
                { !!this.state.messList && this.state.messList.map((item, index) => {
                    return (
                        <CommonDebugLogItem key={index} { ...item } />
                    );
                }) }
            </div>
        );
    }
}
