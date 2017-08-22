/**
 * Created by PBorisov on 08.06.2017.
 */

import $ from 'jquery';
import moment from 'moment';
import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import VMapConstants from './CommonVMap/VMapRussia.js';

class CommonVMapCursor {
    constructor (refresh) {
        this.refresh = refresh;
        this.x  =  0;
        this.y  =  0;
        this.id = -1;
    }
    set (x, y, id) {
        this.x  = (x  != null) ? x  : this.x;
        this.y  = (y  != null) ? y  : this.y;
        this.id = (id != null) ? id : this.id;
        this.refresh();
    }
    clear () {
        this.x  =  0;
        this.y  =  0;
        this.id = -1;
        this.refresh();
    }
    has () { return this.id > -1; }
    is (index) { return this.id === index; }
}

class CommonVMapPopup extends Component {
    constructor (props) {
        super(props);
        this.$self = null;
    }
    componentDidMount () { this.moveToCursor(); }
    componentDidUpdate () { this.moveToCursor(); }
    moveToCursor () { !!this.props.onMoveToCursor && this.props.onMoveToCursor(this, this.props.cursor); }
    render () {
        const item = (!!this.props.data && !this.props.data.isEmpty()) ? this.props.data : null;
        const membersLink = !!this.props.membersLink ? this.props.membersLink.split('?') : [];
        if (!!this.props.region) {
            membersLink[1] = (!!membersLink[1])
                ? `${membersLink[1]}&federal_code=${this.props.region.key}` : `federal_code=${this.props.region.key}`;
        }
        return (
            <div className={`mdlp-vmap-popup ${this.props.role}`} style={ {display : !!this.props.region ? 'block' : 'none'} }>
                <div className="mdlp-vmap-popup-pointer"></div>
                <p className="mdlp-vmap-popup-title">{ !!this.props.region ? this.props.region.value : '' }</p>
                {
                    (this.props.role === 'info') && (
                        (!!item && !!item.data)
                            ? (
                            <div className="mdlp-vmap-popup-info">
                                <a href={ membersLink.join('?') } className="mdlp-vmap-popup-info-row">
                                    <span className="mdlp-vmap-popup-info-row-head">Участников</span>
                                    <span className="mdlp-vmap-popup-info-row-val">{ item.data.members }</span>
                                </a>
                                <p className="mdlp-vmap-popup-info-row">
                                    <span className="mdlp-vmap-popup-info-row-head">ЛП (шт.)</span>
                                    <span className="mdlp-vmap-popup-info-row-val">{ item.data.drugs }</span>
                                </p>
                                <p className="mdlp-vmap-popup-info-row">
                                    <span className="mdlp-vmap-popup-info-row-head">Объем продаж (руб.)</span>
                                    <span className="mdlp-vmap-popup-info-row-val">{ item.data.sales }</span>
                                </p>
                                {/*<a className="mdlp-vmap-popup-info_link" href={ membersLink.join('?') }>Участники системы</a>*/}
                            </div>
                        ) : (!!item && !!item.error)
                            ? (
                            <p className="mdlp-vmap-popup-error">
                                Произошла ошибка.<br/>Повторите запрос позднее
                            </p>
                        ) : (
                            <p className="mdlp-vmap-popup-loader"></p>
                        )
                    )
                }
            </div>
        );
    }
}

class CommonVMapFullInfo extends Component {
    constructor (props) {
        super(props);
    }
    getMaxDymamicsValues () {
        const maxValue = { members : 0, drugs : 0, sales : 0 };
        if (!!this.props.data && !!this.props.data.dynamics) {
            this.props.data.dynamics.forEach((e) => {
                const rates = e.rates || {};
                ['members', 'drugs', 'sales'].forEach((k) => {
                    let v = rates[k] || {};
                    v = { currentValue : 0, previousValue : 0, ...v };
                    maxValue[k] = (v.currentValue  > maxValue[k]) ? v.currentValue  : maxValue[k];
                    maxValue[k] = (v.previousValue > maxValue[k]) ? v.previousValue : maxValue[k];
                });
            });
        }
        return maxValue;
    }
    render () {
        const maxValue = this.getMaxDymamicsValues();
        return (
            <div className="mdlp-common-map-full_info" data-title={ this.props.title }>
                <div className="mdlp-common-map-full_info-common_params">{
                    ['members', 'drugs', 'sales'].map((itemName, index) => {
                        return (
                            <div key={ index } className={`mdlp-common-map-full_info-common_params-item ${itemName}`}
                                 data-value={ this.props.data[itemName] }></div>
                        );
                    })
                }</div>
                {
                    !!this.props.data && !!this.props.data.dynamics && (
                        <div className="mdlp-common-map-full_info-dymamics-box">
                            <div className="mdlp-common-map-full_info-dymamics">{
                                (this.props.data.dynamics || []).map((item, index) => {
                                    return (
                                        <div key={ index } className="mdlp-common-map-full_info-dymamics-item">
                                            <div className="mdlp-common-map-full_info-dymamics-item-graph">{
                                                ['members', 'drugs', 'sales'].map((alias, i) => {
                                                    const r = item.rates[alias] || { currentValue : 0, previousValue : 0 };
                                                    const delta = !!r.currentValue ? (100 * (r.currentValue - r.previousValue) / r.currentValue) : null;
                                                    const currentH  = !!maxValue[alias] ? (430 * r.currentValue  / maxValue[alias]) : 0;
                                                    const previousH = !!maxValue[alias] ? (430 * r.previousValue / maxValue[alias]) : 0;
                                                    return (
                                                        <div key={`${index}_${alias}`}
                                                             className="mdlp-common-map-full_info-dymamics-item-graph-item">
                                                            <p className="mdlp-common-map-full_info-dymamics-item-graph-item-value">
                                                                { r.currentValue }
                                                                { !!delta && <span className={`mdlp-common-map-full_info-dymamics-item-graph-item-value-delta${(delta < 0) ? ' negative' : ''}`}>{ Math.round(delta) }%</span> }
                                                            </p>
                                                            <div className={`mdlp-common-map-full_info-dymamics-item-graph-item-draw ${alias}`}
                                                                 style={ {height : `${(currentH > previousH) ? currentH : previousH}px`} }>
                                                                <div className="mdlp-common-map-full_info-dymamics-item-graph-item-draw-block current"
                                                                     style={ {height : `${currentH + 5}px`} }></div>
                                                                { !!delta && <div className={`mdlp-common-map-full_info-dymamics-item-graph-item-draw-block prev${(delta > 0) ? ' invers' : ''}`}
                                                                                  style={ {height : `${previousH + 5}px`} }></div> }
                                                            </div>
                                                        </div>
                                                    );
                                                })
                                            }</div>
                                            <div className="mdlp-common-map-full_info-dymamics-item-month">{
                                                moment(item.monthYear).locale('ru').format('MMMM YYYY')
                                            }</div>
                                        </div>
                                    );
                                })
                            }</div>
                        </div>
                    )
                }
            </div>
        );
    }
}

class CommonVMapGroup extends Component {
    componentDidMount () {
        $(window).on('resize.vmap', this.handleResize.bind(this));
        window.setTimeout(this.handleResize.bind(this), 50);
    }
    handleResize () { !!this.props.onResize && this.props.onResize(this); }
    render () {
        const { regions, selectedCursor, selectedColor } = this.props;
        return (
            <g>{
                regions.map((reg, index) => {
                    const isSelected = selectedCursor.is(index);
                    return (
                        <path
                            key={ index }
                            id={ reg.code }
                            className={`mdlp-vmap-region${isSelected ? ' selected' : ''}`}
                            d={ reg.path }
                            fill={ isSelected ? selectedColor : reg.fillColor }
                            stroke="#A6BECE"
                            onClick={ (e) => !!this.props.onRegionClick && this.props.onRegionClick(e, index, this) }
                            onMouseOver={ (e) => !!this.props.onRegionMouseOver && this.props.onRegionMouseOver(e, index, this) }
                            onMouseMove={ (e) => !!this.props.onRegionMouseMove && this.props.onRegionMouseMove(e, index, this) }
                            onMouseOut={ (e) => !!this.props.onRegionMouseOut && this.props.onRegionMouseOut(e, index, this) } />
                    );
                })
            }</g>
        );
    }
}

class CommonVMapCanvas extends Component {
    render () {
        return (
            <svg
                width="100%"
                className="mdlp-vmap-canvas"
                x="0px"
                y="0px"
                version="1.1" >
                <CommonVMapGroup { ...this.props }
                    onResize={ (group) => !!this.props.onResize && this.props.onResize(this, group) }
                    onRegionClick={ (e, index, group) => !!this.props.onRegionClick && this.props.onRegionClick(e, index, this, group) }
                    onRegionMouseOver={ (e, index, group) => !!this.props.onRegionMouseOver && this.props.onRegionMouseOver(e, index, this, group) }
                    onRegionMouseMove={ (e, index, group) => !!this.props.onRegionMouseMove && this.props.onRegionMouseMove(e, index, this, group) }
                    onRegionMouseOut={ (e, index, group) => !!this.props.onRegionMouseOut && this.props.onRegionMouseOut(e, index, this, group) } />
            </svg>
        );
    }
}

export default class CommonVMap extends Component {
    constructor (props) {
        super(props);
        this.defaultWidth  = 1183.4;
        this.defaultHeight =  674.4;
        this.current       = new CommonVMapCursor(() => { this.forceUpdate(); });
        this.selected      = new CommonVMapCursor(() => { this.forceUpdate(); });
        //this.hint          = { x : 0, y : 0, id : -1 };
        window.vmap = this;
    }
    handleResize (canvas, group) {
        const domSelf = ReactDOM.findDOMNode(this),
            domCanvas = ReactDOM.findDOMNode(canvas),
            domGroup  = ReactDOM.findDOMNode(group),
            selfSize  = domSelf.getBoundingClientRect(),
            scaleX    = 0.9, // (selfSize.width - 17) / this.defaultWidth,
            scaleY    = scaleX,
            width     = this.defaultWidth * scaleX,
            height    = this.defaultHeight * scaleY;
        domGroup.setAttribute('transform', `scale(${scaleX}, ${scaleY}) translate(0, 0)`);
        const groupSize = domGroup.getBoundingClientRect(),
            x           = (0 - (groupSize.left - selfSize.left)) / scaleX,
            dx          = (selfSize.width - 17 - width) / 2,
            y           = (0 - (groupSize.top - selfSize.top))  / scaleY,
            dy          = 0;
        domGroup.setAttribute('transform', `scale(${scaleX}, ${scaleY}) translate(${x + dx}, ${y + dy})`);
        domCanvas.setAttribute('height', height);
    }
    handleRegionClick (e, index, canvas, group) {
        const { code, key } = VMapConstants.regions[index];
        if (this.selected.is(index)) {
            this.selected.clear();
            this.current.set(e.pageX, e.pageY, index);
            !!this.props.onRegionDeselect && this.props.onRegionDeselect(code, key);
        } else {
            this.current.clear();
            this.selected.set(e.pageX, e.pageY, index);
            !!this.props.onRegionSelect && this.props.onRegionSelect(code, key);
        }
    }
    handleRegionMouseMove (e, index, canvas, group) {
        this.selected.is(index) ? this.current.clear() : this.current.set(e.pageX, e.pageY, index);
    }
    handleRegionMouseOut (e, index, canvas, group) {
        this.current.clear();
    }
    handleMoveToCursor (popup, cursor) {
        const domSelf   = ReactDOM.findDOMNode(this),
            selfWidth   = domSelf.clientWidth,
            domPopup    = ReactDOM.findDOMNode(popup),
            popupWidth  = domPopup.clientWidth,
            popupHeight = domPopup.clientHeight,
            x           = cursor.x - domSelf.offsetLeft,
            y           = cursor.y - domSelf.offsetTop,
            popupLeft   = (x - popupWidth/2 < 0)         ? 0
                :        (x + popupWidth/2 > selfWidth) ? selfWidth - popupWidth
                : x - popupWidth/2,
            pointXType  = (popupLeft < (selfWidth - popupLeft - popupWidth)) ? 'left' : 'right',
            pointYType  = ((y - popupHeight - 22) < 0) ? 'up' : 'down',
            popupTop    = (y + domSelf.scrollTop) + ((pointYType === 'down') ? -(popupHeight + 22) : 22),
            pointLeft   = (x - popupLeft) - ((pointXType === 'right') ? 10 : 0);
        domPopup.style.left            = popupLeft + 'px';
        domPopup.style.top             = popupTop  + 'px';
        domPopup.firstChild.style.left = pointLeft + 'px';
        domPopup.firstChild.setAttribute('data-position', `${pointXType}-${pointYType}`);
    }
    render () {
        const selReg  = this.selected.has() ? VMapConstants.regions[this.selected.id] : null,
            curReg    = this.current.has()  ? VMapConstants.regions[this.current.id]  : null,
            item      = !!selReg ? this.props.model.getItem(selReg.code) : null;
        return (
            <div className="mdlp-vmap">
                <div className="mdlp-vmap-wrap">
                    {
                        <CommonVMapPopup role="hint" region={ curReg }
                                         cursor={ this.current }
                                         onMoveToCursor={ this.handleMoveToCursor.bind(this) } />
                    }
                    {
                        <CommonVMapPopup role="info" region={ selReg }
                                         cursor={ this.selected }
                                         membersLink={ this.props.membersLink }
                                         data={ item }
                                         onMoveToCursor={ this.handleMoveToCursor.bind(this) } />
                    }
                    <CommonVMapCanvas regions={ VMapConstants.regions }
                                      selectedColor={ VMapConstants.selectedColor }
                                      bordersColor={ VMapConstants.bordersColor }
                                      selectedCursor={ this.selected }
                                      onResize={ this.handleResize.bind(this) }
                                      onRegionClick={ this.handleRegionClick.bind(this) }
                                      onRegionMouseMove={ this.handleRegionMouseMove.bind(this) }
                                      onRegionMouseOut={ this.handleRegionMouseOut.bind(this) }/>
                </div>
                {
                    !!item && !item.isEmpty() && (
                        <CommonVMapFullInfo title={ selReg.value } data={ this.props.model.getItem(selReg.code).data } />
                    )
                }
            </div>
        );
    }
}
