import React, {useEffect, useState} from 'react';
import TestService from "../../services/TestService";
import {ITest} from "../../models/entity/ITest";
import "./testsPage.css"
import Test from "../../component/testCell/TestCell";
import AddTest from "../../component/addTest/AddTest";
import PageTemplate from "../../component/UI/page-template/PageTemplate";
import {faPersonChalkboard} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

function TestsPage() {

    const [tests, setTests] = useState<ITest[]>([]);

    useEffect(() => {
        getTests();
    }, []);

    async function getTests() {
        try {
            const response = await TestService.getAuthorTests();
            setTests(response.data);
            console.log(response.data);
        } catch (e) {
            console.log(e);
        }
    }

    return (
        <PageTemplate>
            <div className={"app-section"}>
                <div className={"section-title"}>
                    <FontAwesomeIcon className={"section-icon"} icon={faPersonChalkboard} />
                    <p className={"section-name"}>Мої тести</p>
                </div>
                <div className={"tests"}>
                    {tests.map(test =>
                        <Test test={test} key={Number(test.id)}/>
                    )}
                    <AddTest addTest={getTests}/>
                </div>
            </div>
        </PageTemplate>
    );

}

export default TestsPage;