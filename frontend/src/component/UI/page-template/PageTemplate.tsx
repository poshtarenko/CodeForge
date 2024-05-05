import {observer} from "mobx-react-lite";
import React, {useState} from "react";
import Header from "../header/Header";
import Sidebar from "../sidebar/Sidebar";
import "./pageTemplate.css";
import {StompSessionProvider} from "react-stomp-hooks";

interface IProps {
    children: React.ReactNode
}

const PageTemplate: React.FC<IProps> = ({children}) => {

    const [sidebarOn, setSidebarOn] = useState<boolean>(false);

    return (
        <StompSessionProvider url={"http://localhost:8080/ws"}>
            <React.Fragment>
                <Sidebar isActive={sidebarOn} setActive={setSidebarOn}/>
                <div className={"main-content"} onClick={() => setSidebarOn(false)}>
                    <Header setSidebarOn={setSidebarOn}/>
                    {children}
                </div>
            </React.Fragment>
        </StompSessionProvider>
    );
};

export default observer(PageTemplate);