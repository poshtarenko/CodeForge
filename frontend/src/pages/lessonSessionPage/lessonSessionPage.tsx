import React, {useEffect, useRef, useState} from 'react';
import TestService from "../../services/TestService";
import {ITask, ITest} from "../../models/entity/ITest";
import "./lessonSessionPage.css"
import {useParams} from "react-router-dom";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import SolutionService from "../../services/SolutionService";
import {IAnswer, ISolution} from "../../models/entity/IAnswer";
import AnswerService from "../../services/AnswerService";
import {ILesson} from "../../models/entity/ILesson";
import LessonService from "../../services/LessonService";

interface AnswerDetails {
    code: string,
    taskCompletionStatus: string,
}

const LessonSessionPage: React.FC = () => {

    const [lesson, setLesson] = useState<ILesson>({} as ILesson);
    const [selectedTaskId, setSelectedTaskId] = useState<number>(0);
    const [solutions, setSolutions] = useState(new Map<number, AnswerDetails>());

    const [checkingCode, setCheckingCode] = useState<boolean>(false);

    const solutionTextarea = useRef<HTMLTextAreaElement>(null);

    const {lessonId} = useParams<string>();


    useEffect(() => {
        loadLesson();
    }, []);

    async function loadLesson() {
        try {
            const response = await LessonService.getLessonAsAuthor(Number(lessonId));
            setLesson(response.data);
        } catch (e) {
            console.log(e);
        }
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
        setCheckingCode(true);
        const code = solutionTextarea.current?.value!;

        const result = await SolutionService.tryCode({code: code, taskId: selectedTaskId});

        const solutionCode = solutions.get(selectedTaskId)?.code!;
        setSolutions(new Map(solutions.set(
            selectedTaskId,
            {code: solutionCode, taskCompletionStatus: result.data})
        ));

        setCheckingCode(false);
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

    return (
        <PageTemplate>
            <div className={"respondent-session-page"}>
                <div className={"respondent-session-sidebar"}>
                    <p className={"test-name"}>{lesson.name}</p>
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
                    </div>
                </div>
            </div>
        </PageTemplate>
    );

}

export default LessonSessionPage;