import {observer} from "mobx-react-lite";
import "./header.css";
import React, {useState} from "react";
import {faBars} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Sidebar from "../sidebar/Sidebar";

interface IProps {
    setSidebarOn: (active: boolean) => void,
}

const Header: React.FC<IProps> = ({setSidebarOn}) => {

    return (
        <>
            <div className={"header-wrapper"}>
                <div className={"header"}>
                    <FontAwesomeIcon onClick={(e) => {e.stopPropagation(); setSidebarOn(true)}}
                                     className={"menu-button"} icon={faBars}/>
                    <p className={"header-app-name"}>Codeforge</p>
                </div>
            </div>
        </>
    );
};

export default observer(Header);