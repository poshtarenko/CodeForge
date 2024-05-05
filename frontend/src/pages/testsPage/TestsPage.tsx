import React, {useEffect, useState} from 'react';
import TestService from "../../services/TestService";
import {ITest} from "../../models/entity/ITest";
import "./testsPage.css"
import TestCell from "../../component/testCell/TestCell";
import AddTestCell from "../../component/addTestCell/AddTestCell";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {faPersonChalkboard} from "@fortawesome/free-solid-svg-icons";
import {faLightbulb} from "@fortawesome/free-regular-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import ProblemCell from "../../component/problemCell/ProblemCell";
import ProblemService from "../../services/ProblemService";
import {Problem} from "../../models/entity/Problem";
import AddProblemCell from "../../component/addProblemCell/AddProblemCell";
import {ILanguage} from "../../models/entity/ILanguage";
import LanguageService from "../../services/LanguageService";

function TestsPage() {

    const [tests, setTests] = useState<ITest[]>([]);
    const [problems, setProblems] = useState<Problem[]>([]);
    const [languages, setLanguages] = useState<ILanguage[]>([]);

    useEffect(() => {
        getTests();
        getCustomProblems();
        getLanguages();
    }, []);

    async function getTests() {
        const response = await TestService.getAuthorTests();
        setTests(response.data);
    }

    async function getCustomProblems() {
        const response = await ProblemService.getCustomProblems();
        setProblems(response.data);
    }

    async function getLanguages() {
        const response = await LanguageService.getAllLanguages();
        setLanguages(response.data);
    }

    return (
        <PageTemplate>
            <div className={"app-section"}>
                <div className={"section-title"}>
                    <FontAwesomeIcon className={"section-icon"} icon={faPersonChalkboard}/>
                    <p className={"section-name"}>Мої тести</p>
                </div>
                <div className={"tests"}>
                    {tests.map(test =>
                        <TestCell test={test} key={Number(test.id)}/>
                    )}
                    <AddTestCell afterSave={getTests}/>
                </div>
                <div className={"section-title"}>
                    <FontAwesomeIcon className={"section-icon"} icon={faLightbulb}/>
                    <p className={"section-name"}>Мої розроблені завдання</p>
                </div>
                <div className={"tests"}>
                    {problems.map(problem =>
                        <ProblemCell problem={problem} key={Number(problem.id)}/>
                    )}
                    <AddProblemCell languages={languages} afterSave={getCustomProblems}/>
                </div>
            </div>
        </PageTemplate>
    );

}

export default TestsPage;