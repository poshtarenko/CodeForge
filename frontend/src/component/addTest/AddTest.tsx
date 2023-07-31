import {observer} from "mobx-react-lite";
import React, {useState} from "react";
import "./addTest.css";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import TestService from "../../services/TestService";

interface IProps {
    addTest: () => void;
}

const AddTest: React.FC<IProps> = ({addTest}) => {
    const [active, setActive] = useState<boolean>(false);
    const [testName, setTestName] = useState<string>("");

    async function createTest() {
        await TestService.createTest({name: testName, maxDuration: 10500});
        addTest();
        setActive(false);
    }

    return (
        active
            ?
            <div className={"add-test-active"}>
                <div className={"inner"}>
                    <p className={"add-test-title"}>Новий тест</p>
                    <input onChange={(e) => setTestName(e.target.value)}
                           className={"test-name-input standard-input"}
                           type="text"
                           placeholder={"Назва"}
                    />
                    <div className={"buttons"}>
                        <button className={"standard-button"} onClick={() => setActive(false)}>Скасувати</button>
                        <button onClick={() => createTest()}
                                className={"standard-button"}>Створити
                        </button>
                    </div>
                </div>
            </div>
            :
            <div className={"add-test-inactive"} onClick={() => setActive(true)}>
                <FontAwesomeIcon className={"add-icon"} icon={faPlus}/>
            </div>
    );
};

export default observer(AddTest);