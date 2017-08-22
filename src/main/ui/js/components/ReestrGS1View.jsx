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
import { CommonCheckBox, CommonText, CommonDate } from './CommonControl.jsx';
import CommonTableGrid                            from './CommonTableGrid.jsx';
import CommonPagination                           from './CommonPagination.jsx';
import CardItemInfoBlock                          from './CommonInfoBlock.jsx';

export default class ReestrGS1View extends Component {
    constructor (props) {
        super(props);
        this.state = { currentGTIN : null };
    }
    componentWillMount () {
        this.props.actions.refresh();
    }
    render () {
        const { filter, list, total, lazyMode, request, itemCard } = this.props.events;
        const { apply, refresh, setFilterParam, toPage, itemCardRequestData } = this.props.actions;
        const minDate = new Date(2017, 0, 1, 0, 0);
        const maxDate = new Date();
        const breadcrumbs = !this.state.currentGTIN
            ? [ {title : 'Реестр GS1'} ]
            : [ {title : 'Реестр GS1', onClick : () => { this.setState({ currentGTIN : null }) }}, {title : `${this.state.currentGTIN}`} ];
        return (
            <div className="mdlp-main-view-container">
                <CommonPreloader visible={request}/>
                <AppMainMenu menuKey="REESTRS" breadcrumbs={ breadcrumbs } />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <ReestrSubMenu menuKey="REESTR_GS1" />
                        {
                            !this.state.currentGTIN && (
                                <CommonFilters hasApply={filter.isChanged} onApply={apply}>

                                    <CommonCollapsePanel title="GTIN">
                                        <CommonText mask="#{0,14}"
                                                    id={filter.PROD_COVER_GTIN.id}
                                                    name={filter.PROD_COVER_GTIN.name}
                                                    title={filter.PROD_COVER_GTIN.title}
                                                    value={filter.PROD_COVER_GTIN.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Наименование держателя РУ">
                                        <CommonText id={filter.TR_PARTNER_NAME.id}
                                                    name={filter.TR_PARTNER_NAME.name}
                                                    title={filter.TR_PARTNER_NAME.title}
                                                    value={filter.TR_PARTNER_NAME.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Бренд">
                                        <CommonText id={filter.PROD_NAME.id}
                                                    name={filter.PROD_NAME.name}
                                                    title={filter.PROD_NAME.title}
                                                    value={filter.PROD_NAME.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Код производителя">
                                        <CommonText id={filter.GS1_MEMBER_GLN.id}
                                                    name={filter.GS1_MEMBER_GLN.name}
                                                    title={filter.GS1_MEMBER_GLN.title}
                                                    value={filter.GS1_MEMBER_GLN.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Номер РУ">
                                        <CommonText id={filter.WEB_90000175.id}
                                                    name={filter.WEB_90000175.name}
                                                    title={filter.WEB_90000175.title}
                                                    value={filter.WEB_90000175.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>

                                    <CommonCollapsePanel title="Дата государственной регистрации">
                                        <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                                    minDate={minDate}
                                                    maxDate={!!filter.WEB_90001589_to.value
                                                ? new Date(filter.WEB_90001589_to.value.valueOf()) : maxDate}
                                                    id={filter.WEB_90001589_from.id}
                                                    name={filter.WEB_90001589_from.name}
                                                    title={filter.WEB_90001589_from.title}
                                                    value={filter.WEB_90001589_from.value}
                                                    onChange={setFilterParam} />
                                        <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                                    minDate={!!filter.WEB_90001589_from.value
                                                ? new Date(filter.WEB_90001589_from.value.valueOf()) : minDate}
                                                    maxDate={maxDate}
                                                    id={filter.WEB_90001589_to.id}
                                                    name={filter.WEB_90001589_to.name}
                                                    title={filter.WEB_90001589_to.title}
                                                    value={filter.WEB_90001589_to.value}
                                                    onChange={setFilterParam} />
                                    </CommonCollapsePanel>
                                </CommonFilters>
                            )
                        }
                    </div>
                    {
                        !this.state.currentGTIN ? (
                            <div className="mdlp-main-view-workspace-area">
                                <CommonPagination place="above" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                                  totalItemsCount={total} lazyMode={ lazyMode }
                                                  pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                                <CommonTableGrid columns={ this.props.columns } data={ list }
                                                 onRowClick={(e, row, i) => { this.setState({currentGTIN : row.PROD_COVER_GTIN}); }} />
                                <CommonPagination place="under" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                                  totalItemsCount={total} lazyMode={ lazyMode }
                                                  pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                            </div>
                        ) : (
                            <div className="mdlp-main-view-workspace-area">
                                <ReestrGS1ViewItem gtin={ this.state.currentGTIN } getData={ itemCardRequestData } fullInfo={ itemCard } />
                            </div>
                        )
                    }
                </div>
            </div>
        );
    }
}

class ReestrGS1ViewItem extends Component {
    componentWillMount () {
        !!this.props.getData && this.props.getData( appRequests.GET_REESTR_ITEM_GS1({ gtin : this.props.gtin }) );
    }
    render () {
        const { fullInfo } = this.props;
        return !!Object.keys( fullInfo || {} ).length && (
            <div>
                <h2 className="mdlp-inner">{ fullInfo.PROD_NAME }</h2>
                <CardItemInfoBlock title="Данные о лекарственном препарате">{[
                    { title : 'GTIN', value : fullInfo['PROD_COVER_GTIN'] },
                    { title : 'Код производителя', value : fullInfo['GS1_MEMBER_GLN'] },
                    { title : 'Наименование товара на этикетке', value : fullInfo['PROD_DESC'] },
                    { title : 'Полное наименование товара', value : fullInfo['PROD_DESC_FULL'] },
                    { title : 'Бренд (торговая марка)', value : fullInfo['PROD_NAME'] },
                    { title : 'Номер регистрационного удостоверения', value : fullInfo['WEB_90000175'] },
                    { title : 'Дата государственной регистрации', value : fullInfo['WEB_90001589'] },
                    { title : 'Наименование держателя регистрационного удостоверения', value : fullInfo['TR_PARTNER_NAME'] },
                    { title : 'Адрес держателя регистрационного удостоверения', value : fullInfo['TR_PARTNER_ADDR'] },
                    { title : 'ТН ВЭД 1', value : fullInfo['TNVED_1'] },
                    { title : 'ТН ВЭД 2', value : fullInfo['TNVED_2'] },
                    { title : 'ТН ВЭД 3', value : fullInfo['TNVED_3'] },
                    { title : 'ТН ВЭД 4', value : fullInfo['TNVED_4'] },
                    { title : 'ТН ВЭД 5', value : fullInfo['TNVED_5'] },
                    { title : 'Торговое наименование лекарственного препарата', value : fullInfo['WEB_90000164'] },
                    { title : 'Лекарственная форма', value : fullInfo['WEB_90001590'] },
                    { title : 'Количество единиц измерения дозировки лекарственного препарата', value : fullInfo['WEB_90001681'] },
                    { title : 'Тип вторичной (потребительской) упаковки ', value : fullInfo['PROD_COVER_TYPE_DICT'] },
                    { title : 'Материал вторичной (потребительской) упаковки  ', value : fullInfo['PROD_COVER_MATERIAL'] },
                    { title : 'Количество / мера ЛП во вторичной (потребительской) упаковке', value : fullInfo['WEB_90001591'] },
                    { title : 'Количество / мера ЛП во вторичной (потребительской) упаковке - единицы измерения', value : fullInfo['WEB_90001591'] },
                    { title : 'Описание вложенной немаркированнной (первичной) упаковки', value : fullInfo['PROD_COVER_EXT_DESC'] },
                    { title : 'Наименование фасовщика/упаковщика во вторичную (потребительскую) упаковку ', value : fullInfo['TR_PARTNER_NAME_SERVICE_PROVIDER'] },
                    { title : 'Адрес фасовщика/упаковщика во вторичную (потребительскую)  упаковку', value : fullInfo['TR_PARTNER_ADDR_SERVICE_PROVIDER'] },
                    { title : 'Адрес фасовщика/упаковщика во вторичную (потребительскую)  упаковку (по ФИАС для резидентов РФ)', value : fullInfo['???'] }
                ]}</CardItemInfoBlock>
            </div>
        );
    }
}
