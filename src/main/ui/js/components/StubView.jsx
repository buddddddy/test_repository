/**
 * Created by PBorisov on 09.06.2017.
 */

import React, { PropTypes, Component }   from 'react';
import $                                 from 'jquery';
import moment                            from 'moment';
import { appMenu, IsDebugMode, IsTestMode } from '../actions/common_utils.js';
import { CommonMainMenu, CommonSubMenu } from './CommonMainMenu.jsx';
import CommonPreloader                   from './CommonPreloader.jsx';

export default class StubView extends Component {
    render () {
        return (
            <div className="mdlp-main-view-container">
                <CommonMainMenu title="Меню" opts={[
                    //appMenu.KIZ_LIST(false),
                    appMenu.LP_LIST(false),
                    appMenu.MEMBERS_LIST(false),
                    appMenu.EVENTS_LIST(false),
                    appMenu.STUB_PAGE(true)
                ]} breadcrumbs={[{title : 'Мониторинг'}]}/>
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <CommonSubMenu title="Мониторинг" opts={[
                            appMenu.STUB_SUBPAGE1(false),
                            appMenu.STUB_SUBPAGE2(false),
                            appMenu.STUB_SUBPAGE3(false),
                            appMenu.STUB_SUBPAGE4(false),
                            appMenu.STUB_SUBPAGE5(false),
                            appMenu.STUB_SUBPAGE6(false),
                            appMenu.STUB_SUBPAGE7(false),
                            appMenu.STUB_SUBPAGE8(false),
                            appMenu.STUB_SUBPAGE9(false),
                            appMenu.STUB_SUBPAGE10(false),
                            appMenu.STUB_SUBPAGE11(false),
                            appMenu.STUB_SUBPAGE12(false),
                            appMenu.STUB_SUBPAGE13(false),
                            appMenu.STUB_SUBPAGE14(false),
                            appMenu.STUB_SUBPAGE15(false)
                        ]}/>
                    </div>
                    <div className="mdlp-main-view-workspace-area">
                    </div>
                </div>
            </div>
        );
    }
}
