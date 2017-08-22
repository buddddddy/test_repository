/**
 * Created by PBorisov on 29.03.2017.
 */

import React, { PropTypes, Component }   from 'react';
import $                                 from 'jquery';
import { appMenu }                       from '../actions/common_utils.js';
import moment                            from 'moment';
import Modal, {closeStyle}               from 'simple-react-modal';
import { AppMainMenu, KiZSubMenu }       from './CommonMainMenu.jsx';
import { appRequests, Ajax }             from '../actions/common_utils.js';

class KiZTimelineBlockView extends Component {
    constructor (props) {
        super(props);
        this.addonInfo = null;
        this.handleAddonClick = this.handleAddonClick.bind(this);
    }
    handleAddonClick (e) {
        e.preventDefault();
        const prom = !!this.addonInfo
            ? $.Deferred().resolve(this.addonInfo)
            : Ajax(appRequests.GET_EVENT_INFO({ eventId : this.props.id, kizId : this.props.kizInfo.id }));
        prom.done((resp) => {
            this.addonInfo = resp;
            !!this.props.onAddonClick && this.props.onAddonClick(this.props, this.addonInfo);
        }).fail(() => {});
    }
    render () {
        const side = !!(this.props.index % 2) ? 'even' : 'odd';
        let date = !!this.props.dateTime ? moment(this.props.dateTime) : null;
        date = (!!date && date.isValid()) ? date.format('DD.MM.YYYY') : '';
        const memberType = (this.props.type.class == 'create') ? 'Производитель'
            :              (this.props.type.class == 'sale')   ? 'Продавец' : 'Получатель';
        const memberName = !!this.props.memberToName ? this.props.memberToName : this.props.memberFromName;
        const memberINN  = !!this.props.memberToINN  ? this.props.memberToINN  : this.props.memberFromINN;
        const memberKPP  = !!this.props.memberToKPP  ? this.props.memberToKPP  : this.props.memberFromKPP;
        const cost = (['move_owner', 'receive_owner', 'move_order_s', 'receive_order_s'].indexOf(this.props.eventTypeId) >= 0)
            ? (Number(this.props.cost) || 0) : 0;
        const cancelled = !!this.props.cancellationInfo.cancelled  ? this.props.cancellationInfo.cancelled  : this.props.cancellationInfo.cancelled;
        const cancelInitiatorName = !!this.props.cancellationInfo.title  ? this.props.cancellationInfo.title  : this.props.cancellationInfo.title;
        return (
            <div className={`frst-timeline-block frst-${side}-item ${this.props.type.state}`} data-animation="slideInUp">
                <div className="frst-timeline-img">
                    <span className={`${cancelled ? 'striped' : ''}`}>
                        <i className={ this.props.type.class } aria-hidden="true"></i>
                    </span>
                </div>
                <div className={`frst-timeline-content hingeTop animated hingeLeft`}>
                    <div className={`frst-timeline-content-inner ${cancelled ? 'striped' : ''}`}>
                        <span className="frst-timeline-cancelled-content-title">{(cancelled) && <span>{`Операция отменена:`}</span>}</span>
                        <p className="frst-timeline-cancelled-content-text">{(cancelInitiatorName) && <span>{`${cancelInitiatorName}`}</span>}</p>
                        <span className="frst-timeline-content-title">{this.props.type.title}</span>
                        <span className="frst-date">{date}</span>
                        <p className="frst-timeline-content-text">{
                            (!!memberType && !!memberName) && `${memberType} ${memberName}`
                        }</p>
                        <p className="frst-timeline-content-text">
                            { !!memberINN && <span>{`ИНН: ${memberINN}`}</span> }
                            { !!memberKPP && <span>{`КПП: ${memberKPP}`}</span> }
                        </p>
                        <p className="frst-timeline-content-text">
                            { (cost > 0) && <span>{`Стоимость: ${cost} руб.`}</span> }
                        </p>
                        {/*
                         <p className="frst-timeline-content-links">
                         <a href={ this.props.inside } target="_blank">Зарегистрированный документ</a>
                         <a href="#" onClick={ this.handleAddonClick  }>Подробнее</a>
                         </p>
                        */}
                    </div>
                </div>
            </div>
        );
    }
}

function EventAddonInfoMap (data) {
    switch (data.eventTypeId) {
        case 'register_end_packing' :
            return [
                { name : 'Наименование ЛП'                  , value : data.lpName },
                { name : 'Производитель (наименование, ИНН)', value : data.producer },
                { name : 'Дата операции'                    , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' },
                { name : 'Номер серии'                      , value : data.seriesNumber },
                { name : 'Срок годности ЛП'                 , value : (!!data.expirationDate && moment(data.expirationDate).isValid()) ? moment(data.expirationDate).format('DD.MM.YYYY') : '' },
                { name : 'GTIN'                             , value : data.GTIN },
                { name : 'SGTIN'                            , value : data.SGTIN },
                { name : 'TNVED'                            , value : data.TNVED }
            ];
        case 'move_order_s' :
            return [
                { name : 'Отправитель (ИНН, наименование)'     , value : data.sender },
                { name : 'Получатель (ИНН, наименование)'      , value : data.recipient },
                { name : 'Дата операции'                       , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' },
                { name : 'Накладная (название документа, дата)', value : data.bill },
                { name : 'Стоимость'                           , value : data.cost }
            ];
        case 'receive_order_s' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'retail_sale' :
            return [
                { name : 'Продавец (ИНН, наименование)', value : data.seller },
                { name : 'Дата продажи'                , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' },
                { name : 'Цена'                        , value : data.price },
                { name : 'Список документов'           , value : data.documents.map((doc, index) => {
                    return `${doc.type} № ${doc.number}` +
                        (((!!doc.date && moment(doc.date).isValid())) ? ' от '+moment(doc.date).format('DD.MM.YYYY') : '');
                }).join("\n") }
            ];
        case 'health_care' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'move_owner' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'receive_owner' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'control_samples' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'register_foreign_product_emission' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'receive_importer' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'recipe' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'withdrawal' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'foreign_shipment' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'foreign_import' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'fts_data' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'move_place' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'move_destruction' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'destruction' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'reexport' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'relabeling' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        case 'move_info' :
            return [
                { name : 'Получатель (ИНН, наименование)', value : data.recipient },
                { name : 'Дата операции'                 , value : (!!data.date && moment(data.date).isValid()) ? moment(data.date).format('DD.MM.YYYY') : '' }
            ];
        default :
            return [];
    }
}

export default class KiZTimelineView extends Component {
    constructor (props) {
        super(props);
        this.state = { showModal : false, eventInfo : null };
        this.handleAddonInfo = this.handleAddonInfo.bind(this);
        this.handleModalShow = this.handleModalShow.bind(this);
        this.handleModalHide = this.handleModalHide.bind(this);
    }
    handleAddonInfo (info, addon) {
        this.setState({ eventInfo : { ...info, addon } });
        this.handleModalShow();
    }
    handleModalShow () { this.setState({ showModal :  true }); }
    handleModalHide () { this.setState({ showModal : false }); }
    render () {
        const { kizInfo, list } = this.props;
        return (
            <div className="mdlp-main-view-container">
                { !!this.state.eventInfo && (
                    <Modal className="mdlp-dialog" containerClassName="mdlp-dialog-event-info"
                           show={ this.state.showModal } closeOnOuterClick={ true } onClose={ this.handleModalHide }>
                        <a className="mdlp-dialog-close_btn" onClick={ this.handleModalHide }>
                            <span className="mdlp-dialog-close_btn__tip"></span>
                        </a>
                        <div>
                            <div className="mdlp-dialog-event-info-title">{ this.state.eventInfo.type.title }</div>
                            { EventAddonInfoMap(this.state.eventInfo.addon).map((param, index) => {
                                return (
                                    <div key={ index } className="mdlp-dialog-event-info-row">
                                        <span>{ param.name }</span>
                                        <span><pre>{ param.value }</pre></span>
                                    </div>
                                );
                            }) }
                        </div>
                    </Modal>
                ) }
                <AppMainMenu menuKey="KIZ_LIST" breadcrumbs={ [{ title : <span className="mdlp-breadcrumbs-kiz-title found">{ kizInfo.id }</span> }] } />
                <div className="mdlp-main-view-workspace">
                    <div className="mdlp-main-view-workspace-leftbar">
                        <KiZSubMenu menuKey="KIZ_TIMELINE" kizId={ kizInfo.id } />
                    </div>
                    <div className="mdlp-main-view-workspace-area">
                        <h2 className="mdlp-inner">{ kizInfo.title }</h2>
                        <div className="frst-container" data-animation-name="slideInUp">
                            <div className="frst-timeline frst-timeline-style-1 frst-alternate frst-date-opposite">
                                { this.props.list.map((item, index) => {
                                    return (
                                        <KiZTimelineBlockView key={index}
                                                              index={index}
                                                              type={ this.props.types[item.eventTypeId] }
                                                              kizInfo={ kizInfo }
                                                              onAddonClick={ this.handleAddonInfo }
                                            { ...item }
                                        />
                                    );
                                }) }
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
