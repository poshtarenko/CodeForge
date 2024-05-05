import React, {useContext, useEffect, useRef, useState} from 'react';
import "./lessonSessionPage.css"
import {useParams} from "react-router-dom";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {ILesson} from "../../models/entity/ILesson";
import LessonService from "../../services/LessonService";
import {IEvaluationResult, IParticipation} from "../../models/entity/IParticipation";
import {useStompClient, useSubscription} from "react-stomp-hooks";
import {Context} from "../../index";

const LessonSessionPage: React.FC = () => {

    const {store} = useContext(Context);

    const {lessonId} = useParams<string>();

    const [lesson, setLesson] = useState<ILesson>({} as ILesson);
    const [selectedParticipationId, setSelectedParticipationId] = useState<number>(0);
    const [myParticipationId, setMyParticipationId] = useState<number>(0);
    const [description, setDescription] = useState<string>("");
    const [myCode, setMyCode] = useState<string>("");

    const codeTextarea = useRef<HTMLTextAreaElement>(null);
    const descriptionTextarea = useRef<HTMLTextAreaElement>(null);

    const isAuthor = store.role === "AUTHOR";
    const isRespondent = store.role === "RESPONDENT";

    const stompClient = useStompClient()!;
    useSubscription(`/topic/lessons/${lessonId}/description_updates`, (message) => {
        if (isRespondent) {
            const l: ILesson = JSON.parse(message.body);
            setDescription(l.description)
        }
    });
    useSubscription(`/topic/lessons/${lessonId}/participation_updates`, (message) => {
        console.log(message.body)
        const participation : IParticipation = JSON.parse(message.body);

        const prevEvaluationResult = currentParticipation().evaluationResult;
        const currEvaluationResult = participation.evaluationResult;
        console.log("checking")
        if (!evaluationResultsEquals(prevEvaluationResult, currEvaluationResult)) {
            participation.evaluating = false;
            console.log("ok")
            setLesson(prevLesson => ({
                ...prevLesson,
                participations: prevLesson.participations.map(p =>
                    p.id === participation.id ? participation : p
                )
            }));
        }

        if (participation.id !== myParticipationId) {
            setLesson(prevLesson => ({
                ...prevLesson,
                participations: prevLesson.participations.map(p =>
                    p.id === participation.id ? participation : p
                )
            }));
        }
    });

    function evaluationResultsEquals(first: IEvaluationResult, second: IEvaluationResult): boolean {
        if (first == null && second == null) {
            return true;
        }
        if (first == null && second != null) {
            return false;
        }
        if (first != null && second == null) {
            return false;
        }
        if (first.output !== second.output) {
            return false;
        }
        if (first.error !== second.error) {
            return false;
        }
        return true;
    }


    useEffect(() => {
        loadLesson();
    }, []);

    async function loadLesson() {
        try {
            const response = await LessonService.getLesson(Number(lessonId))
            setLesson(response.data)
            setDescription(response.data.description)
            const myParticipation = response.data.participations.find(p => p.user.id === store.userId)!
            setMyParticipationId(myParticipation.id)
            setSelectedParticipationId(myParticipation.id)
            setMyCode(myParticipation.code)
        } catch (e) {
            console.log(e);
        }
    }

    function updateDescription(description: string) {
        setDescription(description)
        if (stompClient.connected) {
            stompClient.publish({
                destination: `/app/lessons/${lessonId}/update_description`,
                body: `{"description" : "${description}"}`
            });
        }
    }

    function updateCode(code: string) {
        setMyCode(code)
        if (stompClient.connected) {
            let json = JSON.stringify(code);
            console.log(json)
            const escapedJson = code.replace(/\\n/g, "\\n")
                .replace(/\\'/g, "\\'")
                .replace(/\\"/g, '\\"')
                .replace(/\\&/g, "\\&")
                .replace(/\\r/g, "\\r")
                .replace(/\\t/g, "\\t")
                .replace(/\\b/g, "\\b")
                .replace(/\\f/g, "\\f");
            stompClient.publish({
                destination: `/app/lessons/${lessonId}/participations/${myParticipationId}/update_code`,
                body: `{"code" : ${json}}`
            });
        }
    }

    async function evaluateCode() {
        if (stompClient.connected) {
            stompClient.publish({
                destination: `/app/lessons/${lessonId}/participations/${myParticipationId}/evaluate`
            });
        }

        currentParticipation().evaluating = true;
        setLesson(prevLesson => ({
            ...prevLesson,
            participations: prevLesson.participations.map(p =>
                p.id === currentParticipation().id ? currentParticipation() : p
            )
        }));
    }

    function currentParticipation(): IParticipation {
        return lesson?.participations?.find(p => p.id === selectedParticipationId)!;
    }

    function getCurrentEvaluationDesc(): string {
        if (currentParticipation()?.evaluating)
            return "Компіляція...";
        if (currentParticipation() === undefined)
            return "";
        let evaluationResult = currentParticipation()?.evaluationResult;
        if (evaluationResult === null)
            return "";
        if (evaluationResult?.output != null) {
            return evaluationResult?.output;
        }
        return evaluationResult!.error;
    }

    function getCurrentCode(): string {
        if (selectedParticipationId === myParticipationId) {
            return myCode;
        }
        const code = lesson.participations.find(p => p.id === selectedParticipationId)?.code;
        if (code == null) {
            return ""
        }
        return code
    }

    return (
        <PageTemplate>
            <div className={"lesson-session-page"}>
                <div className={"lesson-session-sidebar"}>
                    <p className={"test-name"}>{lesson.name}</p>
                    <p className={"test-language"}>Мова : {lesson.language?.name}</p>
                    <textarea onChange={(e) => updateDescription(e.target.value)}
                              ref={descriptionTextarea} className={"description-textarea"}
                              value={description} readOnly={isRespondent}>
                    </textarea>
                    <div className={"participants"}>
                        {lesson.participations?.map(participation =>
                            <div onClick={() => setSelectedParticipationId(participation.id)}
                                 className={selectedParticipationId === participation.id ? "participant active" : "participant"}
                                 key={participation.id}>
                                <p className={"participant-name"}>{participation.user.username}</p>
                            </div>
                        )}
                    </div>
                </div>
                <div className={"answer-block"}>
                    <textarea onChange={(e) => updateCode(e.target.value)}
                              ref={codeTextarea} className={"answer-input"}
                              readOnly={selectedParticipationId !== myParticipationId}
                              value={getCurrentCode()}>
                    </textarea>
                    <div className={"answer-evaluation-block"}>
                        <button onClick={() => evaluateCode()}
                                className={"standard-button evaluate-code-button"}>
                            Запустити код
                        </button>
                        <p className={"evaluation-result"}>{getCurrentEvaluationDesc()}</p>
                    </div>
                </div>
            </div>
        </PageTemplate>
    );

}

export default LessonSessionPage;