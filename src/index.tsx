import React, {createContext} from 'react';
import * as ReactDOMClient from 'react-dom/client';
import App from "./App";
import Store from "./store/store";

interface State {
    store: Store;
}

const store = new Store();

export const Context = createContext<State>({store});

const root = ReactDOMClient.createRoot(document.getElementById("root")!);
root.render(
    <Context.Provider value={{store}}>
        <App/>
    </Context.Provider>
);