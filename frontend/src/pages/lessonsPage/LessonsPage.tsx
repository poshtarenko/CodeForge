import React, {useEffect, useState} from 'react';
import TestService from "../../services/TestService";
import {ITest} from "../../models/entity/ITest";
import "./lessonsPage.css"
import Test from "../../component/testCell/TestCell";
import AddTest from "../../component/addTestCell/AddTestCell";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {ILesson} from "../../models/entity/ILesson";
import LessonService from "../../services/LessonService";
import LessonCell from "../../component/lessonCell/LessonCell";
import AddLesson from "../../component/addLesson/AddLesson";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPersonChalkboard, faUsers} from "@fortawesome/free-solid-svg-icons";

function LessonsPage() {

    const [lessons, setLessons] = useState<ILesson[]>([]);

    useEffect(() => {
        getLessons();
    }, []);

    async function getLessons() {
        try {
            const response = await LessonService.getAuthorLessons();
            setLessons(response.data);
            console.log(response.data);
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <PageTemplate>
            <div className={"app-section"}>
                <div className={"section-title"}>
                    <FontAwesomeIcon className={"section-icon"} icon={faUsers} />
                    <p className={"section-name"}>Мої заняття</p>
                </div>
                <div className={"tests"}>
                    {lessons.map(lesson =>
                        <LessonCell lesson={lesson} key={Number(lesson.id)}/>
                    )}
                    <AddLesson addLesson={getLessons}/>
                </div>
            </div>
        </PageTemplate>
    );

}

export default LessonsPage;