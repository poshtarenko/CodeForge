import React, {useEffect} from "react";
import {observer} from "mobx-react-lite";
import {BrowserRouter} from "react-router-dom";
import AppRouter from "./router/AppRouter";
import "./style/main.css";
import {StompSessionProvider} from "react-stomp-hooks";

function App() {

    useEffect(() => {
        document.title = "CodeForge";
    }, []);

    return (
        <StompSessionProvider url={"http://localhost:8080/ws"}>
            <BrowserRouter>
                <AppRouter/>
            </BrowserRouter>
        </StompSessionProvider>
    )
}

export default observer(App);