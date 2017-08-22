/**
 * Created by PBorisov on 24.03.2017.
 */

import React, { PropTypes, Component } from 'react';
import $ from 'jquery';
import moment from 'moment';
import { appMenu, IsDebugMode, IsTestMode }       from '../actions/common_utils.js';
import { AppMainMenu }                            from './CommonMainMenu.jsx';
import CommonFilters                              from './CommonFilters.jsx';
import CommonCollapsePanel                        from './CommonCollapsePanel.jsx';
import { CommonCheckBox, CommonText, CommonDate } from './CommonControl.jsx';
import CommonPreloader                            from './CommonPreloader.jsx';
import CommonTableGrid                            from './CommonTableGrid.jsx';
import CommonPagination                           from './CommonPagination.jsx';

export default class KiZListView extends Component {
    constructor (props) {
        super(props);
        this.handleRowClick = this.handleRowClick.bind(this);
    }
    handleRowClick (e, row) {
        const isdebug        = IsDebugMode ? '&debug=1' : '';
        const istest         = IsTestMode  ? '&test=1'  : '';
        window.location.href = `kiz_info.html?kizId=${row.id}${isdebug}${istest}`;
    }
    componentWillMount () {
        this.props.actions.refresh();
    }
    render () {
        const { filter, list, total, lazyMode, request } = this.props.events;
        const { apply, refresh, setFilterParam, toPage } = this.props.actions;
        const minDate                                    = new Date(2017, 0, 1, 0, 0);
        const maxDate                                    = new Date();
        return (
            <div className="mdlp-main-view-container">
                <CommonPreloader visible={request}/>
                <AppMainMenu menuKey="KIZ_LIST" breadcrumbs={[{title : 'Список КиЗ'}]}/>
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <CommonFilters hasApply={filter.isChanged} onApply={apply}>
                            <CommonCollapsePanel title="SGTIN">
                                <CommonText mask="#{27}"
                                            id={filter.SGTIN.id}
                                            name={filter.SGTIN.name}
                                            title=""
                                            value={filter.SGTIN.value}
                                            onChange={setFilterParam}/>
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="Номер серии">
                                <CommonText id={filter.seriesNumber.id}
                                            name={filter.seriesNumber.name}
                                            title=""
                                            value={filter.seriesNumber.value}
                                            onChange={setFilterParam}/>
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="ИНН текущего владельца">
                                <CommonText mask="9{0,10}"
                                            id={filter.ownerInn.id}
                                            name={filter.ownerInn.name}
                                            title=""
                                            value={filter.ownerInn.value}
                                            onChange={setFilterParam}/>
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="GTIN">
                                <CommonText mask="#{14}"
                                            id={filter.GTIN.id}
                                            name={filter.GTIN.name}
                                            title=""
                                            value={filter.GTIN.value}
                                            onChange={setFilterParam}/>
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="SSCC">
                                <CommonText mask="*{18}"
                                            id={filter.SSCC.id}
                                            name={filter.SSCC.name}
                                            title=""
                                            value={filter.SSCC.value}
                                            onChange={setFilterParam}/>
                                </CommonCollapsePanel>
                            <CommonCollapsePanel title="Наименование товара">
                                <CommonText id={filter.name.id}
                                            name={filter.name.name}
                                            title=""
                                            value={filter.name.value}
                                            onChange={setFilterParam}/>
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="Статус">
                                <div className="mdlp-common-filters-scrollable">
                                    { filter.statuses.map((item, index) => {
                                        return (
                                            <CommonCheckBox key={index}
                                                            id={item.id}
                                                            name={item.name}
                                                            title={item.title}
                                                            value={item.value}
                                                            onChange={setFilterParam} />
                                        );
                                    }) }
                                </div>
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="Дата ввода в гр. оборот">
                                <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                            minDate={minDate}
                                            maxDate={!!filter.releaseDateTo.value
                                                ? new Date(filter.releaseDateTo.value.valueOf()) : maxDate}
                                            id={filter.releaseDateFrom.id}
                                            name={filter.releaseDateFrom.name}
                                            title={filter.releaseDateFrom.title}
                                            value={filter.releaseDateFrom.value}
                                            onChange={setFilterParam} />
                                <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                            minDate={!!filter.releaseDateFrom.value
                                                ? new Date(filter.releaseDateFrom.value.valueOf()) : minDate}
                                            maxDate={maxDate}
                                            id={filter.releaseDateTo.id}
                                            name={filter.releaseDateTo.name}
                                            title={filter.releaseDateTo.title}
                                            value={filter.releaseDateTo.value}
                                            onChange={setFilterParam} />
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="Дата последней операции">
                                <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                            minDate={minDate}
                                            maxDate={!!filter.lastDateTo.value
                                                ? new Date(filter.lastDateTo.value.valueOf()) : maxDate}
                                            id={filter.lastDateFrom.id}
                                            name={filter.lastDateFrom.name}
                                            title={filter.lastDateFrom.title}
                                            value={filter.lastDateFrom.value}
                                            onChange={setFilterParam} />
                                <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                            minDate={!!filter.lastDateFrom.value
                                                ? new Date(filter.lastDateFrom.value.valueOf()) : minDate}
                                            maxDate={maxDate}
                                            id={filter.lastDateTo.id}
                                            name={filter.lastDateTo.name}
                                            title={filter.lastDateTo.title}
                                            value={filter.lastDateTo.value}
                                            onChange={setFilterParam} />
                            </CommonCollapsePanel>
                        </CommonFilters>
                    </div>
                    <div className="mdlp-main-view-workspace-area">
                        <CommonPagination place="above" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                          totalItemsCount={total} lazyMode={ lazyMode }
                                          pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                        <CommonTableGrid columns={ this.props.columns } data={ list } onRowClick={ this.handleRowClick } />
                        <CommonPagination place="under" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                          totalItemsCount={total} lazyMode={ lazyMode }
                                          pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                    </div>
                </div>
            </div>
        );
    }
}
