/**
 * Created by PBorisov on 17.03.2017.
 */

import React, { PropTypes, Component }            from 'react';
import $                                          from 'jquery';
import moment                                     from 'moment';
import { appMenu }                                from '../actions/common_utils.js';
import { AppMainMenu }                            from './CommonMainMenu.jsx';
import CommonFilters                              from './CommonFilters.jsx';
import CommonCollapsePanel                        from './CommonCollapsePanel.jsx';
import CommonPreloader                            from './CommonPreloader.jsx';
import { CommonCheckBox, CommonText, CommonDate } from './CommonControl.jsx';
import CommonTableGrid                            from './CommonTableGrid.jsx';
import CommonPagination                           from './CommonPagination.jsx';

export default class EventsListView extends Component {
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
                <AppMainMenu menuKey="EVENTS_LIST" breadcrumbs={[{title : 'Журнал событий'}]} />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <CommonFilters hasApply={filter.isChanged} onApply={apply}>
                            <CommonCollapsePanel title="Тип события">
                                <div className="mdlp-common-filters-scrollable">
                                    { filter.types.map((item, index) => {
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
                            <CommonCollapsePanel title="Участник системы">
                                <CommonText labelPosition="left" mask="9{0,10}"
                                            id={filter.memberINN.id}
                                            name={filter.memberINN.name}
                                            title={filter.memberINN.title}
                                            value={filter.memberINN.value}
                                            onChange={setFilterParam} />
                                <CommonText labelPosition="up"
                                            id={filter.memberName.id}
                                            name={filter.memberName.name}
                                            title={filter.memberName.title}
                                            value={filter.memberName.value}
                                            onChange={setFilterParam} />
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="Дата события">
                                <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                            minDate={minDate}
                                            maxDate={!!filter.dateTo.value
                                                ? new Date(filter.dateTo.value.valueOf()) : maxDate}
                                            id={filter.dateFrom.id}
                                            name={filter.dateFrom.name}
                                            title={filter.dateFrom.title}
                                            value={filter.dateFrom.value}
                                            onChange={setFilterParam} />
                                <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                            minDate={!!filter.dateFrom.value
                                                ? new Date(filter.dateFrom.value.valueOf()) : minDate}
                                            maxDate={maxDate}
                                            id={filter.dateTo.id}
                                            name={filter.dateTo.name}
                                            title={filter.dateTo.title}
                                            value={filter.dateTo.value}
                                            onChange={setFilterParam} />
                            </CommonCollapsePanel>
                        </CommonFilters>
                    </div>
                    <div className="mdlp-main-view-workspace-area">
                        <CommonPagination place="above" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                          totalItemsCount={total} lazyMode={ lazyMode }
                                          pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                        <CommonTableGrid columns={ this.props.columns } data={ list } />
                        <CommonPagination place="under" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                          totalItemsCount={total} lazyMode={ lazyMode }
                                          pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                    </div>
                </div>
            </div>
        );
    }
}
