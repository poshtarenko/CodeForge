import {observer} from "mobx-react-lite";
import React from "react";
import {ITest} from "../../models/entity/ITest";
import "./testCell.css"
import {Link} from "react-router-dom";
import TestService from "../../services/TestService";

interface IProps {
    test: ITest;
}

const TestCell: React.FC<IProps> = ({test}) => {

    return (
        <div className={"test-cell"}>
            <div className={"test-inner"}>
                <Link className={"test-name"} to={`/test/${test.id}`}>{test.name}</Link>
                <p className={"test-lang"}>{TestService.getLanguages(test)}</p>
                <p className={"test-tasks-count"}><span
                    className={"number"}>{TestService.getTasksCount(test)}</span> завдань</p>
                <p className={"test-max-score"}><span className={"number"}>{TestService.getMaxScore(test)}</span> балів
                </p>
            </div>
        </div>
    );
};

export default observer(TestCell);