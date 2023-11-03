import {observer} from "mobx-react-lite";
import React, {useState} from "react";
import "./addLesson.css";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import LessonService from "../../services/LessonService";

interface IProps {
    addLesson: () => void;
}

const AddLesson: React.FC<IProps> = ({addLesson}) => {
    const [active, setActive] = useState<boolean>(false);
    const [lessonName, setLessonName] = useState<string>("");

    async function createLesson() {
        await LessonService.createLesson({name: lessonName});
        addLesson();
        setActive(false);
    }

    return (
        active
            ?
            <div className={"add-lesson-active"}>
                <div className={"inner"}>
                    <p className={"add-lesson-title"}>Нове заняття</p>
                    <input onChange={(e) => setLessonName(e.target.value)}
                           className={"lesson-name-input standard-input"}
                           type="text"
                           placeholder={"Назва"}
                    />
                    <div className={"buttons"}>
                        <button className={"standard-button"} onClick={() => setActive(false)}>Скасувати</button>
                        <button onClick={() => createLesson()}
                                className={"standard-button"}>Створити
                        </button>
                    </div>
                </div>
            </div>
            :
            <div className={"add-lesson-inactive"} onClick={() => setActive(true)}>
                <FontAwesomeIcon className={"add-icon"} icon={faPlus}/>
            </div>
    );
};

export default observer(AddLesson);