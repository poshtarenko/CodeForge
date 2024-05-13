import React, {useEffect, useState} from 'react';
import "./lessonPage.css"
import {useNavigate, useParams} from "react-router-dom";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {ILesson} from "../../models/entity/ILesson";
import LessonService from "../../services/LessonService";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import {ILanguage} from "../../models/entity/ILanguage";
import LanguageService from "../../services/LanguageService";
import LessonSessionPage from "../lessonSessionPage/lessonSessionPage";

const LessonPage: React.FC = () => {

    const {id} = useParams<string>();

    const [lesson, setLesson] = useState<ILesson>({} as ILesson);
    const [changingName, setChangingName] = useState<boolean>(false);
    const [newName, setNewName] = useState<string>("");
    const [languages, setLanguages] = useState<ILanguage[]>([]);


    let navigate = useNavigate();

    async function navigateToSession() {
        navigate("/lesson/session/" + id)
    }

    useEffect(() => {
        loadLesson()
        loadLanguages()
    }, []);

    async function loadLesson() {
        try {
            const responseLesson = await LessonService.getLesson(Number(id));
            setLesson(responseLesson.data);
            setNewName(responseLesson.data.name);
        } catch (e) {
            console.log(e);
        }
    }

    async function loadLanguages() {
        try {
            const languages = await LanguageService.getAllLanguages();
            setLanguages(languages.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function selectLanguage(id : number) {
        try {
            lesson.language = languages.find(l => l.id === id)!
            await LessonService.updateLesson(lesson.id, {name: newName, languageId: id});
            loadLesson();
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
                <div className={"language-select"}>
                    <p>Мова програмування</p>
                    <select onChange={(e) => selectLanguage(Number(e.target.value))}
                            defaultValue={"NONE"}>
                        <option value="NONE" disabled>Мова</option>
                        {languages.map(language =>
                            <option key={Number(language.id)} value={String(language.id)} selected={language.id === lesson?.language.id}>
                                {language.name}
                            </option>)}
                    </select>
                </div>
                <button onClick={() => navigateToSession()} className={"standard-button"}>Підключитися до заняття
                </button>
            </div>
        </PageTemplate>
    );

}

export default LessonPage;