import React, {useEffect, useRef, useState} from 'react';
import "./problemPage.css"
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {ILanguage} from "../../models/entity/ILanguage";
import LanguageService from "../../services/LanguageService";
import {SignatureExample, TestingCodeExample} from "./CodeExamples";
import {Problem} from "../../models/entity/Problem";
import ProblemService from "../../services/ProblemService";
import {UpdateProblemRequest} from "../../models/request/UpdateProblemRequest";
import {useParams} from "react-router-dom";
import {faCircleCheck, faCircleExclamation, faCircleInfo} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import SolutionService from "../../services/SolutionService";

function ProblemPage() {

    const [problem, setProblem] = useState<Problem>({} as Problem);
    const [languages, setLanguages] = useState<ILanguage[]>([]);
    const [solution, setSolution] = useState<string>("");
    const [taskCompletionStatus, setTaskCompletionStatus] = useState<string>("");
    const [checkingCode, setCheckingCode] = useState<boolean>(false);
    const {id} = useParams<string>();

    const solutionTextarea = useRef<HTMLTextAreaElement>(null);

    function constructUpdateProblemRequest(problem: Problem): UpdateProblemRequest {
        return {
            name: problem.name,
            languageId: problem.language.id,
            description: problem.description,
            templateCode: problem.templateCode,
            testingCode: problem.testingCode
        };
    }

    useEffect(() => {
        getProblem();
        getLanguages();
    }, []);

    async function getLanguages() {
        const response = await LanguageService.getAllLanguages();
        setLanguages(response.data);
    }

    async function getProblem() {
        const response = await ProblemService.getCustomProblem(Number(id));
        setProblem(response.data);
    }

    function updateName(name: string) {
        const updatedProblem = {...problem, name: name};
        updateProblem(updatedProblem)
    }

    function updateLanguage(languageId: number) {
        const updatedProblem = {...problem, language: languages.find((l => l.id === languageId))!};
        updateProblem(updatedProblem)
    }

    function updateDescription(description: string) {
        const updatedProblem = {...problem, description: description};
        updateProblem(updatedProblem)
    }

    function updateTemplateCode(templateCode: string) {
        const updatedProblem = {...problem, templateCode: templateCode};
        updateProblem(updatedProblem)
    }

    function updateTestingCode(testingCode: string) {
        const updatedProblem = {...problem, testingCode: testingCode};
        updateProblem(updatedProblem)
    }

    async function updateProblem(problem: Problem) {
        const p = (await ProblemService.updateCustomProblem(Number(id), constructUpdateProblemRequest(problem))).data
        setProblem(p)
    }

    async function tryCode() {
        await setCheckingCode(true);
        const code = solutionTextarea.current?.value!;
        const result = await SolutionService.tryCode({code: code, problemId: Number(id)});
        setTaskCompletionStatus(result.data)
        await setCheckingCode(false);
    }

    function getCodeEvaluationDesc(): string {
        if (checkingCode)
            return "Компіляція...";

        if (taskCompletionStatus === undefined)
            return "";

        if (Object.keys(taskCompletionStatus!).length === 0)
            return "";

        return taskCompletionStatus!;
    }

    return (
        <PageTemplate>
            <div className={"problem-app-section"}>
                <div className={"problem-page"}>
                    <div className="status-and-guide">
                        <div className="problem-status-block">
                            <div className="problem-status-block-inner">
                                {
                                    problem.isCompleted ?
                                        <div className={"problem-status problem-status-completed"}>
                                            <FontAwesomeIcon className={"problem-status-icon"}
                                                             icon={faCircleCheck}/>
                                            <p>Завдання повністю заповнене. Завдання доступне для використання у
                                                тестуваннях</p>
                                        </div>
                                        :
                                        <div className={"problem-status problem-status-uncompleted"}>
                                            <FontAwesomeIcon className={"problem-status-icon"}
                                                             icon={faCircleExclamation}/>
                                            <p>Завдання не заповнене. Завдання не доступне для використання у
                                                тестуваннях</p>
                                        </div>
                                }
                            </div>
                        </div>
                        <div className={"guide"}>
                            <div className={"guide-inner"}>
                                <div className="guide-title">
                                    <FontAwesomeIcon icon={faCircleInfo} className={"guide-icon"}/>
                                    <p>Інструкція</p>
                                </div>
                                <div className="guide-content">
                                    <p className={"guide-paragraph"}>Для того, щоб завдання стало доступним для
                                        використанням у ваших тестуваннях, ви
                                        маєте заповнити наступні поля :</p>
                                    <p className={"guide-paragraph"}>1. Назва завдання : будь-яка коротка назва, що
                                        стисло
                                        описує суть вашого завдання</p>
                                    <p className={"guide-paragraph"}>2. Мова програмування : мова програмування, якою
                                        студенти мають виконувати завдання.
                                        Ви також повинні написати тестувальний код і сигнатуру методу саме цією
                                        мовою</p>
                                    <p className={"guide-paragraph"}>3. Опис завдання : детальний опис завдання.
                                        Зверніть
                                        увагу, що при створенні завдання
                                        для конкретного тестування, ви зможе залишати додаткові примітки саме для цього
                                        тестування</p>
                                    <p className={"guide-paragraph"}>4. Сигнатура методу : сигнатура методу, яку повинен
                                        реалізувати студент. Приклад
                                        сигнатури методу для обраної мови програмування наведено у відповідному полі
                                        вводу.
                                        Повинна бути написана на обраній мові програмування</p>
                                    <p className={"guide-paragraph"}>5. Тестувальний код : код, що тестує рішення
                                        студента.
                                        Приклад тестувального коду (із
                                        сигнатурою) для обраної мови програмування наведено у полі вводу. Загалом,
                                        тестувальний код повинен
                                        реалізовувати таку сигнатуру методу, що повертає True у випадку правильного
                                        рішення
                                        студента, і False у випадку неправильного рішення. Повинен бути написаний на
                                        обраній
                                        мові програмування</p>
                                    <p className={"guide-paragraph"}>Обов'язково перевірте коректну роботу вашого
                                        завдання
                                        та тестувального коду,
                                        виконавши ваше завдання самостійно. Ви можете зробити це у панелі справа.
                                        Введіть
                                        програмний код, що
                                        вирішує ваше завдання, і переконайтесь у правильному налаштуванні вашого
                                        завдання </p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="form">
                        <div className="form-inner">
                            <div className="input-element">
                                <p className={"input-title"}>Назва завдання</p>
                                <input value={problem.name} onChange={(e) => updateName(e.target.value)}
                                       placeholder={"Назва"} type="text"/>
                            </div>
                            <div className="input-element">
                                <p className={"input-title"}>Мова програмування</p>
                                <select className={"problem-language-select"}
                                        onChange={(e) => updateLanguage(Number(e.target.value))}
                                        defaultValue={"NONE"}>
                                    <option value="NONE" disabled>-</option>
                                    {languages.map(language =>
                                        <option key={Number(language.id)}
                                                selected={language.id === problem?.language?.id!}
                                                value={String(language.id)}>{language.name}</option>)}
                                </select>
                            </div>
                            <div className="input-element">
                                <p className={"input-title"}>Опис завдання</p>
                                <textarea className={"description-input"}
                                          onChange={(e) => updateDescription(e.target.value)}
                                          value={problem.description!} placeholder={"Опис"}></textarea>
                            </div>
                            <div className="input-element">
                                <p className={"input-title"}>Сигнатура методу</p>
                                <textarea className={"signature-input"}
                                          onChange={(e) => updateTemplateCode(e.target.value)}
                                          value={problem.templateCode!} placeholder={SignatureExample}></textarea>
                            </div>
                            <div className="input-element testing-code-input-element">
                                <p className={"input-title"}>Тестувальний код</p>
                                <textarea className={"testing-code-input"}
                                          onChange={(e) => updateTestingCode(e.target.value)}
                                          value={problem.testingCode!} placeholder={TestingCodeExample}></textarea>
                            </div>
                        </div>
                    </div>
                    <div className="evaluation">
                        <div className="evaluation-title">
                            <div className="evaluation-title-inner">
                                <p>Перевірочне виконання завдання</p>
                            </div>
                        </div>
                        <div className={"answer-block"}>
                            <textarea onChange={(e) => setSolution(e.target.value)}
                                      ref={solutionTextarea} className={"answer-input"}
                                      value={solution} placeholder={"Код, що виконує завдання"}>
                            </textarea>
                            <div className={"answer-evaluation-block"}>
                                <p className={"evaluation-result"}>{getCodeEvaluationDesc()}</p>
                                <button onClick={() => tryCode()}
                                        className={"standard-button evaluate-code-button"}>
                                    Запустити код
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </PageTemplate>
    );
}

export default ProblemPage;