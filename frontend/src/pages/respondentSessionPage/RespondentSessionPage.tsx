import React, {useEffect, useRef, useState} from 'react';
import TestService from "../../services/TestService";
import {ITask, ITest} from "../../models/entity/ITest";
import "./respondentSessionPage.css"
import {useParams} from "react-router-dom";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import SolutionService from "../../services/SolutionService";
import {IAnswer, ISolution} from "../../models/entity/IAnswer";
import AnswerService from "../../services/AnswerService";

interface AnswerDetails {
    code: string,
    taskCompletionStatus: string,
}

const RespondentSessionPage: React.FC = () => {

    const [test, setTest] = useState<ITest>({} as ITest);
    const [selectedTaskId, setSelectedTaskId] = useState<number>(0);
    const [solutions, setSolutions] = useState(new Map<number, AnswerDetails>());

    const [testFinished, setTestFinished] = useState<boolean>(false);
    const [answer, setAnswer] = useState<IAnswer>({} as IAnswer);

    const [checkingCode, setCheckingCode] = useState<boolean>(false);
    const [savingSolution, setSavingSolution] = useState<boolean>(false);

    const solutionTextarea = useRef<HTMLTextAreaElement>(null);

    const {testId} = useParams<string>();


    useEffect(() => {
        loadTest();
    }, []);

    async function loadTest() {
        try {
            const responseTest = await TestService.getTest(Number(testId));
            const responseAnswer = await AnswerService.findRespondentCurrentAnswer(responseTest.data.id);

            if (responseAnswer.data.isFinished) {
                setTestFinished(true);
            }
            setAnswer(responseAnswer.data);
            setTest(responseTest.data);
            setSelectedTaskId(getTasksSorted(responseTest.data.tasks)[0].id);
            initAnswersMap(responseTest.data.tasks, responseAnswer.data.solutions);
        } catch (e) {
            console.log(e);
        }
    }

    function initAnswersMap(tasks: ITask[], solutionsList: ISolution[]) {
        tasks.forEach((task) => setSolutions(
            new Map(solutions.set(
                task.id,
                {code: findTaskSolution(task, solutionsList), taskCompletionStatus: ""})
            ))
        );
    }

    function findTaskSolution(task: ITask, solutionsList: ISolution[]): string {
        const solution: ISolution = solutionsList.find((s) => s.task.id === task.id)!;
        if (solution)
            return solution.code;
        return task.problem.templateCode;
    }

    function getTasksSorted(tasks: ITask[]) {
        return tasks?.sort((a, b) => a.id > b.id ? 1 : -1);
    }

    function currentSolution() {
        return solutions.get(selectedTaskId);
    }

    function changeSolutionCode(code: string) {
        const taskCompletionStatus = solutions.get(selectedTaskId)?.taskCompletionStatus!;
        setSolutions(new Map(solutions.set(
            selectedTaskId,
            {code: code, taskCompletionStatus: taskCompletionStatus})
        ));
    }

    async function tryCode() {
        await setCheckingCode(true);
        const code = solutionTextarea.current?.value!;

        const result = await SolutionService.tryCode({code: code, taskId: selectedTaskId});

        const solutionCode = solutions.get(selectedTaskId)?.code!;
        setSolutions(new Map(solutions.set(
            selectedTaskId,
            {code: solutionCode, taskCompletionStatus: result.data})
        ));

        await setCheckingCode(false);
    }

    function getCodeEvaluationDesc(): string {
        if (checkingCode)
            return "Компіляція...";

        if (currentSolution()?.taskCompletionStatus === undefined)
            return "";

        if (Object.keys(currentSolution()?.taskCompletionStatus!).length === 0)
            return "";

        return currentSolution()?.taskCompletionStatus!;
    }

    async function saveSolution() {
        await setSavingSolution(true);
        const solution = solutions.get(selectedTaskId);
        console.log(answer.id);
        await SolutionService.putSolution({code: solution?.code!, taskId: selectedTaskId, answerId: answer.id});
        await setSavingSolution(false);
    }

    async function finishTest() {
        await AnswerService.finishAnswer(answer.id);
        await loadTest();
    }

    if (testFinished) {
        return (
            <PageTemplate>
                <div className={"result-page"}>
                    <div className={"result-block"}>
                        <p>Ви пройшли тестування</p>
                        <p>
                            Ваш результат <span className={"number"}>: {answer.score}</span>/<span
                            className={"number"}>{TestService.calcTotalMaxScore(test)} балів</span>
                        </p>
                    </div>
                </div>
            </PageTemplate>
        );
    }

    return (
        <PageTemplate>
            <div className={"respondent-session-page"}>
                <div className={"respondent-session-sidebar"}>
                    <p className={"test-name"}>{test.name}</p>
                    <div className={"tasks"}>
                        {test.tasks?.map(task =>
                            <div onClick={() => setSelectedTaskId(task.id)}
                                 className={selectedTaskId === task.id ? "task active" : "task"}
                                 key={task.id}>
                                <p className={"task-name"}>{task.problem.name}</p>
                                <div className={"task-info"}>
                                    <div className={"task-language"}>Мова : {task.problem.language.name}</div>
                                    <div className={"task-desc"}>{task.problem.description}</div>
                                    {task.note ? <div className={"task-note"}>Примітка : {task.note}</div> : null}
                                    <div className={"task-score"}>{task.maxScore} балів</div>
                                </div>
                            </div>
                        )}
                    </div>
                    <button onClick={() => finishTest()}
                            className={"standard-button finish-test-button"}>
                        Закінчити тестування
                    </button>
                </div>
                <div className={"answer-block"}>
                    <textarea onChange={(e) => changeSolutionCode(e.target.value)}
                              ref={solutionTextarea} className={"answer-input"}
                              value={currentSolution()?.code}>
                    </textarea>
                    <div className={"answer-evaluation-block"}>
                        <button onClick={() => tryCode()}
                                className={"standard-button evaluate-code-button"}>
                            Запустити код
                        </button>
                        <p className={"evaluation-result"}>{getCodeEvaluationDesc()}</p>
                        <button onClick={() => saveSolution()}
                                className={"standard-button save-answer-button"}>
                            {savingSolution ? "Зберігаємо..." : "Зберегти відповідь"}
                        </button>
                    </div>
                </div>
            </div>
        </PageTemplate>
    );

}

export default RespondentSessionPage;