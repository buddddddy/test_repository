/**
 * Created by PBorisov on 20.03.2017.
 */

import React, { Component } from 'react';

export default class CommonTableGrid extends Component {
    constructor (props) {
        super(props);
        this.handleRowClick = this.handleRowClick.bind(this);
    }
    handleRowClick (e, row, index) {
        !!this.props.onRowClick && this.props.onRowClick(e, row, index);
    }
    render () {
        const addonCls = !!this.props.onRowClick ? 'row_clickable' : '';
        return (
            <table className={`mdlp-common-table_grid ${addonCls}`}>
                <colgroup>
                { this.props.columns.map((col, index) => {
                    return <col key={`col_${index}`}
                                style={{ width : col.width, minWidth : col.minWidth }}></col>;
                }) }
                </colgroup>
                <thead>
                    <tr>
                    { this.props.columns.map((col, index) => {
                        return <th key={`th_${index}`} style={{ minWidth : col.minWidth }}>{col.title}</th>;
                    }) }
                    </tr>
                </thead>
                <tbody>
                { this.props.data.map((row, rindex) => {
                    return (
                        <tr key={`tr_${rindex}`} onClick={(e) => {this.handleRowClick(e, row, rindex)}}>
                        { this.props.columns.map((col, cindex) => {
                            const align = col.align || 'left';
                            return (
                                <td key={`td_${rindex}_${cindex}`} style={{ textAlign : align, minWidth : col.minWidth }}>
                                    { !!col.key ? row[col.key] : !!col.rnd ? col.rnd(row) : '' }
                                </td>
                            );
                        }) }
                        </tr>
                    );
                }) }
                </tbody>
            </table>
        );
    }
}
