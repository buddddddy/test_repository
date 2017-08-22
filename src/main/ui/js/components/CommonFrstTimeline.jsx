/**
 * Created by PBorisov on 29.03.2017.
 */

import React, { Component } from 'react';
import $                    from 'jquery';
import moment               from 'moment';

class CommonFrstTimelineBlock extends Component {
    render () {
        const side = !!(this.prop.index % 2) ? 'even' : 'odd';
        return (
            <div className={`frst-timeline-block frst-${side}-item`} data-animation="slideInUp">
            </div>
        );
    }
}

export default class CommonFrstTimeline extends Component {
    render () {
        return (
            <div className="frst-container" data-animation-name="slideInUp">
                <div className="frst-timeline frst-timeline-style-1 frst-alternate frst-date-opposite">
                    { this.props.children.map((item, index) => {
                        return (
                            <CommonFrstTimelineBlock key={index} index={index}>{item}</CommonFrstTimelineBlock>
                        );
                    }) }
                </div>
            </div>
        );
    }
}
