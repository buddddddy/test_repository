/**
 * Created by PBorisov on 17.03.2017.
 */

import React, { PropTypes, Component } from 'react';
import * as queryString                from 'query-string';

export default class LoginFormView extends Component {
    constructor (props) {
        super(props);
        this.state = {
            login    : '',
            password : '',
            isFull   : false,
            error    : queryString.parse(location.search).error
        };
        this.handleLoginChange    = this.handleLoginChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
    }
    handleLoginChange (ev) {
        let login = ev.target.value;
        this.setState((prevState, props) => {
            return { login, isFull : (!!login && !!prevState.password) };
        });
    }
    handlePasswordChange (ev) {
        let password = ev.target.value;
        this.setState((prevState, props) => {
            return { password, isFull : (!!password && !!prevState.login) };
        });
    }
    render () {
        return (
            <div className="mdlp-login-form-container">
                <h2 className="mdlp-login-form-title">Вход в систему</h2>
                { !!this.state.error ? <p className="mdlp-login-form-error">Логин/пароль указаны неверно</p> : '' }
                <form action="/login" method="post">
                    <input
                        placeholder="Логин"
                        className="mdlp-login-form-control"
                        type="text"
                        name="login"
                        value={ this.state.login }
                        onChange={ this.handleLoginChange }/>
                    <input
                        placeholder="Пароль"
                        className="mdlp-login-form-control"
                        type="password"
                        name="password"
                        value={ this.state.password }
                        onChange={ this.handlePasswordChange }/>
                    <input className="mdlp-login-form-submit" type="submit" value="Войти" disabled={!this.state.isFull}/>
                </form>
            </div>
        );
    }
}
