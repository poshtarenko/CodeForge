import React from "react";
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