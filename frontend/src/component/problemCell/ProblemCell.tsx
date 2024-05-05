import {observer} from "mobx-react-lite";
import React from "react";
import "./problemCell.css"
import {Link} from "react-router-dom";
import {Problem} from "../../models/entity/Problem";

interface IProps {
    problem: Problem;
}

const ProblemCell: React.FC<IProps> = ({problem}) => {

    return (
        <div className={"problem-cell"}>
            <div className={"problem-inner"}>
                <Link className={"problem-name"} to={`/task/${problem.id}`}>{problem.name}</Link>
                <p className={"problem-language"}>{problem.language.name}</p>
            </div>
        </div>
    );
};

export default observer(ProblemCell);