/**
 * Created by PBorisov on 16.06.2017.
 */

import React, { PropTypes, Component }            from 'react';
import $                                          from 'jquery';
import moment                                     from 'moment';
import { appMenu, appRequests }                   from '../actions/common_utils.js';
import { AppMainMenu, ReestrSubMenu }             from './CommonMainMenu.jsx';
import CommonFilters                              from './CommonFilters.jsx';
import CommonCollapsePanel                        from './CommonCollapsePanel.jsx';
import CommonPreloader                            from './CommonPreloader.jsx';
import { CommonComboBox, CommonText, CommonDate } from './CommonControl.jsx';
import CommonTableGrid                            from './CommonTableGrid.jsx';
import CommonPagination                           from './CommonPagination.jsx';
import CardItemInfoBlock                          from './CommonInfoBlock.jsx';

export default class ReestrLicensesView extends Component {
    constructor (props) {
        super(props);
        this.state = { currentPrms : null };
    }
    componentWillMount () {
        this.props.actions.refresh();
    }
    render () {
        const { filter, list, total, lazyMode, request, itemCard } = this.props.events;
        const { apply, refresh, setFilterParam, toPage, itemCardRequestData } = this.props.actions;
        const minDate = new Date(2009, 0, 1, 0, 0);
        const maxDate = new Date();
        const breadcrumbs = !this.state.currentPrms
            ? [ {title : 'Реестр лицензий на производство ЛП'} ]
            : [ {title : 'Реестр лицензий на производство ЛП', onClick : () => { this.setState({ currentPrms : null }) }}, {title : `${this.state.currentPrms.L_NUM}`} ];
        return (
            <div className="mdlp-main-view-container">
                <CommonPreloader visible={request}/>
                <AppMainMenu menuKey="REESTRS" breadcrumbs={ breadcrumbs } />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <ReestrSubMenu menuKey="REESTR_LICENSES" />
                        {
                            !this.state.currentPrms && (
                                <CommonFilters hasApply={filter.isChanged} onApply={apply}>



                                    <CommonCollapsePanel title="Номер лицензии">
                                        <CommonText id={filter.L_NUM.id}
                                                    name={filter.L_NUM.name}
                                                    title={filter.L_NUM.title}
                                                    value={filter.L_NUM.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Срок действия лицензии">
                                        <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                                    minDate={minDate}
                                                    maxDate={!!filter.END_DATE.value
                                                ? new Date(filter.END_DATE.value.valueOf()) : maxDate}
                                                    id={filter.START_DATE.id}
                                                    name={filter.START_DATE.name}
                                                    title={filter.START_DATE.title}
                                                    value={filter.START_DATE.value}
                                                    onChange={setFilterParam} />
                                        <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                                    minDate={!!filter.START_DATE.value
                                                ? new Date(filter.START_DATE.value.valueOf()) : minDate}
                                                    maxDate={maxDate}
                                                    id={filter.END_DATE.id}
                                                    name={filter.END_DATE.name}
                                                    title={filter.END_DATE.title}
                                                    value={filter.END_DATE.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title={filter.L_STATUS.title}>
                                        <CommonComboBox id={filter.L_STATUS.id}
                                                        name={filter.L_STATUS.name}
                                                        title=""
                                                        value={filter.L_STATUS.value}
                                                        options={ [{id : '0', title : 'Действующая'}, {id : '1', title : 'Недействующая'}] }
                                                        onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="ИНН">
                                        <CommonText mask="#{0,12}"
                                            id={filter.INN.id}
                                            name={filter.INN.name}
                                            title={filter.INN.title}
                                            value={filter.INN.value}
                                            onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Название организации">
                                        <CommonText id={filter.ORG_NAME.id}
                                            name={filter.ORG_NAME.name}
                                            title={filter.ORG_NAME.title}
                                            value={filter.ORG_NAME.value}
                                            onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                </CommonFilters>
                            )
                        }
                    </div>
                    {
                        !this.state.currentPrms ? (
                            <div className="mdlp-main-view-workspace-area">
                                <CommonPagination place="above" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                                  totalItemsCount={total} lazyMode={ lazyMode }
                                                  pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                                <CommonTableGrid columns={ this.props.columns } data={ list }
                                                 onRowClick={(e, row, i) => {
                                                    this.setState({currentPrms : { INN : row.INN, L_NUM : row.L_NUM } });
                                                 }} />
                                <CommonPagination place="under" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                                  totalItemsCount={total} lazyMode={ lazyMode }
                                                  pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                            </div>
                        ) : (
                            <div className="mdlp-main-view-workspace-area">
                                <ReestrLicensesViewItem prms={ this.state.currentPrms } getData={ itemCardRequestData } fullInfo={ itemCard } />
                            </div>
                        )
                    }
                </div>
            </div>
        );
    }
}


class ReestrLicensesViewItem extends Component {
    componentWillMount () {
        !!this.props.getData && this.props.getData( appRequests.GET_REESTR_ITEM_LICENSES(this.props.prms) );
    }
    render () {
        const { fullInfo } = this.props;
        return !!Object.keys( fullInfo || {} ).length && (
                <div>
                    <h2 className="mdlp-inner">{ fullInfo.PROD_SELL_NAME }</h2>
                    <CardItemInfoBlock>{[
                        { title : 'ИНН', value : fullInfo['INN'] },
                        { title : 'Название организации', value : fullInfo['ORG_NAME'] },
                        { title : 'Номер лицензии', value : fullInfo['L_NUM'] },
                        { title : 'Дата начала действия лицензии', value : fullInfo['START_DATE'] },
                        { title : 'Дата окончания действия лицензии', value : fullInfo['END_DATE'] },
                        { title : 'Дата последнего изменения лицензии', value : fullInfo['CHANGE_DATE'] },
                        { title : 'Статус лицензии', value : (fullInfo['L_STATUS'] == '0') ? 'Действующая' : (fullInfo['L_STATUS'] == '1') ? 'Недействующая' : fullInfo['L_STATUS'] },
                        { title : 'ОГРН', value : fullInfo['OGRN'] },
                        { title : 'Адрес места осуществления деятельности', value : fullInfo['ADDRESS'].PLACE },
                        { title : 'Перечень работ/услуг согласно лицензии', value : String(fullInfo['WORK_LIST']).split('.') },
                        { title : 'Перечень работ/услуг согласно лицензии 2', value : String(fullInfo['WORK_LIST_2']).split('.') }
                    ]}</CardItemInfoBlock>
                </div>
            );
    }
}
