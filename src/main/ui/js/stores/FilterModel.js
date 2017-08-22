/**
 * Created by PBorisov on 27.03.2017.
 */

import moment from 'moment';
import * as queryString from 'query-string';

const searchPrms = queryString.parse(location.search);
const defaultPageSize = 20;

class FieldFilterModel {
    constructor (opts) {
        this.id          = opts.id || opts.name;
        this.name        = opts.name;
        this.title       = opts.title;
        this.curval      = opts.value;
        this.editval     = this.curval;
        this.filterTypes = opts.filterTypes || [];
    }
    getContentKey () {
        return `${ this.filterTypes.join('_') }${ !!this.filterTypes.length ? '_' : '' }${ this.name }`;
    }
    getKey () {
        return `${ this.filterTypes.join('_') }${ !!this.filterTypes.length ? '_' : '' }${this.name}:${this.id}`;
    }
    isChanged () { return this.toString('curval') != this.toString('editval'); }
    synch (from, to) { this[to] = this[from] }
    cur2edit () { this.synch('curval', 'editval'); }
    edit2cur () { this.synch('editval', 'curval'); }
    setEdit (value) { this.editval = value; }
    toString (from) { return this[from]; }
    serialize (from) { return this.toString(from); }
    state (from) { return { id : this.id, name : this.name, title : this.title, value : this[from], filterTypes : this.filterTypes }; }
}

class TextFilterModel extends FieldFilterModel {
    constructor (opts) {
        opts.value = opts.value || '';
        super(opts);
    }
    setEdit (value) { this.editval = (value != null) ? value : ''; }
    serialize (from) { return (this[from] != '') ? this.toString(from) : null; }
}

class DateFilterModel extends FieldFilterModel {
    constructor (opts) {
        opts.value = !!opts.value ? moment(opts.value) : null;
        super(opts);
    }
    toString (from) { return !!this[from] ? this[from].format('YYYY-MM-DD') : ''; }
    setEdit (value) { this.editval = !!value ? value : null; }
    serialize (from) { return !!this[from] ? this.toString(from) : null; }
}

class CheckBoxFilterModel extends FieldFilterModel {
    constructor (opts) {
        opts.value = !!opts.value;
        super(opts);
    }
    isChanged () { return (this.curval || this.editval) && !(this.curval && this.editval); }
    setEdit (value) { this.editval = !!value; }
    serialize (from) { return this[from] ? [this.id] : []; }
    state (from) { return [super.state(from)]; }
}

export default class FilterModel {
    constructor (opts) {
        this.types         = opts.types    || { DEFAULT : opts.rest };
        this.type          = opts.type     || Object.keys(this.types)[0] || null;
        this.userId        = opts.userId;
        this.name          = opts.name;
        this.nPage         = opts.nPage    || 1;
        this.pageSize      = opts.pageSize || defaultPageSize;
        this.contentsTable = {};
        this.contents      = (opts.params || []).map((item, index) => {
            const Builder = (item.type == 'date')  ? DateFilterModel
                :           (item.type == 'check') ? CheckBoxFilterModel
                : TextFilterModel;
            const field = new Builder(item);
            this.contentsTable[field.getKey()] = index;
            return field;
        });
        this.cache         = {};
        this.fromSearchParams(opts.params) || this.restore();
    }
    changeType (newType) {
        const oldType = this.type;
        if (this.types.hasOwnProperty(newType)) {
            this.cache[this.type] = { nPage : this.nPage, pageSize : this.pageSize };
            this.type     = newType;
            this.nPage    = !!this.cache[newType] ? this.cache[newType].nPage    : 1;
            this.pageSize = !!this.cache[newType] ? this.cache[newType].pageSize : defaultPageSize;
        }
        return oldType;
    }
    rest () {
        const cb = this.types[this.type];
        return cb.apply(null, arguments);
    }
    grepContents () {
        return !this.type
            ? this.contents
            : this.contents.filter( e => !e.filterTypes.length || (e.filterTypes.indexOf(this.type) >= 0) );
    }
    getKey (data) {
        //return `${data.name}:${data.id}`;
        return `${ data.filterTypes.join('_') }${ !!data.filterTypes.length ? '_' : '' }${data.name}:${data.id}`;
    }
    isChanged () {
        let isChanged = false;
        this.grepContents().forEach( e => isChanged = isChanged || e.isChanged() );
        return isChanged;
    }
    cur2edit () {
        this.grepContents().forEach( e => e.cur2edit() );
        return this;
    }
    edit2cur () {
        this.grepContents().forEach( e => e.edit2cur() );
        return this;
    }
    setEdit (data) {

        console.log('+++', data);

        this.contents[this.contentsTable[this.getKey(data)]].setEdit(data.value);
        return this;
    }
    setNPage (nPage) {
        this.nPage = !!nPage ? nPage : this.nPage;
        return this;
    }
    serialize (from, nPage) {
        let ser = { nPage : nPage || this.nPage, pageSize : this.pageSize };
        if (!!this.type && (this.type !== 'DEFAULT')) ser.filterType = this.type;
        return this.grepContents().reduce((h, field) => {
            const val  = field.serialize(from);
            const name = field.name;
            if (val == null) return h;
            if (!h.hasOwnProperty(name)) {
                h[name] = val;
            } else {
                if (!(h[name] instanceof Array)) h[name] = [h[name]];
                h[name] = h[name].concat(val);
            }
            return h;
        }, ser);
    }
    state (from) {
        return this.contents.reduce((h, field) => {
            const state = field.state(from);
            const name  = field.getContentKey(); // field.name;
            if (!h.hasOwnProperty(name)) {
                h[name] = state;
            } else {
                if (!(h[name] instanceof Array)) h[name] = [h[name]];
                h[name] = h[name].concat(state);
            }
            return h;
        }, { filterType : this.type, isChanged : this.isChanged(), nPage : this.nPage, pageSize : this.pageSize });
    }
    fromSearchParams (fields) {
        const params = { ...searchPrms };
        const setVal = (key, val) => {
            const v = this.contents[ this.contentsTable[key] ];
            if (!!v) { v.curval  = val; v.editval = val; }
        };
        const tmp = (fields || []).reduce((h, field, i) => {
            if (!h.hasOwnProperty(field.name) && params.hasOwnProperty(field.name)) {
                const tmp = (field.type === 'check')
                    ? [].concat(params[field.name]).forEach((v, i) => { setVal(`${field.name}:${v}`, true) })
                    : setVal(`${field.name}:${field.name}`, params[field.name]);
                h[field.name] = true;
            }
            return h;
        }, {});
        return !!Object.keys(tmp).length;
    }
    restore () {
        /* Убираем сохранение/восстановление фильтров из локального хранилища */
        //if (!window.localStorage || !window.JSON) return false;
        //const pers = window.JSON.parse(window.localStorage.getItem(`${this.userId}-${this.name}`) || '{}');
        //this.setNPage(pers.nPage);
        //Object.keys(this.contentsTable).forEach((k) => {
        //    const p = pers[k];
        //    const v = this.contents[ this.contentsTable[k] ];
        //    if ( !!p && !!v ) {
        //        v.curval  = p.curval;
        //        v.editval = p.editval;
        //    }
        //});
        return this;
    }
    save () {
        /* Убираем сохранение/восстановление фильтров из локального хранилища */
        //if (!window.localStorage || !window.JSON) return false;
        //window.localStorage.setItem(`${this.userId}-${this.name}`, window.JSON.stringify( (() => {
        //    let pers = { nPage : this.nPage };
        //    Object.keys(this.contentsTable).forEach((k) => {
        //        const { curval, editval } = this.contents[ this.contentsTable[k] ];
        //        pers[k] = { curval, editval };
        //    });
        //    return pers;
        //})() ));
        return this;
    }
}
