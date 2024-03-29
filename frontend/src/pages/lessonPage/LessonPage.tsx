import React, {useEffect, useState} from 'react';
import "./lessonPage.css"
import {useNavigate, useParams} from "react-router-dom";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {ILesson} from "../../models/entity/ILesson";
import LessonService from "../../services/LessonService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faPenToSquare} from "@fortawesome/free-solid-svg-icons";

const LessonPage: React.FC = () => {

    const {id} = useParams<string>();

    const [lesson, setLesson] = useState<ILesson>({} as ILesson);
    const [changingName, setChangingName] = useState<boolean>(false);
    const [newName, setNewName] = useState<string>("");


    let navigate = useNavigate();

    async function navigateToSession() {
        navigate("/lesson/session/" + id)
    }

    useEffect(() => {
        loadAll();
    }, []);

    async function loadAll() {
        loadLesson();
    }

    async function loadLesson() {
        try {
            const responseLesson = await LessonService.getLessonAsAuthor(Number(id));
            setLesson(responseLesson.data);
            setNewName(responseLesson.data.name);
        } catch (e) {
            console.log(e);
        }
    }

    async function updateName(newName: string) {
        try {
            setChangingName(false);
            await LessonService.updateLesson(lesson.id, {name: newName, languageId: null});
            loadLesson();
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <PageTemplate>
            <div className={"lesson-page"}>
                <div className={"lesson-title-block"}>
                    <div>
                        {changingName ?
                            <div className={"change-lesson-name-block"}>
                                <input onChange={(e) => setNewName(e.target.value)}
                                       className={"lesson-name-input"}
                                       value={newName}/>
                                <FontAwesomeIcon onClick={() => updateName(newName)}
                                                 className={"ok-button"}
                                                 icon={faCheck}/>
                            </div>
                            :
                            <div className={"lesson-name-block"}>
                                <p className={"lesson-name"}>{lesson.name}</p>
                                <FontAwesomeIcon onClick={() => setChangingName(true)}
                                                 className={"change-button"}
                                                 icon={faPenToSquare}/>
                            </div>
                        }
                        <p className={"lesson-lang"}>Код для входу : {lesson.inviteCode}</p>
                    </div>
                </div>
                <button onClick={() => navigateToSession()} className={"standard-button"}>Підключитися до заняття
                </button>
            </div>
        </PageTemplate>
    );

}

export default LessonPage;