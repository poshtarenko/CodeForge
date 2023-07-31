import React, {useEffect} from "react";
import {observer} from "mobx-react-lite";
import {BrowserRouter} from "react-router-dom";
import AppRouter from "./router/AppRouter";
import "./style/main.css";

function App() {

    useEffect(() => {
        document.title = "Codeforge";
    }, []);

    return (
        <BrowserRouter>
            <AppRouter/>
        </BrowserRouter>
    )
}

export default observer(App);