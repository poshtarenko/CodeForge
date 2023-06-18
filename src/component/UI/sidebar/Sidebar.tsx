import {observer} from "mobx-react-lite";
import "./sidebar.css";
import React, {useContext} from "react";
import {faPersonChalkboard, faRightFromBracket} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Context} from "../../../index";
import {Link} from "react-router-dom";
import SidebarLink from "./SidebarLink";
import {faPlay} from "@fortawesome/free-solid-svg-icons/faPlay";

interface IProps {
    isActive: boolean,
    setActive: (active: boolean) => void,
}

const Sidebar: React.FC<IProps> = ({isActive, setActive}) => {

    const {store} = useContext(Context);

    console.log(store.role)

    return (
        <div className={isActive ? "sidebar active" : "sidebar"}>
            <p className={"sidebar-app-name"}>Codeforge</p>
            <SidebarLink setActive={setActive}>
                {store.role === "AUTHOR" ?
                    <Link className={"sidebar-link"} to={"/testsPage"}>
                        <FontAwesomeIcon className={"sidebar-icon"} icon={faPersonChalkboard} />
                        <p>Мої тести</p>
                    </Link>
                    : null
                }
                {store.role === "RESPONDENT" ?
                    <Link className={"sidebar-link"} to={"/start"}>
                        <FontAwesomeIcon className={"sidebar-icon"} icon={faPlay} />
                        <p>Почати теcт</p>
                    </Link>
                    : null
                }
            </SidebarLink>
            <SidebarLink setActive={setActive}>
                <div className={"sidebar-link"} onClick={() => store.logout()}>
                    <FontAwesomeIcon className={"sidebar-icon"} icon={faRightFromBracket} />
                    <p>Вийти</p>
                </div>
            </SidebarLink>
        </div>
    );
};

export default observer(Sidebar);