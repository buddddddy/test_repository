/**
 * Created by PBorisov on 20.03.2017.
 */

import React, { Component }       from 'react';
import DayPicker, { LocaleUtils } from 'react-day-picker';
import $                          from 'jquery';
import moment                     from 'moment';
import Inputmask                  from 'inputmask';
import 'jquery.inputmask';
import 'inputmask.extensions';
import 'inputmask.numeric.extensions';
import 'inputmask.date.extensions';

class CommonControl extends Component {
    getElID () {
        return `${this.props.id}-${this.props.name}`;
    }
    handleChange (value) {
        !!this.props.onChange && this.props.onChange({
            id    : this.props.id,
            name  : this.props.name,
            filterTypes : this.props.filterTypes || [],
            value : value
        });
    }
    render () { throw 'Abstract method \'render\' was called !'; }
}

export class CommonCheckBox extends CommonControl {
    constructor (props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }
    handleChange (ev) {
        super.handleChange($(ev.target).is(':checked'));
    }
    render () {
        return (
            <span className="mdlp-common-control checkbox">
                <input type="checkbox" id={this.getElID()} checked={this.props.value} onChange={this.handleChange}/>
                <label htmlFor={this.getElID()}>{this.props.title}</label>
            </span>
        );
    }
}

export class CommonComboBox extends CommonControl {
    constructor (props) {
        super(props);
        this.input        = null;
        this.handleChange = this.handleChange.bind(this);
    }
    handleChange (ev) {
        super.handleChange($(ev.target).val());
    }
    render () {
        const cls = (!this.props.labelPosition) ? '' : (this.props.labelPosition == 'up') ? 'label-up' : 'label-left';
        return (
            <span className={`mdlp-common-control combobox ${cls}`}>
                { !!this.props.labelPosition && <label htmlFor={this.getElID()}>{this.props.title}</label> }
                <select id={ this.getElID() } value={ this.props.value } onChange={ this.handleChange }>
                    <option key={ -1 } value="">--- Не выбран</option>
                    {
                        (this.props.options || []).sort((a, b) => {
                            return (a.title < b.title) ? -1 : (a.title > b.title) ? 1 : 0;
                        }).map((opt, index) => {
                                return (
                                    <option key={ index } value={ opt.id }>{ opt.title }</option>
                                );
                            }
                        )
                    }
                </select>
            </span>
        );
    }
}

export class CommonText extends CommonControl {
    constructor (props) {
        super(props);
        this.input        = null;
        this.setInputMask = this.setInputMask.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }
    setInputMask (input) {
        this.input = input;
        !!this.input && !!this.props.mask && Inputmask(this.props.mask, {
            definitions  : { '#': { validator : '[A-Z0-9]', cardinality : 1, prevalidator : null } },
            autoUnmask   : true,
            //clearIncomplete : true,
            oncomplete   : () => { this.handleChange($(this.input).val()); },
            onincomplete : () => { this.handleChange(''); },
            oncleared    : () => { this.handleChange(''); }
        }).mask(input);
    }
    handleChange (v) {
        super.handleChange(v);
    }
    render () {
        const cls = (!this.props.labelPosition) ? '' : (this.props.labelPosition == 'up') ? 'label-up' : 'label-left';
        return (
            <span className={`mdlp-common-control text ${cls}`}>
                { !!this.props.labelPosition && <label htmlFor={this.getElID()}>{this.props.title}</label> }
                <input type="text"
                       placeholder={ !this.props.labelPosition ? this.props.title : null }
                       id={this.getElID()}
                       value={this.props.value}
                       ref={this.setInputMask}
                       onChange={ !this.props.mask ? (() => this.handleChange($(this.input).val())) : null }
                    />
            </span>
        );
    }
}

export class CommonDate extends CommonControl {
    constructor (props) {
        super(props);
        this.state                 = {
            month            : (!!this.props.value && this.props.value.isValid())
                ? new Date(this.props.value.valueOf()) : new Date(),
            calendarIsOpened : false
        };
        this.inputDate             = null;
        const monthes              = ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август',
            'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'];
        const wdays                = [['Вс', 'Воскресенье'], ['Пн', 'Понедельник'], ['Вт', 'Вторник'], ['Ср', 'Среда'],
            ['Чт', 'Четверг'], ['Пт', 'Пятница'], ['Сб', 'Суббота']];
        this.dayPickerLocale       = {
            ...LocaleUtils,
            formatMonthTitle   : (d, l) => { return `${monthes[d.getMonth()]} ${d.getFullYear()}`; },
            getMonths          : (l)    => { return monthes; },
            formatWeekdayShort : (i, l) => { return wdays[i][0]; },
            formatWeekdayLong  : (i, l) => { return wdays[i][1]; },
            getFirstDayOfWeek  : (l)    => { return 1; }
        };
        this.setInputMask          = this.setInputMask.bind(this);
        this.handleYearMonthChange = this.handleYearMonthChange.bind(this);
        this.handleDateChange      = this.handleDateChange.bind(this);
    }
    handleYearMonthChange (month) { this.setState({ month }); }
    setInputMask (inputDate) {
        this.inputDate = inputDate;
        !!this.inputDate && Inputmask('dd.mm.yyyy', {
            placeholder     : 'ДД.ММ.ГГГГ',
            clearIncomplete : true,
            oncomplete      : () => {
                let date = moment(this.inputDate.value, this.props.format || 'DD.MM.YYYY');
                const mday = Number(date.format('YYYYMMDD'));
                const min  = Number(moment(this.props.minDate).format('YYYYMMDD'));
                const max  = Number(moment(this.props.maxDate).format('YYYYMMDD'));
                date = (mday < min) ? moment(this.props.minDate)
                    :  (mday > max) ? moment(this.props.maxDate)
                    : date;
                this.handleChange(date);
            },
            onincomplete    : () => { this.handleChange(this.props.value); },
            oncleared       : () => { this.handleChange(null); }
        }).mask(inputDate);
    }
    handleDateChange (day, { disabled, selected }) {
        if (!disabled) {
            const date = !selected ? moment(day, this.props.format || 'DD.MM.YYYY') : null;
            this.setState({ calendarIsOpened : false });
            this.handleChange(date);
        }
    }
    handleChange (date) {
        let value = (!!date && date.isValid()) ? date : null;
        super.handleChange(value);
        this.handleYearMonthChange(!!value ? new Date(value.valueOf()) : new Date());
    }
    render () {
        const cls = (!this.props.labelPosition) ? '' : (this.props.labelPosition == 'up') ? 'label-up' : 'label-left';
        const txt = !!this.props.value ? this.props.value.format(this.props.format || 'DD.MM.YYYY') : '';
        const cal = !!this.props.value ? new Date(this.props.value.valueOf())                       : null; // new Date();

        if (!!this.inputDate) this.inputDate.value = txt;
        return (
            <span className={`mdlp-common-control date ${cls}`}>
                { !!this.props.labelPosition && <label htmlFor={this.getElID()}>{this.props.title}</label> }
                <input type="text" id={this.getElID()} readOnly={this.state.calendarIsOpened}
                       defaultValue={txt} ref={this.setInputMask} onFocus={(e) => {
                            e.persist();
                            window.setTimeout(() => { e.target.select(); }, 50);
                       }}/>
                <div className={`mdlp-common-control-date-dropper ${this.state.calendarIsOpened ? 'opened' : ''}`}>
                    <span className="mdlp-common-control-date-dropper-btn" onClick={() => { this.setState({ calendarIsOpened : !this.state.calendarIsOpened }) }}></span>
                    <div className="mdlp-common-control-date-dropper-calendar">
                        <DayPicker locale="ru"
                                   localeUtils={this.dayPickerLocale}
                                   initialMonth={new Date()}
                                   month={ this.state.month }
                                   fromMonth={ this.props.minDate }
                                   toMonth={ this.props.maxDate }
                                   selectedDays={cal}
                                   disabledDays={ (day) => {
                                        const mday = Number(moment(day).format('YYYYMMDD'));
                                        const min  = Number(moment(this.props.minDate).format('YYYYMMDD'));
                                        const max  = Number(moment(this.props.maxDate).format('YYYYMMDD'));
                                        return (mday < min) || (mday > max);
                                   } }
                                   onDayClick={this.handleDateChange}
                                   captionElement={
                                        <YearMonthForm
                                            minDate={this.props.minDate}
                                            maxDate={this.props.maxDate}
                                            onChange={this.handleYearMonthChange}
                                        />
                                   }
                            />
                    </div>
                </div>
            </span>
        );
    }
}
function YearMonthForm({ date, minDate, maxDate, localeUtils, onChange }) {
    const months = localeUtils.getMonths();
    const years  = [];
    for (let i = minDate.getFullYear(); i <= maxDate.getFullYear(); i += 1) {
        years.push(i);
    }
    const handleChange = function handleChange(e) {
        const { year, month } = e.target.form;
        onChange(new Date(year.value, month.value));
    };
    return (
        <form className="DayPicker-Caption">
            <select className="default" name="month" onChange={ handleChange } value={ date.getMonth() }>
                { months.map((month, i) => {
                    const disabled = ((date.getFullYear() == minDate.getFullYear()) && (i < minDate.getMonth()))
                        || ((date.getFullYear() == maxDate.getFullYear()) && (i > maxDate.getMonth()));
                    return ( <option key={ i } value={ i } disabled={disabled}>{ month }</option> );
                })}
            </select>
            <select className="default" name="year" onChange={ handleChange } value={ date.getFullYear() }>
                { years.map((year, i) =>
                    <option key={ i } value={ year }>
                        { year }
                    </option>)
                }
            </select>
        </form>
    );
}

export class CommonSwitchButton extends CommonControl {
    // ReactDOM.findDOMNode(component)
    constructor (props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
    }
    handleChange (ev) {
        super.handleChange($(ev.target).is(':checked') ? this.props.checked.value : this.props.unchecked.value);
    }
    render () {
        return (
            <span className="mdlp-common-control switch">
                <input type="checkbox" id={this.getElID()}
                       checked={this.props.value === this.props.checked.value}
                       onChange={this.handleChange}/>
                <label htmlFor={this.getElID()}
                       data-title-unchecked={ this.props.unchecked.title }
                       data-title-checked={ this.props.checked.title }>
                    <span className="slider"></span>
                </label>
            </span>
        );
    }
}
