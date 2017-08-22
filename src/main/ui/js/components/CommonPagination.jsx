/**
 * Created by PBorisov on 20.03.2017.
 */

import React, { Component } from 'react';
import Pagination from 'react-js-pagination';

export default class CommonPagination extends Component {
    render () {
        return (
            <div className={`mdlp-common-pagination ${this.props.place}`}>
                <div className="mdlp-common-pagination-box">
                    <Pagination
                        firstPageText={<span className="mdlp-common-pagination-first_page"></span>}
                        prevPageText={<span className="mdlp-common-pagination-prev_page"></span>}
                        nextPageText={<span className="mdlp-common-pagination-next_page"></span>}
                        lastPageText={<span className="mdlp-common-pagination-last_page"></span>}
                        onChange={this.props.onChange}
                        activePage={this.props.activePage}
                        itemsCountPerPage={this.props.itemsCountPerPage}
                        totalItemsCount={this.props.totalItemsCount}
                        pageRangeDisplayed={ this.props.lazyMode ? 1 : this.props.pageRangeDisplayed}/>
                    <a className="mdlp-common-pagination-refresh" href="#" onClick={this.props.onRefresh}></a>
                </div>
                { !this.props.lazyMode && (
                    <span className="mdlp-common-pagination-total">{ this.props.totalItemsCount }</span>
                ) }
            </div>
        );
    }
}
