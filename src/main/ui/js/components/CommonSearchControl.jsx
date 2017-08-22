/**
 * Created by PBorisov on 13.04.2017.
 */

import React, { Component } from 'react';
import $                    from 'jquery';
import Inputmask            from 'inputmask';
import { appMenu }          from '../actions/common_utils.js';
import 'jquery.inputmask';
import 'inputmask.extensions';

export default class CommonSearchControl extends Component {
    constructor (props) {
        super(props);
        this.state        = { isReady : false };
        this.setInputMask = this.setInputMask.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    setInputMask (input) {
        !!this.props.mask && Inputmask(this.props.mask, {
            definitions  : { '#': { validator : '[A-Z0-9]', cardinality : 1, prevalidator : null } },
            oncomplete   : () => { this.setState({ isReady :  true }) },
            onincomplete : () => { this.setState({ isReady : false }) },
            oncleared    : () => { this.setState({ isReady : false }) }
        }).mask(input);
    }
    handleSubmit (e) {
        e.preventDefault();
        e.stopPropagation();
        window.location.href = this.props.flink(true, e.target[this.props.name].value).link;
    }
    render () {
        return (
            <form className="mdlp-common-control mdlp-common-search-control" onSubmit={ this.handleSubmit }>
                <input className="default" type="text" name={this.props.name} placeholder={ this.props.title } ref={this.setInputMask} />
                <input className="" type="submit" value="" disabled={ !this.state.isReady }/>
            </form>
        );
    }
}
