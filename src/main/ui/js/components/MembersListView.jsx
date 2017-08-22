/**
 * Created by PBorisov on 28.03.2017.
 */

import React, { PropTypes, Component }   from 'react';
import $                                 from 'jquery';
import moment                            from 'moment';
import { appMenu, IsDebugMode, IsTestMode } from '../actions/common_utils.js';
import { AppMainMenu } from './CommonMainMenu.jsx';
import CommonFilters, { CommonFilterBlock } from './CommonFilters.jsx';
import CommonCollapsePanel               from './CommonCollapsePanel.jsx';
import { CommonCheckBox, CommonText, CommonDate, CommonComboBox, CommonSwitchButton } from './CommonControl.jsx';
import CommonPreloader                   from './CommonPreloader.jsx';
import CommonTableGrid                   from './CommonTableGrid.jsx';
import CommonPagination                  from './CommonPagination.jsx';
import VMapConstants                     from './CommonVMap/VMapRussia.js';

const regions = VMapConstants.regions.map((r, i) => { return { id : r.key, title : r.value }; });

export default class MembersListView extends Component {
    constructor (props) {
        super(props);
        this.handleFilterTypeSwitch = this.handleFilterTypeSwitch.bind(this);
        this.handleRowClick         = this.handleRowClick.bind(this);
    }
    componentWillMount () {
        this.handleFilterTypeSwitch({value : 'EGR'});
    }
    handleFilterTypeSwitch (newFilterType) {
        this.props.actions.refresh(newFilterType.value);
    }
    handleRowClick (e, row) {
        const isdebug        = IsDebugMode ? '&debug=1' : '';
        const istest         = IsTestMode  ? '&test=1'  : '';
        window.location.href = `member_card.html?producerInn=${row.INN}${isdebug}${istest}`;
    }
    render () {
        const { filter, list, total, lazyMode, request } = this.props.events;
        const { apply, refresh, setFilterParam, toPage } = this.props.actions;
        return (
            <div className="mdlp-main-view-container">
                <CommonPreloader visible={request}/>
                <AppMainMenu menuKey="MEMBERS_LIST" breadcrumbs={[{title : 'Список участников'}]} />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">{
                        !!filter.filterType && (
                            <CommonFilters hasApply={filter.isChanged} onApply={apply}>
                                <CommonFilterBlock active={ filter.filterType === 'EGR' }>
                                    <CommonCollapsePanel title={filter.EGR_federal_code.title} defaultExpanded={ !!filter.EGR_federal_code.value }>
                                        <CommonComboBox id={filter.EGR_federal_code.id}
                                                        name={filter.EGR_federal_code.name}
                                                        title=""
                                                        value={filter.EGR_federal_code.value}
                                                        filterTypes={filter.EGR_federal_code.filterTypes}
                                                        options={ regions }
                                                        onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                    <CommonCollapsePanel title={filter.EGR_nameEGR.title}>
                                        <CommonText id={filter.EGR_nameEGR.id}
                                                    name={filter.EGR_nameEGR.name}
                                                    title=""
                                                    value={filter.EGR_nameEGR.value}
                                                    filterTypes={filter.EGR_nameEGR.filterTypes}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                    <CommonCollapsePanel title={filter.EGR_INN.title}>
                                        <CommonText id={filter.EGR_INN.id}
                                                    name={filter.EGR_INN.name}
                                                    title=""
                                                    value={filter.EGR_INN.value}
                                                    filterTypes={filter.EGR_INN.filterTypes}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                    <CommonCollapsePanel title={filter.EGR_KPP.title}>
                                        <CommonText mask="9{0,100}"
                                                    id={filter.EGR_KPP.id}
                                                    name={filter.EGR_KPP.name}
                                                    title=""
                                                    value={filter.EGR_KPP.value}
                                                    filterTypes={filter.EGR_KPP.filterTypes}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                    <CommonCollapsePanel title={filter.EGR_OGRN.title}>
                                        <CommonText mask="9{0,100}"
                                                    id={filter.EGR_OGRN.id}
                                                    name={filter.EGR_OGRN.name}
                                                    title=""
                                                    value={filter.EGR_OGRN.value}
                                                    filterTypes={filter.EGR_OGRN.filterTypes}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                </CommonFilterBlock>

                                <CommonFilterBlock active={ filter.filterType === 'RAFP' }>
                                    <CommonCollapsePanel title={filter.RAFP_nameRAFP.title}>
                                        <CommonText id={filter.RAFP_nameRAFP.id}
                                                    name={filter.RAFP_nameRAFP.name}
                                                    title=""
                                                    value={filter.RAFP_nameRAFP.value}
                                                    filterTypes={filter.RAFP_nameRAFP.filterTypes}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                    <CommonCollapsePanel title={filter.RAFP_ITIN.title}>
                                        <CommonText id={filter.RAFP_ITIN.id}
                                                    name={filter.RAFP_ITIN.name}
                                                    title=""
                                                    value={filter.RAFP_ITIN.value}
                                                    filterTypes={filter.RAFP_ITIN.filterTypes}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                </CommonFilterBlock>
                            </CommonFilters>
                        )
                    }</div>
                    <div className="mdlp-main-view-workspace-area">

                        <CommonPagination place="above" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                          totalItemsCount={total} lazyMode={ lazyMode }
                                          pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                        <CommonTableGrid columns={ this.props.columns[filter.filterType] } data={ list }
                                         onRowClick={ this.handleRowClick }/>
                        <CommonPagination place="under" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                          totalItemsCount={total} lazyMode={ lazyMode }
                                          pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                    </div>
                </div>

            </div>
        );
    }
}
