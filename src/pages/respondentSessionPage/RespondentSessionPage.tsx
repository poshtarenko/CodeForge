import React, {useEffect, useRef, useState} from 'react';
import TestService from "../../services/TestService";
import {ITask, ITest} from "../../models/entity/ITest";
import "./respondentSessionPage.css"
import {useParams} from "react-router-dom";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {TryCodeResult} from "../../models/entity/TryCodeResult";
import AnswerService from "../../services/AnswerService";

interface AnswerDetails {
    code: string,
    tryCodeResult: TryCodeResult,
}

const RespondentSessionPage: React.FC = () => {

    const [test, setTest] = useState<ITest>({} as ITest);
    const [selectedTaskId, setSelectedTaskId] = useState<number>(0);
    const [answers, setAnswers] = useState(new Map<number, AnswerDetails>());

    const answerTextarea = useRef<HTMLTextAreaElement>(null);

    const {code} = useParams<string>();


    useEffect(() => {
        loadTest();
    }, []);

    async function loadTest() {
        try {
            const response = await TestService.getTestByCode(code!);
            setTest(response.data);
            setSelectedTaskId(getTasksSorted(response.data.tasks)[0].id);
            initAnswersMap(response.data.tasks);
        } catch (e) {
            console.log(e);
        }
    }

    function initAnswersMap(tasks: ITask[]) {
        tasks.forEach((task) => setAnswers(
            new Map(answers.set(
                task.id,
                {code: "", tryCodeResult: {} as TryCodeResult})
            ))
        );
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
        const code = answerTextarea.current?.value!;

        const result = await AnswerService.tryCode({code: code, taskId: selectedTaskId});

        const answerCode = answers.get(selectedTaskId)?.code!;
        setAnswers(new Map(answers.set(
            selectedTaskId,
            {code: answerCode, tryCodeResult: result.data})
        ));

        console.log(result.data)
    }

    function getCodeEvaluationDesc(): string {
        const result = currentAnswer()?.tryCodeResult;
        if (result?.isCompleted) {
            return "Завдання виконане"
        } else if (result?.error) {
            return "Помилка:\n" + result.error;
        }
        return "";
    }

    async function saveAnswer() {
        const answer = answers.get(selectedTaskId);
        await AnswerService.saveAnswer({code: answer?.code!, taskId: selectedTaskId});
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
                            Зберегти відповідь
                        </button>
                    </div>
                </div>
            </div>
        </PageTemplate>
    );

}

export default RespondentSessionPage;