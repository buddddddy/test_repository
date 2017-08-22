/**
 * Created by PBorisov on 18.04.2017.
 */

import $      from 'jquery';
import moment from 'moment';

class RegionsAnalyticsItem {
    constructor (code, key) {
        this.code     = code;
        this.key      = key;
        this.current  = false;
        this.selected = false;
        this.data     = null;
        this.error    = null;
        this.expTime  = 0;
    }
    isEmpty () {
        return !this.data || (this.expTime < moment().valueOf());
    }
    clear () {
        this.expTime  = 0;
        this.deferred = null;
        this.data     = null;
        this.error    = null;
        return this;
    }
    setData (data) {
        return $.extend(this, { data, error : null, expTime : moment().add(30, 'minutes').valueOf() });
    }
    getData () { return this.isEmpty() ? null : this.data; }
    setError (error) {
        return $.extend(this, { error, data : null, expTime : 0 });
    }
    getError () { return this.error; }
    setCurrent (isCurrent) { this.current = !!isCurrent; return this; }
    isCurrent () { return this.current; }
    setSelected (isSelected) { this.selected = !!isSelected; return this; }
    isSelected () { return this.selected; }
    dto () {
        const { code, name, mapPoint, data, error } = this;
        return { code, name, mapPoint, data, error };
    }
}

export default class RegionsAnalyticsModel {
    constructor (opts) {
        this.rest  = opts.rest;
        this.items = {};
    }
    newItem (code, key) {
        this.items[code] = this.items[code] || new RegionsAnalyticsItem(code, key);
        return this.items[code];
    }
    getItem (code) {
        return this.items[code] || null;
    }
    getCurrent () {
        for (var code of Object.keys(this.items))
            if (this.items[code].isCurrent()) return this.items[code];
        return null;
    }
    setCurrent (item) {
        const current = this.getCurrent();
        !!current && current.setCurrent(false);
        item.setCurrent(true);
    }
    getSelected () {
        for (var code of Object.keys(this.items))
            if (this.items[code].isSelected()) return this.items[code];
        return null;
    }
    setSelected (item) {
        const selected = this.getSelected();
        !!selected && selected.setSelected(false);
        item.setSelected(true);
    }
}
