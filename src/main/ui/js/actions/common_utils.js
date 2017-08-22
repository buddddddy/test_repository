/**
 * Created by PBorisov on 31.03.2017.
 */

import React, { Component } from 'react';
import ReactDOM             from 'react-dom';
import * as queryString     from 'query-string';
import $                    from 'jquery';
import CommonDebugLogPanel  from '../components/CommonDebugLogPanel.jsx';

const searchPrms = queryString.parse(location.search);

export const IsDebugMode = !!searchPrms.debug;
export const IsTestMode  = !!searchPrms.test;

export function Ajax (request, withoutError) {
    const def = $.Deferred();
    const opts = $.extend({}, (request.opts || {}), {__non_cache : (new Date()).valueOf()});
    $.ajax({
        url         : request.url,
        method      : request.type,
        data        : opts,
        dataType    : 'json',
        traditional : true
    }).done((resp) => {
        def.resolve(resp)
    }).fail((error) => {
        console.error(error);
        if (!withoutError) {
            $('#mdlp-common-messages').text('Произошла ошибка. Повторите запрос позднее.').addClass('error').addClass('show');
            $('body').scrollTop(0);
            window.setTimeout(() => {
                $('#mdlp-common-messages').html('').removeClass('show').removeClass('error');
            }, 5000);
        }
        def.reject(error);
    });
    return def.promise();
}
//window.Ajax = Ajax;

const addonPrms = [];
!!searchPrms.debug && addonPrms.push('debug=1');
!!searchPrms.test  && addonPrms.push('test=1');
const prms1 = (!!addonPrms.length ? '?' : '') + addonPrms.join('&');
const prms2 = (!!addonPrms.length ? '&' : '') + addonPrms.join('&');
export const appMenu = {
    ANALYTICS       : (isCurrent) => { return { title : 'Сводная аналитика'    , link : `analytics.html${prms1}`   , isCurrent } },
    EVENTS_LIST     : (isCurrent) => { return { title : 'Журнал событий'       , link : `events_list.html${prms1}` , isCurrent } },
    KIZ_LIST        : (isCurrent) => { return { title : 'Список КиЗ'           , link : `kiz_list.html${prms1}`    , isCurrent } },
    LP_LIST         : (isCurrent) => { return { title : 'Зарегистрированные ЛП', link : `lp_list.html${prms1}`     , isCurrent } },
    MEMBERS_LIST    : (isCurrent) => { return { title : 'Список участников'    , link : `members_list.html${prms1}`, isCurrent } },
    REESTRS         : (isCurrent) => { return { title : 'Реестры'              , link : `reestr_gs1.html${prms1}`  , isCurrent } },

    KIZ_INFO        : (isCurrent, kizId) => { return { title : 'Общая информация'      , link : `kiz_info.html?kizId=${kizId}${prms2}`    , isCurrent } },
    KIZ_EVENTS      : (isCurrent, kizId) => { return { title : 'Журнал операций по КиЗ', link : `kiz_events.html?kizId=${kizId}${prms2}`  , isCurrent } },
    KIZ_TIMELINE    : (isCurrent, kizId) => { return { title : 'График движения КиЗ'   , link : `kiz_timeline.html?kizId=${kizId}${prms2}`, isCurrent } },

    REESTR_GS1      : (isCurrent) => { return { title : 'Реестр GS1'                        , link : `reestr_gs1.html${prms1}`     , isCurrent } },
    REESTR_ESKLP    : (isCurrent) => { return { title : 'Реестр ЕСКЛП'                      , link : `reestr_esklp.html${prms1}`   , isCurrent } },
    REESTR_LICENSES : (isCurrent) => { return { title : 'Реестр лицензий на производство ЛП', link : `reestr_licenses.html${prms1}`, isCurrent } },
    REESTR_PHARM    : (isCurrent) => { return { title : 'Реестр лицензий на фармацевтическую и медицинскую деятельность', link : `reestr_pharm.html${prms1}`, isCurrent } }

    //STUB_PAGE    : (isCurrent) => { return { title : 'Мониторинг', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE1: (isCurrent) => { return { title : 'Количество ЛП, выпущенных в оборот', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE2: (isCurrent) => { return { title : 'Превышение предельных отпускных цен на ЛП', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE3: (isCurrent) => { return { title : 'Количество и стоимость ЛП, ввозимых в РФ', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE4: (isCurrent) => { return { title : 'Нарушения в части несоответствии адресов осуществления деятельности', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE5: (isCurrent) => { return { title : 'Нарушения при передачи сведений о ЛП, для которых отсутствуют сведения о вводе в оборот', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE6: (isCurrent) => { return { title : 'Местонахождение ЛП в соответствии с заданиями на выборочный контроль', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE7: (isCurrent) => { return { title : 'Движение ЛП, приобретаемых за гос. бюджетов', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE8: (isCurrent) => { return { title : 'Нарушения при попытке регистрации субъектами обращения ЛП операций по реализации и отпуску ЛП, в отношении которых принято решение о временном выводе из обращения', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE9: (isCurrent) => { return { title : 'Нарушения при попытке регистрации субъектами обращения операций по повторному выводу из обращения ЛП', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE10: (isCurrent) => { return { title : 'Нарушения при попытке регистрации субъектами обращения операций по реализации и отпуску ЛП с истекшими сроками годности', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE11: (isCurrent) => { return { title : 'Нарушения в части своевременности внесения сведений субъектами обращения ЛП', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE12: (isCurrent) => { return { title : 'ЛП, переданные на уничтожение ', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE13: (isCurrent) => { return { title : 'Общее количество регистрируемых операций', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE14: (isCurrent) => { return { title : 'Обращения пользователей к сервису проверки легитимности ЛП', link : `stub_page.html${prms1}`, isCurrent } },
    //STUB_SUBPAGE15: (isCurrent) => { return { title : 'Количества обращений и сообщений о выявленных нарушениях при проверке легитимности ЛП пользователями', link : `stub_page.html${prms1}`, isCurrent } }
};

export const appRequests = !searchPrms.test ? {
    GET_APP_INFO             : (data) => { return { url : '/rest/get_app_info'                , opts : data || {}, type : 'GET' } },
    GET_USER_INFO            : (data) => { return { url : '/rest/get_user_info'               , opts : data || {}, type : 'GET' } },
    GET_EVENTS_TYPES         : (data) => { return { url : 'json/get_events_types.json'        , opts : data || {}, type : 'GET' } },
    GET_KIZ_FULL_INFO        : (data) => { return { url : '/rest/get_kiz_full_info'           , opts : data || {}, type : 'GET' } },
    GET_KIZ_SHORT_INFO       : (data) => { return { url : '/rest/get_kiz_short_info'          , opts : data || {}, type : 'GET' } },
    GET_MEMBER_BYINN_INFO    : (data) => { return { url : '/rest/get_member/byInn'            , opts : data || {}, type : 'GET' } },
    GET_MEMBER_FILIALS_LIST  : (data) => { return { url : '/rest/get_member_filials_list'     , opts : data || {}, type : 'GET' } },
    GET_SAFE_WAREHOUSES      : (data) => { return { url : '/rest/get_member_safe_warehouses'  , opts : data || {}, type : 'GET' } },
    GET_KIZ_STATS_LIST       : (data) => { return { url : 'json/get_kiz_statuses.json'        , opts : data || {}, type : 'GET' } },
    GET_KIZ_EVENTS_LIST      : (data) => { return { url : 'json/get_kiz_events.json'          , opts : data || {}, type : 'GET' } },
    GET_LP_FORMS_LIST        : (data) => { return { url : '/rest/get_lp_forms'                , opts : data || {}, type : 'GET' } },
    GET_EVENTS_LIST          : (data) => { return { url : '/rest/get_events_list'             , opts : data || {}, type : 'GET' } },
    GET_EVENT_INFO           : (data) => { return { url : '/rest/get_event_full_info'         , opts : data || {}, type : 'GET' } },
    GET_KIZ_LIST             : (data) => { return { url : '/rest/get_kiz_list'                , opts : data || {}, type : 'GET' } },
    GET_LP_LIST              : (data) => { return { url : '/rest/get_lp_list'                 , opts : data || {}, type : 'GET' } },
    GET_MEMBERS_LIST         : (data) => { return { url : '/rest/get_members_list'            , opts : data || {}, type : 'GET' } },
    GET_DEBUG_LOG            : (data) => { return { url : '/rest/get_logs'                    , opts : data || {}, type : 'GET' } },
    GET_REGION_ANALYTICS     : (data) => { return { url : '/rest/get_region_analytics'        , opts : data || {}, type : 'GET' } },

    GET_REESTR_GS1           : (data) => { return { url : '/rest/get_reestr_gs1'          , opts : data || {}, type : 'GET' } },
    GET_REESTR_ITEM_GS1      : (data) => { return { url : '/rest/get_reestr_item_gs1'     , opts : data || {}, type : 'GET' } },
    GET_REESTR_ESKLP         : (data) => { return { url : '/rest/get_reestr_esklp'        , opts : data || {}, type : 'GET' } },
    GET_REESTR_ITEM_ESKLP    : (data) => { return { url : '/rest/get_reestr_item_esklp'   , opts : data || {}, type : 'GET' } },
    GET_REESTR_LICENSES      : (data) => { return { url : '/rest/get_reestr_licenses'     , opts : data || {}, type : 'GET' } },
    GET_REESTR_ITEM_LICENSES : (data) => { return { url : '/rest/get_reestr_item_licenses', opts : data || {}, type : 'GET' } },
    GET_REESTR_PHARM         : (data) => { return { url : '/rest/get_reestr_pharm_licenses'     , opts : data || {}, type : 'GET' } },
    GET_REESTR_ITEM_PHARM    : (data) => { return { url : '/rest/get_reestr_item_pharm_licenses', opts : data || {}, type : 'GET' } }
} : {
    GET_APP_INFO             : (data) => { return { url : 'json/get_app_info.json'            , opts : data || {}, type : 'GET' } },
    GET_USER_INFO            : (data) => { return { url : 'json/get_user_info.json'           , opts : data || {}, type : 'GET' } },
    GET_EVENTS_TYPES         : (data) => { return { url : 'json/get_events_types.json'        , opts : data || {}, type : 'GET' } },
    GET_KIZ_FULL_INFO        : (data) => { return { url : 'json/get_kiz_full_info.json'       , opts : data || {}, type : 'GET' } },
    GET_KIZ_SHORT_INFO       : (data) => { return { url : 'json/get_kiz_short_info.json'      , opts : data || {}, type : 'GET' } },
    GET_MEMBER_BYINN_INFO    : (data) => { return { url : 'json/get_member_byInn.json'        , opts : data || {}, type : 'GET' } },
    GET_MEMBER_FILIALS_LIST  : (data) => { return { url : 'json/get_member_filials_list.json' , opts : data || {}, type : 'GET' } },
    GET_KIZ_STATS_LIST       : (data) => { return { url : 'json/get_kiz_statuses.json'        , opts : data || {}, type : 'GET' } },
    GET_KIZ_EVENTS_LIST      : (data) => { return { url : 'json/get_kiz_events.json'          , opts : data || {}, type : 'GET' } },
    GET_LP_FORMS_LIST        : (data) => { return { url : 'json/get_lp_forms.json'            , opts : data || {}, type : 'GET' } },
    GET_EVENTS_LIST          : (data) => { return { url : 'json/get_events_list.json'         , opts : data || {}, type : 'GET' } },
    GET_EVENT_INFO           : (data) => { return { url : 'json/get_event_info.json1'         , opts : data || {}, type : 'GET' } },
    GET_KIZ_LIST             : (data) => { return { url : 'json/get_kiz_list.json'            , opts : data || {}, type : 'GET' } },
    GET_LP_LIST              : (data) => { return { url : 'json/get_lp_list.json'             , opts : data || {}, type : 'GET' } },
    GET_MEMBERS_LIST         : (data) => { return { url : 'json/get_members_list.json'        , opts : data || {}, type : 'GET' } },
    GET_DEBUG_LOG            : (data) => { return { url : 'json/get_debug_log.json'           , opts : data || {}, type : 'GET' } },
    GET_REGION_ANALYTICS     : (data) => { return { url : 'json/get_region_analytics.json'    , opts : data || {}, type : 'GET' } },

    GET_REESTR_GS1           : (data) => { return { url : 'json/get_reestr_gs1.json'          , opts : data || {}, type : 'GET' } },
    GET_REESTR_ITEM_GS1      : (data) => { return { url : 'json/get_reestr_item_gs1.json'     , opts : data || {}, type : 'GET' } },
    GET_REESTR_ESKLP         : (data) => { return { url : 'json/get_reestr_esklp.json'        , opts : data || {}, type : 'GET' } },
    GET_REESTR_ITEM_ESKLP    : (data) => { return { url : 'json/get_reestr_item_esklp.json'   , opts : data || {}, type : 'GET' } },
    GET_REESTR_LICENSES      : (data) => { return { url : 'json/get_reestr_licenses.json'     , opts : data || {}, type : 'GET' } },
    GET_REESTR_ITEM_LICENSES : (data) => { return { url : 'json/get_reestr_item_licenses.json', opts : data || {}, type : 'GET' } },
    GET_REESTR_PHARM         : (data) => { return { url : 'json/get_reestr_pharm_licenses.json'     , opts : data || {}, type : 'GET' } },
    GET_REESTR_ITEM_PHARM    : (data) => { return { url : 'json/get_reestr_item_pharm_licenses.json', opts : data || {}, type : 'GET' } }
};

$.fn.resizeBox = function () {
    const $body  = $('body');
    const $self  = $(this).addClass('mdlp-common-resize_box');
    const $panel = $('<div class="mdlp-common-resize_box-panel"></div>').appendTo($self);
    const $cont  = $('<div class="mdlp-common-resize_box-cont"></div>').appendTo($self);
    var y = 0, minh = $self.height(), itr = null;
    $panel.on('mousedown.resizebox', (e) => {
        y = $self.offset().top;
        $body.css('cursor', 'ns-resize')
            .on('selectstart.resizebox', () => { return false; })
            .on('mousemove.resizebox', (me) => { y = me.pageY; });
        itr = window.setInterval(() => {
            $self.height(Math.max(minh, $self.height() + $self.offset().top - y));
        }, 100);
        $body.one('mouseup.resizebox', () => {
            $body.css('cursor', 'inherit').off('selectstart.resizebox').off('mousemove.resizebox');
            !!itr && window.clearInterval(itr);
            itr = null;
        });
        return false;
    });
    return $cont.get(0);
};

export function appStart (requests, cb) {
    $(function () {
        const defs  = [appRequests.GET_APP_INFO(), appRequests.GET_USER_INFO()].concat(requests || []).map((r) => Ajax(r));
        $.when.apply($, defs).done(function (appInfo, userInfo) {
            appInfo  = (appInfo  instanceof Array) ? (appInfo[0]  || null) : (appInfo  || null);
            userInfo = (userInfo instanceof Array) ? (userInfo[0] || null) : (userInfo || null);
            if (!userInfo) window.location.href = '/';
            /* Set application and current user info */
            $('#mdlp-user-info').attr('href', userInfo.link).text(userInfo.name);
            $('#js-page-title-logo').attr('href', appMenu.ANALYTICS(true).link);
            $('#js-page-title-logo-img').attr('src', appInfo.logo);
            $('#js-page-title-text').text(appInfo.title);
            $('#js-page-footer-version').text(appInfo.version);
            $('#js-page-footer-copyright').text(appInfo.footer);
            cb.apply(null, arguments);
            //cb(userInfo);
            if (IsDebugMode) {
                $('body').addClass('debug-log');
                ReactDOM.render(
                    <CommonDebugLogPanel/>,
                    $('#react-debug-log-container').resizeBox()
                );
            }
            /* Show page content */
            $('body').removeClass('not-ready');
        }).fail(function (error) {
            window.location.href = '/';
        });
    });
}
