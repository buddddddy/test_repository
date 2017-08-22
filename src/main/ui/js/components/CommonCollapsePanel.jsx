/**
 * Created by PBorisov on 19.03.2017.
 */

import React, { Component } from 'react';

export default class CommonCollapsePanel extends Component {
    constructor (props) {
        super(props);
        this.state = { isCollapsed : !this.props.defaultExpanded };
        this.handleClick = this.handleClick.bind(this);
    }
    handleClick () {
        const newState = !this.state.isCollapsed;
        this.setState({ isCollapsed : newState });
    }
    render () {
        const sttClass = this.state.isCollapsed ? 'collapsed' : '';
        return (
            <div className={`mdlp-common-collapse_panel ${sttClass}`}>
                <div className="mdlp-common-collapse_panel-title" onClick={this.handleClick}>{this.props.title}</div>
                <div className="mdlp-common-collapse_panel-content">{this.props.children}</div>
            </div>
        );
    }
}
