/**
 * Created by PBorisov on 08.06.2017.
 */

import React, { PropTypes, Component } from 'react';
import $                   from 'jquery';
import { appMenu }         from '../actions/common_utils.js';
import { AppMainMenu }     from './CommonMainMenu.jsx';
import CommonSearchControl from './CommonSearchControl.jsx';
import CommonVMap          from './CommonVMap.jsx';

class AnalyticsCommonInfoBlock extends Component {
    render () {
        return (
            <div className="mdlp-analytics_common_info_block">
                <a key="0"
                   href={ appMenu.MEMBERS_LIST(true).link }
                   className="mdlp-analytics_common_info_block-item members"
                   title="Количество участников">{ this.props.data.members }</a>
                <a key="1"
                    href={ appMenu.KIZ_LIST(true).link }
                    className="mdlp-analytics_common_info_block-item drugs"
                    title="Количество промаркированных ЛП (шт.)">{ this.props.data.drugs }</a>
                <span key="2"
                      className="mdlp-analytics_common_info_block-item sales"
                      title="Объем продаж (руб.)">{ this.props.data.sales }</span>
            </div>
        );
    }
}


export default class AnalyticsView extends Component {
    handleRegionSelect (code, key) {
        const region = this.props.model.getItem(code) || this.props.model.newItem(code, key);
        const selectedRegion = this.props.model.getSelected();
        !!selectedRegion && this.handleRegionDeselect(selectedRegion.code);
        !!this.props.actions.showRegionFullInfo && this.props.actions.showRegionFullInfo(region);
    }
    handleRegionDeselect (code, key) {
        const region  = this.props.model.getItem(code) || this.props.model.newItem(code, key);
        !!this.props.actions.hideRegionFullInfo && this.props.actions.hideRegionFullInfo(region);
    }
    render () {
        const { tooltipShow, fullInfoShow } = this.props.regionAnalytics || {};
        //const currentRegion  = this.props.model.getCurrent();
        const selectedRegion = this.props.model.getSelected();
        return (
            <div className="mdlp-main-view-container">
                <AppMainMenu menuKey="ANALYTICS" breadcrumbs={ false }>
                    <div className="mdlp-index-page-header_box">
                        <h2 className="mdlp-index-page-header_box-title">Сводная аналитика</h2>
                        {
                            !!this.props.analyticsCommonInfo &&
                                <AnalyticsCommonInfoBlock data={ this.props.analyticsCommonInfo } />
                        }
                    </div>
                </AppMainMenu>
                <div className="mdlp-common-vmap-placeholder">
                    {/*<div className="mdlp-analytics-search_kiz">
                        <CommonSearchControl flink={ appMenu.KIZ_INFO } name="kizId" title="Поиск по SGTIN" mask="#{27}"/>
                    </div>*/}
                    <CommonVMap
                        model={ this.props.model }
                        membersLink={ appMenu.MEMBERS_LIST(false).link }
                        { ...this.props.regionAnalytics }
                        onRegionSelect={ this.handleRegionSelect.bind(this) }
                        onRegionDeselect={ this.handleRegionDeselect.bind(this) } />
                </div>
            </div>
        );
    }
}
