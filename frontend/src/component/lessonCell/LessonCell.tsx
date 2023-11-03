import {observer} from "mobx-react-lite";
import React from "react";
import "./lessonCell.css"
import {Link} from "react-router-dom";
import {ILesson} from "../../models/entity/ILesson";

interface IProps {
    lesson: ILesson;
}

const LessonCell: React.FC<IProps> = ({lesson}) => {

    return (
        <div className={"lesson-cell"}>
            <div className={"test-inner"}>
                <Link className={"test-name"} to={`/lesson/${lesson.id}`}>{lesson.name}</Link>
            </div>
        </div>
    );
};

export default observer(LessonCell);