import React, {useContext, useState} from 'react';
import './auth.css';
import {Context} from "../../index";
import {Link} from "react-router-dom";
import {observer} from "mobx-react-lite";

const LoginPage: React.FC = () => {

    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const {store} = useContext(Context);


    return (
        <div className="auth-wrapper">
            <div className="auth-block login-block">
                <p className="app-name">CodeForge</p>
                <p className="auth-word">Авторизація</p>
                <form className="auth-form">
                    <label htmlFor="email">Електронна пошта</label>
                    <input className={"standard-input"} onChange={(e) => setEmail(e.target.value)} type="email"
                           id="email"/>
                    <label htmlFor="password">Пароль</label>
                    <input className={"standard-input"} onChange={(e) => setPassword(e.target.value)} type="password"
                           id="password"/>
                    <button className="standard-button" type={"button"}
                            onClick={() => store.login(email, password)}>Увійти
                    </button>
                    <p className="help-text">Немає акаунту? <Link to={"/register"}>Зареєструйтесь</Link></p>
                </form>
            </div>
        </div>
    );
}

export default observer(LoginPage);