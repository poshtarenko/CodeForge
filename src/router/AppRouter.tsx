import React, {useContext, useEffect, useState} from 'react';
import {Navigate, Route, Routes} from "react-router-dom";
import {authorRoutes, IRoute, publicRoutes, respondentRoutes} from "./Routes";
import {Context} from "../index";
import {observer} from "mobx-react-lite";

const AppRouter: React.FC = () => {
    const {store} = useContext(Context);
    const [isLoading, setIsLoading] = useState<boolean>(true);


    useEffect(() => {
        loadAuth();
    });

    async function loadAuth() {
        if (localStorage.getItem('token')) {
            await store.checkAuth();
        }
        setIsLoading(false);
    }

    if (isLoading) {
        return (<div>LOADING...</div>)
    }

    function listToRoutes(routes: IRoute[], redirectLink: string) {
        return (
            <Routes>
                {routes.map(route =>
                    <Route
                        element=<route.component/>
                        path={route.path}
                        key={route.path}
                    />
                )}
                <Route path="*" element={<Navigate to={redirectLink} replace/>}/>
            </Routes>
        );
    }

    function getRoutesByRole(role: string) : JSX.Element{
        if (role === "AUTHOR") {
            return listToRoutes(authorRoutes, "/tests");
        } else if (role === "RESPONDENT") {
            return listToRoutes(respondentRoutes, "/start");
        }
        return <></>
    }

    return (
        store.isAuth
            ?
            getRoutesByRole(store.role)
            :
            <Routes>
                {publicRoutes.map(route =>
                    <Route
                        element=<route.component/>
                        path={route.path}
                        key={route.path}
                    />
                )}
                <Route path="*" element={<Navigate to="/login" replace/>}/>
            </Routes>
    );
};

export default observer(AppRouter);