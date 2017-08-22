/**
 * Created by PBorisov on 09.06.2017.
 */

import React, { PropTypes, Component } from 'react';
import $ from 'jquery';
import moment from 'moment';
import { appMenu, IsDebugMode, IsTestMode }       from '../actions/common_utils.js';
import { AppMainMenu }                            from './CommonMainMenu.jsx';
import CommonFilters, { CommonFilterBlock }       from './CommonFilters.jsx';
import CommonCollapsePanel                        from './CommonCollapsePanel.jsx';
import CommonPreloader                            from './CommonPreloader.jsx';
import { CommonCheckBox, CommonText, CommonDate } from './CommonControl.jsx';
import CommonTableGrid                            from './CommonTableGrid.jsx';
import CommonPagination                           from './CommonPagination.jsx';
import CommonSearchControl                        from './CommonSearchControl.jsx';
import CardItemInfoBlock                          from './CommonInfoBlock.jsx';

class MemberInfoBlock extends Component {
    render () {
        const lpListLink = appMenu.LP_LIST(true).link.split('?');
        lpListLink[1] = (!!lpListLink[1]) ? `${lpListLink[1]}&producerINN=${this.props.member.INN}` : `producerINN=${this.props.member.INN}`;
        const prms = (this.props.member.isResident)
            ? [ { key : 'INN', name : 'ИНН' }, { key : 'KPP', name : 'КПП'}, { key : 'certOwnerFIO', name : 'Руководитель' } ]
            : [ { key : 'INN', name : 'ИТИН' } ];
        return (
            <div className="mdlp-member_info">
                <p className="mdlp-member_info-title">{ this.props.member.name }</p>
                <div className="mdlp-member_info-block">{
                    prms.map((prm, index) => {
                        return (
                            <div key={ index } className="mdlp-member_info-block-item">
                                <span className="mdlp-member_info-block-item-title">{ prm.name }</span>
                                <span className="mdlp-member_info-block-item-value">{ this.props.member[prm.key] }</span>
                            </div>
                        );
                    })
                }
                {/*
                    <div className="mdlp-member_info-block-item">
                        <a className="mdlp-member_info-block-item-link" href={lpListLink.join('?')}>Производимые лекарственные препараты</a>
                    </div>
                */}</div>
            </div>
        );
    }
}

export default class MemberCardView extends Component {
    constructor (props) {
        super(props);
        this.content = null;
        //this.state = { contentCode : 'FACTORY' };
        this.handleFilterTypeSwitch = this.handleFilterTypeSwitch.bind(this);
        this.handleRowClick         = this.handleRowClick.bind(this);
        //this.handleINNChange = this.handleINNChange.bind(this);
    }
    componentWillMount () {
        this.handleFilterTypeSwitch('FACTORY');
    }
    componentWillReceiveProps (newProps) {
        const { filterType } = newProps.events.filter;
        this.content = (filterType !== this.content) ? filterType : this.content;
    }
    handleFilterTypeSwitch (newFilterType) {
        if (newFilterType === 'CHARS') {
            this.content = 'CHARS';
            this.forceUpdate();
        } else {
            this.props.actions.refresh(newFilterType);
        }
    }
    handleRowClick (e, row) {
        const isdebug        = IsDebugMode ? '&debug=1' : '';
        const istest         = IsTestMode  ? '&test=1'  : '';
        if (this.content === 'FACTORY') {
            window.location.href = `kiz_info.html?kizId=${row.id}${isdebug}${istest}`;
        }
    }
    render () {
        const { member }                                 = this.props;
        const { parent }                                 = member;
        const { filter, list, total, lazyMode, request } = this.props.events;
        const { apply, refresh, setFilterParam, toPage } = this.props.actions;
        const minDate                                    = new Date(2017, 0, 1, 0, 0);
        const maxDate                                    = new Date();
        const activeTool = (c) => (c === this.content) ? ' active' : '';
        return !!this.content && (
            <div className="mdlp-main-view-container">
                <CommonPreloader visible={request}/>
                <AppMainMenu menuKey="MEMBERS_LIST" breadcrumbs={[appMenu.MEMBERS_LIST(true), {title : !!this.props.member ? this.props.member.name : ''}]} />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <MemberInfoBlock member={ member } />
                        {
                            !!this.content && (this.content !== 'CHARS') && (
                                <CommonFilters hasApply={filter.isChanged} onApply={apply}>
                                    <CommonFilterBlock active={ this.content === 'FACTORY' }>
                                        <CommonCollapsePanel title="SGTIN">
                                            <CommonText mask="#{0,34}"
                                                        id={filter.FACTORY_SGTIN.id}
                                                        name={filter.FACTORY_SGTIN.name}
                                                        title=""
                                                        value={filter.FACTORY_SGTIN.value}
                                                        filterTypes={filter.FACTORY_SGTIN.filterTypes}
                                                        onChange={setFilterParam}/>
                                        </CommonCollapsePanel>
                                        <CommonCollapsePanel title="Номер серии">
                                            <CommonText id={filter.FACTORY_seriesNumber.id}
                                                        name={filter.FACTORY_seriesNumber.name}
                                                        title=""
                                                        value={filter.FACTORY_seriesNumber.value}
                                                        filterTypes={filter.FACTORY_seriesNumber.filterTypes}
                                                        onChange={setFilterParam}/>
                                        </CommonCollapsePanel>
                                        <CommonCollapsePanel title="GTIN">
                                            <CommonText mask="#{14}"
                                                        id={filter.FACTORY_GTIN.id}
                                                        name={filter.FACTORY_GTIN.name}
                                                        title=""
                                                        value={filter.FACTORY_GTIN.value}
                                                        filterTypes={filter.FACTORY_GTIN.filterTypes}
                                                        onChange={setFilterParam}/>
                                        </CommonCollapsePanel>
                                        <CommonCollapsePanel title="Наименование товара">
                                            <CommonText id={filter.FACTORY_name.id}
                                                        name={filter.FACTORY_name.name}
                                                        title=""
                                                        value={filter.FACTORY_name.value}
                                                        filterTypes={filter.FACTORY_name.filterTypes}
                                                        onChange={setFilterParam}/>
                                        </CommonCollapsePanel>
                                        <CommonCollapsePanel title="Статус">
                                            <div className="mdlp-common-filters-scrollable">
                                                { filter.FACTORY_statuses.map((item, index) => {
                                                    return (
                                                        <CommonCheckBox key={index}
                                                                        id={item.id}
                                                                        name={item.name}
                                                                        title={item.title}
                                                                        value={item.value}
                                                                        filterTypes={item.filterTypes}
                                                                        onChange={setFilterParam} />
                                                    );
                                                }) }
                                            </div>
                                        </CommonCollapsePanel>
                                        <CommonCollapsePanel title="Дата регистрации">
                                            <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                                        minDate={minDate}
                                                        maxDate={!!filter.FACTORY_regDateTo.value
                                                ? new Date(filter.FACTORY_regDateTo.value.valueOf()) : maxDate}
                                                        id={filter.FACTORY_regDateFrom.id}
                                                        name={filter.FACTORY_regDateFrom.name}
                                                        title={filter.FACTORY_regDateFrom.title}
                                                        value={filter.FACTORY_regDateFrom.value}
                                                        filterTypes={filter.FACTORY_regDateFrom.filterTypes}
                                                        onChange={setFilterParam} />
                                            <CommonDate labelPosition="left" format="DD.MM.YYYY"
                                                        minDate={!!filter.FACTORY_regDateFrom.value
                                                ? new Date(filter.FACTORY_regDateFrom.value.valueOf()) : minDate}
                                                        maxDate={maxDate}
                                                        id={filter.FACTORY_regDateTo.id}
                                                        name={filter.FACTORY_regDateTo.name}
                                                        title={filter.FACTORY_regDateTo.title}
                                                        value={filter.FACTORY_regDateTo.value}
                                                        filterTypes={filter.FACTORY_regDateTo.filterTypes}
                                                        onChange={setFilterParam} />
                                        </CommonCollapsePanel>
                                    </CommonFilterBlock>

                                    <CommonFilterBlock active={ this.content === 'FILIALS' }>
                                        <CommonCollapsePanel title={filter.FILIALS_PLACE.title}>
                                            <CommonText id={filter.FILIALS_PLACE.id}
                                                        name={filter.FILIALS_PLACE.name}
                                                        title=""
                                                        value={filter.FILIALS_PLACE.value}
                                                        filterTypes={filter.FILIALS_PLACE.filterTypes}
                                                        onChange={setFilterParam} />
                                        </CommonCollapsePanel>
                                    </CommonFilterBlock>

                                    <CommonFilterBlock active={ this.content === 'SAFE_WAREHOUSES' }>
                                        <CommonCollapsePanel title={filter.SAFE_WAREHOUSES_PLACE.title}>
                                            <CommonText id={filter.SAFE_WAREHOUSES_PLACE.id}
                                                        name={filter.SAFE_WAREHOUSES_PLACE.name}
                                                        title=""
                                                        value={filter.SAFE_WAREHOUSES_PLACE.value}
                                                        filterTypes={filter.SAFE_WAREHOUSES_PLACE.filterTypes}
                                                        onChange={setFilterParam}/>
                                        </CommonCollapsePanel>
                                    </CommonFilterBlock>

                                    <CommonFilterBlock active={ this.content === 'LP' }>
                                        <CommonCollapsePanel title="GTIN">
                                            <CommonText mask="#{0,14}"
                                                        id={filter.LP_GTIN.id}
                                                        name={filter.LP_GTIN.name}
                                                        title=""
                                                        value={filter.LP_GTIN.value}
                                                        filterTypes={filter.LP_GTIN.filterTypes}
                                                        onChange={setFilterParam} />
                                        </CommonCollapsePanel>
                                        <CommonCollapsePanel title="Наименование ЛП">
                                            <CommonText id={filter.LP_name.id}
                                                        name={filter.LP_name.name}
                                                        title=""
                                                        value={filter.LP_name.value}
                                                        filterTypes={filter.LP_name.filterTypes}
                                                        onChange={setFilterParam} />
                                        </CommonCollapsePanel>
                                        <CommonCollapsePanel title="Наименование держателя рег. удостоверения">
                                            <CommonText id={filter.LP_regOwnerName.id}
                                                        name={filter.LP_regOwnerName.name}
                                                        title=""
                                                        value={filter.LP_regOwnerName.value}
                                                        filterTypes={filter.LP_regOwnerName.filterTypes}
                                                        onChange={setFilterParam} />
                                        </CommonCollapsePanel>
                                        <CommonCollapsePanel title="Предельная цена">
                                            <CommonText mask="9{0,7}"
                                                        id={filter.LP_maxPrice.id}
                                                        name={filter.LP_maxPrice.name}
                                                        title=""
                                                        value={filter.LP_maxPrice.value}
                                                        filterTypes={filter.LP_maxPrice.filterTypes}
                                                        onChange={setFilterParam} />
                                        </CommonCollapsePanel>
                                    </CommonFilterBlock>
                                </CommonFilters>
                            )
                        }
                    </div>
                    <div className="mdlp-main-view-workspace-area">
                        <div className="mdlp-member_card-toolsbar">
                            <div className="mdlp-member_card-search_kiz">
                                {
                                    (!!this.props.regAnalInfo) && (
                                        <div className="mdlp-analytics_common_info_block-item drugs"
                                             title="Количество промаркированных ЛП (шт.)">{ this.props.regAnalInfo.drugs }</div>
                                    )
                                }
                                <CommonSearchControl flink={ appMenu.KIZ_INFO } name="kizId" title="Поиск по SGTIN" mask="#{27}"/>
                            </div>

                            <div className="mdlp-member_card-tools">
                                <span className={`mdlp-member_card-tools-item factory${activeTool('FACTORY')}`}
                                      title="Список КиЗ" onClick={e => this.handleFilterTypeSwitch('FACTORY')}></span>
                                <span className={`mdlp-member_card-tools-item chars${activeTool('CHARS')}`}
                                      title="Общая информация" onClick={e => this.handleFilterTypeSwitch('CHARS')}></span>
                                <span className={`mdlp-member_card-tools-item filials${activeTool('FILIALS')}`}
                                      title="Список филиалов" onClick={e => this.handleFilterTypeSwitch('FILIALS')}></span>
                                <span className={`mdlp-member_card-tools-item safe-warehouses${activeTool('SAFE_WAREHOUSES')}`}
                                      title="Список мест ответственного хранения" onClick={e => this.handleFilterTypeSwitch('SAFE_WAREHOUSES')}></span>
                                <span className={`mdlp-member_card-tools-item lp${activeTool('LP')}`}
                                      title="Производимые ЛП" onClick={e => this.handleFilterTypeSwitch('LP')}></span>
                            </div>
                        </div>
                        {
                            (this.content !== 'CHARS') ? (
                                <div>
                                    <CommonPagination place="above" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                                      totalItemsCount={total} lazyMode={ lazyMode }
                                                      pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                                    <CommonTableGrid columns={ this.props.columns[this.content] }
                                                     data={ list }
                                                     onRowClick={ (this.content === 'FACTORY') && this.handleRowClick } />
                                    <CommonPagination place="under" activePage={filter.nPage} itemsCountPerPage={filter.pageSize}
                                                      totalItemsCount={total} lazyMode={ lazyMode }
                                                      pageRangeDisplayed={5} onRefresh={refresh} onChange={toPage}/>
                                </div>
                            ) : (
                                <CardItemInfoBlock>{[
                                    { title : 'ID участника'         , value : !!parent ? parent.ID          : member.id          },
                                    { title : 'Дата регистрации участника'         , value : !!parent ? parent.REG_DATE          : member.REG_DATE          },
                                    { title : (this.props.member.isResident) ? 'ИНН' : 'ИТИН'         , value : !!parent ? parent.INN          : member.INN          },
                                    { title : 'КПП'         , value : !!parent ? parent.KPP          : member.KPP          },
                                    { title : 'ОГРН'        , value : !!parent ? parent.OGRN         : member.OGRN         },
                                    { title : 'Руководитель', value : !!parent ? parent.certOwnerFIO : member.certOwnerFIO },
                                    { title : 'Тип участника', value : !!parent ? parent.isResident ? 'Резидент' : 'Нерезидент' : member.isResident ? 'Резидент' : 'Нерезидент' },
                                    { title : 'Реестр, на основании которого участник идентифицирован', value : !!parent ? parent.registryType : member.registryType }
                                ]}</CardItemInfoBlock>
                            )
                        }
                    </div>
                </div>
            </div>
        );
    }
}
