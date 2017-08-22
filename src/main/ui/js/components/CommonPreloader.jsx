/**
 * Created by PBorisov on 22.03.2017.
 */

import React, { Component } from 'react';

export default class CommonPreloader extends Component {
    render () {
        const stl = { display : this.props.visible ? 'block' : 'none' };
        return (
            <div className="mdlp-common-preloader" style={ stl }></div>
        );
    }
}
