import React, {useState} from 'react';
import "./enterCodePage.css"
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {useNavigate} from "react-router-dom";
import TestService from "../../services/TestService";
import AnswerService from "../../services/AnswerService";
import LessonService from "../../services/LessonService";

function EnterCodePage() {

    const [code, setCode] = useState<string>("");
    const [codeIsWrong, setCodeIsWrong] = useState<boolean>(false);

    let navigate = useNavigate();

    async function navigateToSession() {
        try {
            if (code.length === 8) {
                const startTestResponse = await AnswerService.startTest(code);
                const response = await TestService.getTest(startTestResponse.data.testId);
                navigate("/session/" + response.data.id)
            }
            else if (code.length === 9) {
                const response = await LessonService.connectToLesson(code);
                navigate("/lesson/session/" + response.data.id)
            }
            else {
                setCodeIsWrong(true);
            }
        } catch (e) {
            setCodeIsWrong(true);
        }
    }

    return (
        <PageTemplate>
            <div className={"enter-code-section"}>
                <p className={"connect-to-session-p"}>Підключитись до сесії</p>
                <input onChange={(e) => setCode(e.target.value)}
                       name={"task-code"} value={code} className={"standard-input"} type="text"/>
                <p className={codeIsWrong ? "wrong-code-alert active" : "wrong-code-alert"}>Введеного коду не існує</p>
                <button onClick={() => navigateToSession()} className={"standard-button"}>Підключитися</button>
            </div>
        </PageTemplate>
    );

}

export default EnterCodePage;