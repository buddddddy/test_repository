/**
 * Created by PBorisov on 24.03.2017.
 */

import React, { Component } from 'react';
import moment from 'moment';

export class CommonInfoBlockItem extends Component {
    render () {
        return !this.props.delimiter ? (
            (!!String((this.props.value == null) ? '' : this.props.value).length) && (
                <p className="mdlp-common-info_block-item">
                    <span className="mdlp-common-info_block-item-title">{this.props.title}</span>
                    <span className="mdlp-common-info_block-item-value">{this.props.value}</span>
                </p>
            )
        ) : <br/>;
    }
}

export class CommonInfoBlock extends Component {
    render () {
        return (
            <div className="mdlp-common-info_block">
                <div className="mdlp-common-filters-head">
                    <h3 className="mdlp-common-info_block-head">{this.props.title}</h3>
                    { this.props.children }
                </div>
            </div>
        );
    }
}

export default class CardItemInfoBlock extends Component {
    renderItem (itemProps, index) {
        const title  = itemProps.title;
        const pvalue = itemProps.value;
        const value  = (pvalue == null)
            ? ''
            : (pvalue instanceof Array)
            ? (() => {
                const a = pvalue.filter((e) => !!String(e).trim().length);
                return !!a.length ? (<ul>{ a.map((v, i) => <li key={ i }>{ v }</li>) }</ul>) : '';
            })()
            : (pvalue instanceof Object && pvalue.hasOwnProperty('$date'))
            ? (
                (!!String(pvalue['$date']).match(/^\d\d\d\d\-\d\d\-\d\d/) && moment(pvalue['$date']).isValid())
                    ? moment(pvalue['$date']).format('DD.MM.YYYY') : String(pvalue['$date'])
            )
            : (!!String(pvalue).match(/^\d\d\d\d\-\d\d\-\d\d/) && moment(pvalue).isValid())
            ? moment(pvalue).format('DD.MM.YYYY')
            : String(pvalue);
        return !String(value).length ? null : (
            <div key={ index } className="mdlp-common-info_block-item">
                <div className="mdlp-common-info_block-item-title">{ title }</div>
                <div className="mdlp-common-info_block-item-value">{ value }</div>
            </div>
        );
    }
    render () {
        const children = (this.props.children || [])
            .map( this.renderItem.bind(this) )
            .filter( (item) => { return !!item; } );
        return !!children.length && (
                <div className="mdlp-common-info_block">
                    <div className="mdlp-common-filters-head">
                        { !!this.props.title && (<h3 className="mdlp-common-info_block-head">{this.props.title}</h3>) }
                        { children }
                    </div>
                </div>
            );
    }
}
