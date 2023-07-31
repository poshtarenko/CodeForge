import React, {useContext, useState} from 'react';
import './auth.css';
import {Context} from "../../index";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChalkboardUser, faGraduationCap} from "@fortawesome/free-solid-svg-icons";
import {Link} from "react-router-dom";

const RegistrationPage: React.FC = () => {

    const [email, setEmail] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const [username, setUsername] = useState<string>("");
    const [role, setRole] = useState<string>("");

    const {store} = useContext(Context);

    function changeRole(div: HTMLDivElement) {

        const selectedDiv = div;
        let anotherDiv;

        if (div.id === "student-role") {
            setRole("RESPONDENT");
            anotherDiv = document.getElementById('organizer-role');
        } else if (div.id === "organizer-role") {
            setRole("AUTHOR");
            anotherDiv = document.getElementById('student-role');
        }
        selectedDiv.classList.add("selected-role-wrapper");
        selectedDiv.classList.remove("unselected-role-wrapper");

        anotherDiv?.classList.remove("selected-role-wrapper");
        anotherDiv?.classList.add("unselected-role-wrapper");
    }

    return (
        <div className="auth-wrapper">
            <div className="auth-block register-block">
                <p className="auth-word">Реєстрація</p>
                <form className="auth-form">
                    <label htmlFor="email">Електронна пошта</label>
                    <input className="standard-input" onChange={(e) => setEmail(e.target.value)} type="email"
                           id="email"/>
                    <label htmlFor="name">Ім'я</label>
                    <input className="standard-input" onChange={(e) => setUsername(e.target.value)} type="text"
                           id="name"/>
                    <label htmlFor="password">Пароль</label>
                    <input className="standard-input" onChange={(e) => setPassword(e.target.value)} type="password"
                           id="password"/>
                    <div className="role-picker">
                        <div onClick={(e) => {
                            changeRole(e.currentTarget as HTMLDivElement)
                        }}
                             className="role-wrapper role-wrapper-left unselected-role-wrapper"
                             id="organizer-role">
                            <FontAwesomeIcon className={"role-icon"} icon={faChalkboardUser}/>
                            <p className="role-name">Я організатор</p>
                        </div>
                        <div onClick={(e) => changeRole(e.currentTarget as HTMLDivElement)}
                             className="role-wrapper role-wrapper-right unselected-role-wrapper"
                             id="student-role">
                            <FontAwesomeIcon className={"role-icon"} icon={faGraduationCap}/>
                            <p className="role-name">Я студент</p>
                        </div>
                    </div>
                    <button onClick={() => store.register({email, password, username, role})}
                            className="standard-button"
                            type={"button"}>
                        Реєстрація
                    </button>
                    <p className="help-text">Вже зареєстровані? <Link to={"/login"}>Авторизуйтесь</Link></p>
                </form>
            </div>
        </div>
    );

}

export default RegistrationPage;