import React, {useEffect, useRef, useState} from 'react';
import TestService from "../../services/TestService";
import {ITask, ITest} from "../../models/entity/ITest";
import "./respondentSessionPage.css"
import {useParams} from "react-router-dom";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {TryCodeResult} from "../../models/entity/TryCodeResult";
import AnswerService from "../../services/AnswerService";
import {IAnswer} from "../../models/entity/IAnswer";
import ResultService from "../../services/ResultService";
import {IResult} from "../../models/entity/IResult";

interface AnswerDetails {
    code: string,
    tryCodeResult: TryCodeResult,
}

const RespondentSessionPage: React.FC = () => {

    const [test, setTest] = useState<ITest>({} as ITest);
    const [selectedTaskId, setSelectedTaskId] = useState<number>(0);
    const [answers, setAnswers] = useState(new Map<number, AnswerDetails>());

    const [testFinished, setTestFinished] = useState<boolean>(false);
    const [result, setResult] = useState<IResult>({} as IResult);

    const [checkingCode, setCheckingCode] = useState<boolean>(false);
    const [savingAnswer, setSavingAnswer] = useState<boolean>(false);

    const answerTextarea = useRef<HTMLTextAreaElement>(null);

    const {code} = useParams<string>();


    useEffect(() => {
        loadTest();
    }, []);

    async function loadTest() {
        try {
            const responseTest = await TestService.getTestByCode(code!);
            const responseResult = await ResultService.findRespondentTestResult(responseTest.data.id);

            if (responseResult.data) {
                setTestFinished(true);
                setResult(responseResult.data)
            }

            const responseAnswers = await AnswerService.findRespondentAnswersOnTest(responseTest.data.id);
            setTest(responseTest.data);
            setSelectedTaskId(getTasksSorted(responseTest.data.tasks)[0].id);
            initAnswersMap(responseTest.data.tasks, responseAnswers.data);
        } catch (e) {
            console.log(e);
        }
    }

    function initAnswersMap(tasks: ITask[], answersList: IAnswer[]) {
        tasks.forEach((task) => setAnswers(
            new Map(answers.set(
                task.id,
                {code: findAnswerOnTask(task.id, answersList), tryCodeResult: {} as TryCodeResult})
            ))
        );
    }

    function findAnswerOnTask(taskId: number, answersList: IAnswer[]) : string {
        const answer = answersList.find((a) => a.taskId === taskId);
        if (answer) return answer.code;
        return "";
    }

    function getTasksSorted(tasks: ITask[]) {
        return tasks?.sort((a, b) => a.id > b.id ? 1 : -1);
    }

    function currentAnswer() {
        return answers.get(selectedTaskId);
    }

    function changeAnswerCode(code: string) {
        const tryCodeResult = answers.get(selectedTaskId)?.tryCodeResult!;
        setAnswers(new Map(answers.set(
            selectedTaskId,
            {code: code, tryCodeResult: tryCodeResult})
        ));
    }

    async function tryCode() {
        await setCheckingCode(true);
        const code = answerTextarea.current?.value!;

        const result = await AnswerService.tryCode({code: code, taskId: selectedTaskId});

        const answerCode = answers.get(selectedTaskId)?.code!;
        setAnswers(new Map(answers.set(
            selectedTaskId,
            {code: answerCode, tryCodeResult: result.data})
        ));

        await setCheckingCode(false);
    }

    function getCodeEvaluationDesc(): string {
        if (checkingCode)
            return "Loading...";

        if (currentAnswer()?.tryCodeResult === undefined)
            return "";

        if (Object.keys(currentAnswer()?.tryCodeResult!).length === 0)
            return "";

        const result = currentAnswer()?.tryCodeResult!;

        if (result.isCompleted) {
            return "Завдання виконане"
        } else if (result.error) {
            return result.error;
        }
        return "";
    }

    async function saveAnswer() {
        await setSavingAnswer(true);
        const answer = answers.get(selectedTaskId);
        await AnswerService.saveAnswer({code: answer?.code!, taskId: selectedTaskId});
        await setSavingAnswer(false);
    }

    async function finishTest() {
        await TestService.finishTest(test.id);
        await loadTest();
    }

    if (testFinished) {
        return (
          <PageTemplate>
              <div className={"result-page"}>
                  <div className={"result-block"}>
                      <p>Ви пройшли тестування</p>
                      <p>Ваш результат : <span className={"number"}>{result.score}</span> балів</p>
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
                                    <div className={"task-note"}>Примітка : {task.note}</div>
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
                    <textarea onChange={(e) => changeAnswerCode(e.target.value)}
                              ref={answerTextarea} className={"answer-input"}
                              value={currentAnswer()?.code}>
                    </textarea>
                    <div className={"answer-evaluation-block"}>
                        <button onClick={() => tryCode()}
                                className={"standard-button evaluate-code-button"}>
                            Запустити код
                        </button>
                        <p className={"evaluation-result"}>{getCodeEvaluationDesc()}</p>
                        <button onClick={() => saveAnswer()}
                                className={"standard-button save-answer-button"}>
                            {savingAnswer ? "Зберігаємо..." : "Зберегти відповідь"}
                        </button>
                    </div>
                </div>
            </div>
        </PageTemplate>
    );

}

export default RespondentSessionPage;