/**
 * Created by PBorisov on 27.03.2017.
 */

import React, { PropTypes, Component }   from 'react';
import $                                 from 'jquery';
import { appMenu }                       from '../actions/common_utils.js';
import { AppMainMenu, KiZSubMenu }       from './CommonMainMenu.jsx';
import CommonTableGrid                   from './CommonTableGrid.jsx';

export default class KiZEventsView extends Component {
    render () {
        const { kizInfo } = this.props;
        return (
            <div className="mdlp-main-view-container">
                <AppMainMenu menuKey="KIZ_LIST" breadcrumbs={ [{ title : <span className="mdlp-breadcrumbs-kiz-title found">{ kizInfo.id }</span> }] } />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <KiZSubMenu menuKey="KIZ_EVENTS" kizId={ kizInfo.id } />
                    </div>
                    <div className="mdlp-main-view-workspace-area">
                        <h2 className="mdlp-inner">{ kizInfo.title }</h2>
                        <CommonTableGrid columns={ this.props.columns } data={ this.props.list }/>
                    </div>
                </div>
            </div>
        );
    }
}
