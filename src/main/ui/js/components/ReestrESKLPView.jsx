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

export default class ReestrESKLPView extends Component {
    constructor (props) {
        super(props);
        this.state = { currentID : null };
    }
    componentWillMount () {
        this.props.actions.refresh();
    }
    render () {
        const { filter, list, total, lazyMode, request, itemCard } = this.props.events;
        const { apply, refresh, setFilterParam, toPage, itemCardRequestData } = this.props.actions;
        const minDate = new Date(2017, 0, 1, 0, 0);
        const maxDate = new Date();
        const breadcrumbs = !this.state.currentID
            ? [ {title : 'Реестр ЕСКЛП'} ]
            : [ {title : 'Реестр ЕСКЛП', onClick : () => { this.setState({ currentID : null }) }}, {title : `${this.state.currentID}`} ];
        return (
            <div className="mdlp-main-view-container">
                <CommonPreloader visible={request}/>
                <AppMainMenu menuKey="REESTRS" breadcrumbs={ breadcrumbs } />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <ReestrSubMenu menuKey="REESTR_ESKLP" />
                        {
                            !this.state.currentID && (
                                <CommonFilters hasApply={filter.isChanged} onApply={apply}>

                                    <CommonCollapsePanel title="Наименование держателя РУ">
                                        <CommonText id={filter.REG_HOLDER.id}
                                                    name={filter.REG_HOLDER.name}
                                                    title={filter.REG_HOLDER.title}
                                                    value={filter.REG_HOLDER.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Наименование ЛП">
                                        <CommonText id={filter.PROD_NAME.id}
                                                    name={filter.PROD_NAME.name}
                                                    title={filter.PROD_NAME.title}
                                                    value={filter.PROD_NAME.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Торговое наименование ЛП">
                                        <CommonText id={filter.PROD_SELL_NAME.id}
                                                    name={filter.PROD_SELL_NAME.name}
                                                    title={filter.PROD_SELL_NAME.title}
                                                    value={filter.PROD_SELL_NAME.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Срок действия РУ">
                                        <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                                    minDate={minDate}
                                                    maxDate={!!filter.REG_END_DATE.value
                                                ? new Date(filter.REG_END_DATE.value.valueOf()) : maxDate}
                                                    id={filter.REG_DATE.id}
                                                    name={filter.REG_DATE.name}
                                                    title={filter.REG_DATE.title}
                                                    value={filter.REG_DATE.value}
                                                    onChange={setFilterParam} />
                                        <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                                    minDate={!!filter.REG_DATE.value
                                                ? new Date(filter.REG_DATE.value.valueOf()) : minDate}
                                                    maxDate={maxDate}
                                                    id={filter.REG_END_DATE.id}
                                                    name={filter.REG_END_DATE.name}
                                                    title={filter.REG_END_DATE.title}
                                                    value={filter.REG_END_DATE.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>


                                </CommonFilters>
                            )
                        }
                    </div>
                    {
                        !this.state.currentID ? (
                            <div className="mdlp-main-view-workspace-area">
                                <CommonPagination place="above" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                                  totalItemsCount={total} lazyMode={ lazyMode }
                                                  pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                                <CommonTableGrid columns={ this.props.columns } data={ list }
                                                 onRowClick={(e, row, i) => { this.setState({currentID : row.reg_id, id : row.id}); }} />
                                <CommonPagination place="under" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                                  totalItemsCount={total} lazyMode={ lazyMode }
                                                  pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                            </div>
                        ) : (
                            <div className="mdlp-main-view-workspace-area">
                                <ReestrESKLPViewItem id={ this.state.id } getData={ itemCardRequestData } fullInfo={ itemCard } />
                            </div>
                        )
                    }
                </div>
            </div>
        );
    }
}

class ReestrESKLPViewItem extends Component {
    componentWillMount () {
        !!this.props.getData && this.props.getData( appRequests.GET_REESTR_ITEM_ESKLP({ ID : this.props.id }) );
    }
    render () {
        const { fullInfo } = this.props;
        return !!Object.keys( fullInfo || {} ).length && (
                <div>
                    <h2 className="mdlp-inner">{ fullInfo.PROD_SELL_NAME }</h2>
                    <CardItemInfoBlock title="Данные о лекарственном препарате">{[
                        { title : 'Номер РУ', value : fullInfo['reg_id'] },
                        { title : 'Дата государственной регистрации', value : fullInfo['REG_DATE'] },
                        { title : 'Статус действия РУ', value : (fullInfo['REG_STATUS'] == '0') ? 'Действующее' : (fullInfo['REG_STATUS'] == '1') ? 'Недействующее' : fullInfo['REG_STATUS'] },
                        { title : 'Дата окончания действия РУ', value : fullInfo['REG_END_DATE'] },
                        { title : 'Наименование держателя РУ', value : fullInfo['REG_HOLDER'] },
                        { title : 'Страна регистрации держателя РУ', value : fullInfo['REG_COUNTRY'] },
                        { title : 'Код держателя РУ', value : fullInfo['REG_HOLDER_CODE'] },
                        { title : 'Код ТН ВЭД', value : fullInfo['TN_VED'] },
                        { title : 'Международное непатентованное наименование ЛП', value : fullInfo['PROD_NAME'] },
                        { title : 'Торговое наименование ЛП', value : fullInfo['PROD_SELL_NAME'] },
                        { title : 'Лекарственная форма', value : fullInfo['PROD_FORM_NAME'] },
                        { title : 'Количество единиц измерения дозировки лекарственного препарата', value : fullInfo['PROD_D_NAME'] },
                        { title : 'Первичная упаковка', value : fullInfo['PROD_PACK_1_NAME'] },
                        { title : 'Количество лекарственной формы в первичной упаковке', value : fullInfo['PROD_PACK_1'] },
                        { title : 'Масса/объем в первичной упаковке', value : fullInfo['PROD_PACK_1_SIZE'] },
                        { title : 'Ед. измерения массы/объема в первичной упаковке', value : fullInfo['PROD_PACK_1_ED_NAME'] },
                        { title : 'Вторичная (потребительская) упаковка', value : fullInfo['PROD_PACK_2_NAME'] },
                        { title : 'Количество первичной упаковки в потребительской упаковке', value : fullInfo['PROD_PACK_1_2'] },
                        { title : 'Наименование упаковщика во вторичную/третичную упаковку', value : fullInfo['PACK_2_3_NAME'] },
                        { title : 'Код упаковщика во вторичную/третичную упаковку', value : fullInfo['PACK_2_3_CODE'] },
                        { title : 'Страна регистрации упаковщика во вторичную/третичную упаковку', value : fullInfo['COUNTRY_PACK_2_3'] },
                        { title : 'Адрес фасовщика/упаковщика во вторичную/третичную упаковку ', value : fullInfo['ADDRESS'] },
                        { title : 'Наименование производителя стадии выпускающий контроль качества', value : fullInfo['QA_NAME'] },
                        { title : 'Код налогоплательщика стадии выпускающий контроль качества', value : fullInfo['QA_CODE'] },
                        { title : 'Страна регистрации производителя стадии выпускающий контроль качества', value : fullInfo['QA_COUNTRY'] },
                        { title : 'Адрес стадии выпускающий контроль качества', value : fullInfo['QA_ADDRESS_NAME'] },
                        { title : 'Признак наличия в ЖНВЛП', value : (fullInfo['GNVLP'] == '1') ? 'ДА' : 'НЕТ' },
                        { title : 'Дата регистрации предельной цены', value : fullInfo['MAX_PRICE_DATE'] },
                        { title : 'Предельная зарегистрированная цена (для ЖНВЛП) (руб)', value : fullInfo['MAX_GNVLP'] }
                    ]}</CardItemInfoBlock>
                </div>
            );
    }
}
