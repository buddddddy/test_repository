/**
 * Created by PBorisov on 25.03.2017.
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

export default class LPListView extends Component {
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
                <AppMainMenu menuKey="LP_LIST" breadcrumbs={[{title : 'Зарегистрированные ЛП'}]} />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <CommonFilters hasApply={filter.isChanged} onApply={apply}>
                            <CommonCollapsePanel title="GTIN">
                                <CommonText mask="#{14}"
                                            id={filter.GTIN.id}
                                            name={filter.GTIN.name}
                                            title=""
                                            value={filter.GTIN.value}
                                            onChange={setFilterParam} />
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="Торговое наименование">
                                <CommonText id={filter.name.id}
                                            name={filter.name.name}
                                            title=""
                                            value={filter.name.value}
                                            onChange={setFilterParam} />
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="ИНН производителя / импортера">
                                <CommonText mask="9{0,10}"
                                            id={filter.producerInn.id}
                                            name={filter.producerInn.name}
                                            title=""
                                            value={filter.producerInn.value}
                                            onChange={setFilterParam} />
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="Наименование упаковщика в потребительскую упаковку">
                                <CommonText id={filter.regOwnerName.id}
                                            name={filter.regOwnerName.name}
                                            title=""
                                            value={filter.regOwnerName.value}
                                            onChange={setFilterParam} />
                            </CommonCollapsePanel>
                            <CommonCollapsePanel title="Предельная цена">
                                <CommonText mask="9{0,7}"
                                            id={filter.maxPrice.id}
                                            name={filter.maxPrice.name}
                                            title=""
                                            value={filter.maxPrice.value}
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
