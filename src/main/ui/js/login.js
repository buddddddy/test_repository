/**
 * Created by PBorisov on 16.03.2017.
 */

import $                     from 'jquery';
import React, { Component }  from 'react';
import ReactDOM              from 'react-dom';
import { Ajax, appRequests } from './actions/common_utils.js';
import LoginFormView         from './components/LoginFormView.jsx';

function main (appInfo) {
    ReactDOM.render( <LoginFormView/>, document.getElementById('react-container') );
}

$(function () {
    Ajax(appRequests.GET_APP_INFO()).done((appInfo) => {
        appInfo = (appInfo instanceof Array) ? (appInfo[0] || null) : (appInfo || null);
        $('#js-page-title-logo').attr('src', appInfo.logo);
        $('#js-page-title-text').text(appInfo.title);
        $('#js-page-footer').text(appInfo.footer);
        main(appInfo);
    }).fail((error) => {
        main(null);
    });
});
