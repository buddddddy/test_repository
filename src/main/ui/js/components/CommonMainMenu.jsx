/**
 * Created by PBorisov on 17.03.2017.
 */

import React, { Component } from 'react';
import { appMenu }          from '../actions/common_utils.js';

class CommonMenu extends Component {
    render () {
        return (
            <ul className="mdlp-common-menu">
                { this.props.opts.map((item, index) => {
                    const curClass = item.isCurrent ? 'current' : '';
                    return (
                        <li key={index} className={`mdlp-common-menu-item ${curClass}`}>
                            <a className="mdlp-common-menu-item-link" href={item.link}>{item.title}</a>
                            {/*
                                item.isCurrent
                                    ? <span className="mdlp-common-menu-item-link">{item.title}</span>
                                    : <a className="mdlp-common-menu-item-link" href={item.link}>{item.title}</a>
                            */}
                        </li>
                    );
                }) }
            </ul>
        );
    }
}

export class CommonMainMenu extends Component {
    constructor (props) {
        super(props);
        this.state = { isOpened : false };
        this.handleClick = this.handleClick.bind(this);
    }
    handleClick () {
        const newState = !this.state.isOpened;
        this.setState({ isOpened : newState });
    }
    render () {
        const sttClass = this.state.isOpened ? 'opened' : '';
        return (
            <div className="mdlp-common-main_menu">
                <div className={`mdlp-common-main_menu-dropdown ${sttClass}`}>
                    <span className="mdlp-common-dropdown-btn" onClick={this.handleClick}>{this.props.title}</span>
                    <CommonMenu opts={this.props.opts}/>
                </div>
                { !!this.props.breadcrumbs && (
                    <div className="mdlp-common-main_menu-breadcrumbs">
                        {this.props.breadcrumbs.map((item, index) => {
                            return (
                                !!item.link
                                    ? <a key={index} className="mdlp-common-main_menu-breadcrumbs-item" href={item.link}>{item.title}</a>
                                    : <span key={index} className={`mdlp-common-main_menu-breadcrumbs-item${!!item.onClick ? ' as_link' : ''}`} onClick={ item.onClick }>{item.title}</span>
                            );
                        })}
                    </div>
                ) }
                { !!this.props.children && this.props.children }
            </div>
        );
    }
}

export class AppMainMenu extends Component {
    render () {
        return (
            <CommonMainMenu title="Меню" opts={[
                    appMenu.KIZ_LIST( this.props.menuKey === 'KIZ_LIST' ),
                    appMenu.LP_LIST( this.props.menuKey === 'LP_LIST' ),
                    appMenu.MEMBERS_LIST( this.props.menuKey === 'MEMBERS_LIST' ),
                    //appMenu.EVENTS_LIST( this.props.menuKey === 'EVENTS_LIST' ),
                    appMenu.REESTRS( this.props.menuKey === 'REESTRS' )
                ]} breadcrumbs={ this.props.breadcrumbs }>
                { !!this.props.children && this.props.children }
            </CommonMainMenu>
        );
    }
}

export class CommonSubMenu extends Component {
    render () {
        return (
            <div className="mdlp-common-submenu">
                <span className="mdlp-common-submenu-title mdlp-common-block-title">{this.props.title}</span>
                <CommonMenu opts={this.props.opts}/>
            </div>
        );
    }
}

export class KiZSubMenu extends Component {
    render () {
        return (
            <CommonSubMenu title="Информация по КиЗ" opts={[
                                    appMenu.KIZ_INFO( (this.props.menuKey === 'KIZ_INFO'), this.props.kizId ),
                                    appMenu.KIZ_TIMELINE( (this.props.menuKey === 'KIZ_TIMELINE'), this.props.kizId ),
                                    appMenu.KIZ_EVENTS( (this.props.menuKey === 'KIZ_EVENTS'), this.props.kizId )
                                ]}/>
        );
    }
}

export class ReestrSubMenu extends Component {
    render () {
        return (
            <CommonSubMenu title="Реестры" opts={[
                                    appMenu.REESTR_GS1( this.props.menuKey === 'REESTR_GS1' ),
                                    appMenu.REESTR_ESKLP( this.props.menuKey === 'REESTR_ESKLP' ),
                                    appMenu.REESTR_LICENSES( this.props.menuKey === 'REESTR_LICENSES' ),
                                    appMenu.REESTR_PHARM( this.props.menuKey === 'REESTR_PHARM' )
                                ]}/>
        );
    }
}
