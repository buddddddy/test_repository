/**
 * Created by PBorisov on 20.03.2017.
 */

import React, { Component } from 'react';

export default class CommonFilters extends Component {
    render () {
        return (
            <div className="mdlp-common-filters">
                <div className="mdlp-common-filters-head">
                    <span className="mdlp-common-filters-head-title mdlp-common-block-title">Фильтры</span>
                    {
                        this.props.hasApply && <a className="mdlp-common-filters-apply" href="#"
                                                  title="Применить фильтры" onClick={this.props.onApply}></a>
                    }
                </div>
                { !!this.props.children && this.props.children }
            </div>
        );
    }
}

export class CommonFilterBlock extends Component {
    render () {
        const display = this.props.active ? 'block' : 'none';
        return this.props.active && ( <div>{ this.props.children }</div> );
    }
}
