import React, {useEffect, useState} from 'react';
import TestService from "../../services/TestService";
import {ITest} from "../../models/entity/ITest";
import "./testPage.css"
import {useParams} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faPenToSquare} from "@fortawesome/free-solid-svg-icons";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import TasksMenu from "./tasksMenu/TasksMenu";
import AnswersMenu from "./resultsMenu/AnswersMenu";
import AnswerService from "../../services/AnswerService";
import {IAnswer, ISolution} from "../../models/entity/IAnswer";
import {ISolutionResult} from "../../models/entity/ISolutionResult";

enum ActiveMenu {
    TasksMenu,
    ResultsMenu
}

const TestPage: React.FC = () => {

    const {id} = useParams<string>();

    const [test, setTest] = useState<ITest>({} as ITest);
    const [changingName, setChangingName] = useState<boolean>(false);
    const [newName, setNewName] = useState<string>("");
    const [activeMenu, setActiveMenu] = useState<ActiveMenu>(ActiveMenu.TasksMenu);
    const [answers, setAnswers] = useState<IAnswer[]>([]);

    useEffect(() => {
        loadAll();
    }, []);

    async function loadAll() {
        loadTest();
        loadAnswers();
    }

    async function loadTest() {
        try {
            const responseTest = await TestService.getTestAsAuthor(Number(id));
            setTest(responseTest.data);
            setNewName(responseTest.data.name);
        } catch (e) {
            console.log(e);
        }
    }

    async function loadAnswers() {
        try {
            const responseAnswers = await AnswerService.findTestAnswers(Number(id));
            setAnswers(responseAnswers.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function updateName(newName: string) {
        try {
            setChangingName(false);
            await TestService.updateTest(Number(test.id), {name: newName, maxDuration: 100});
            loadTest();
        } catch (e) {
            console.log(e);
        }
    }

    function fillSolutionsWithNotPassed(answers: IAnswer[]): IAnswer[] {
        let result: IAnswer[] = answers;
        result.forEach(answer => {
            test.tasks.forEach(task => {
                if (!answer.solutions.find(s => s.task.id === task.id)) {
                    const solutionResult: ISolutionResult = {isCompleted: false, error: "Нема відповіді"}
                    const solution: ISolution = {id: 0, code: "", solutionResult: solutionResult, task: task}
                    answer.solutions.push(solution)
                }
            })
        })
        return result
    }

    return (
        <PageTemplate>
            <div className={"test-page"}>
                <div className={"test-title-block"}>
                    <div>
                        {changingName ?
                            <div className={"change-test-name-block"}>
                                <input onChange={(e) => setNewName(e.target.value)}
                                       className={"test-name-input"}
                                       value={newName}/>
                                <FontAwesomeIcon onClick={() => updateName(newName)}
                                                 className={"ok-button"}
                                                 icon={faCheck}/>
                            </div>
                            :
                            <div className={"test-name-block"}>
                                <p className={"test-name"}>{test.name}</p>
                                <FontAwesomeIcon onClick={() => setChangingName(true)}
                                                 className={"change-button"}
                                                 icon={faPenToSquare}/>
                            </div>
                        }
                        <p className={"test-lang"}>Код для входу : {test.inviteCode}</p>
                    </div>
                    <div>
                        <p className={"test-tasks-count"}>
                            <span className={"number"}>{TestService.countTestTasks(test)}</span> завдань
                        </p>
                        <p className={"test-max-score"}>
                            <span className={"number"}>{TestService.calcTotalMaxScore(test)}</span> балів
                        </p>
                    </div>
                </div>
                <div className="menu-selector">
                    <div className="menu-selector-el" onClick={() => setActiveMenu(ActiveMenu.TasksMenu)}>
                        <p className={activeMenu === ActiveMenu.TasksMenu ? "selected" : ""}>Завдання</p>
                        <div className={activeMenu === ActiveMenu.TasksMenu ? "underline selected" : "underline"}></div>
                    </div>
                    <div className="menu-selector-el" onClick={() => setActiveMenu(ActiveMenu.ResultsMenu)}>
                        <p className={activeMenu === ActiveMenu.ResultsMenu ? "selected" : ""}>Відповіді</p>
                        <div
                            className={activeMenu === ActiveMenu.ResultsMenu ? "underline selected" : "underline"}></div>
                    </div>
                </div>
                <TasksMenu isActive={activeMenu === ActiveMenu.TasksMenu} test={test} reloadFunc={loadTest}/>
                <AnswersMenu isActive={activeMenu === ActiveMenu.ResultsMenu}
                             answers={fillSolutionsWithNotPassed(answers)}/>
            </div>
        </PageTemplate>
    );

}

export default TestPage;