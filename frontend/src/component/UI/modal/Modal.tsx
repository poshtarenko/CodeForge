import {observer} from "mobx-react-lite";
import React from "react";
import "./modal.css";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faXmark} from "@fortawesome/free-solid-svg-icons";

interface IProps {
    active: boolean,
    setActive: (active: boolean) => void,
    children: React.ReactNode
}

const Modal: React.FC<IProps> = ({children, active, setActive}) => {

    return (
        <div className={active ? "modal active" : "modal"}>
            <div className="modal-content">
                <FontAwesomeIcon className={"close-button"} icon={faXmark} onClick={() => setActive(false)}/>
                {children}
            </div>
        </div>
    );
};

export default observer(Modal);