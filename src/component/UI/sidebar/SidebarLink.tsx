import React, {useContext} from "react";
import {Context} from "../../../index";
import {Link} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPersonChalkboard, faRightFromBracket} from "@fortawesome/free-solid-svg-icons";
import {observer} from "mobx-react-lite";

interface IProps {
    setActive: (active: boolean) => void,
    children: React.ReactNode,
}

const SidebarLink: React.FC<IProps> = ({setActive, children}) => {

    return (
        <div onClick={() => setActive(false)}>
            {children}
        </div>
    );
};

export default observer(SidebarLink);