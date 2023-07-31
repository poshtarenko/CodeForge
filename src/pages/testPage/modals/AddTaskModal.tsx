import React, {useEffect, useRef, useState} from "react";
import {ILanguage} from "../../../models/entity/ILanguage";
import {ICategory} from "../../../models/entity/ICategory";
import {Problem} from "../../../models/entity/Problem";
import LanguageService from "../../../services/LanguageService";
import CategoryService from "../../../services/CategoryService";
import ProblemService from "../../../services/ProblemService";
import "./addTaskModal.css";
import TaskService from "../../../services/TaskService";
import {IProblem} from "../../../models/entity/ITest";


interface IProps {
    testId: number,
    onSubmit: Function,
    problems: IProblem[],
    categories: ICategory[],
    languages: ILanguage[]
}

const AddTaskModal: React.FC<IProps> = ({testId, onSubmit, problems, categories, languages}) => {

    const [language, setLanguage] = useState<ILanguage>({} as ILanguage);
    const [category, setCategory] = useState<ICategory>({} as ICategory);

    const [problem, setProblem] = useState<Problem>({} as Problem);
    const [note, setNote] = useState<string>("");
    const [score, setScore] = useState<number>(0);

    const problemSelect = useRef<HTMLSelectElement>(null);

    async function addTask() {
        await TaskService.createTask({note: note, maxScore: score, problemId: problem.id, testId: testId});
        await onSubmit();
    }

    function getProblemsFiltered(problems: Problem[], category: string, language: string): Problem[] {
        return problems.filter(p => p.category.name === category && p.language.name === language);
    }

    function selectLanguage(id: number) {
        setLanguage(languages.find((l => l.id === id))!);
        problemSelect.current!.value = "NONE";
    }

    function selectCategory(id: number) {
        setCategory(categories.find((c => c.id === id))!);
        problemSelect.current!.value = "NONE";
    }

    return (
        <div className={"add-task-modal"}>
            <p className={"modal-title"}>Додати завдання</p>
            <p className={"input-title"}>Задача</p>
            <div className={"problem-selection"}>
                <select className={"add-task-input language-select add-task-modal-select"}
                        onChange={(e) => selectLanguage(Number(e.target.value))}
                        defaultValue={"NONE"}>
                    <option value="NONE" disabled>Мова</option>
                    {languages.map(language =>
                        <option key={Number(language.id)} value={String(language.id)}>{language.name}</option>)}
                </select>
                <select className={"add-task-input category-select add-task-modal-select"}
                        onChange={(e) => selectCategory(Number(e.target.value))}
                        defaultValue={"NONE"}>
                    <option value="NONE" disabled>Категорія</option>
                    {categories.map(category =>
                        <option key={Number(category.id)} value={String(category.id)}>{category.name}</option>)}
                </select>
            </div>
            <select ref={problemSelect} className={"add-task-input problem-select add-task-modal-select"}
                    onChange={(e) => setProblem(problems.find(p => p.id === Number(e.target.value))!)}
                    defaultValue={"NONE"}>
                <option value="NONE" disabled>Задача</option>
                {getProblemsFiltered(problems, category.name, language.name).map(problem =>
                    <option key={Number(problem.id)} value={String(problem.id)}>{problem.name}</option>)}
            </select>
            <p className={"problem-desc"}>{problem.description}</p>
            <p className={"input-title"}>Примітка</p>
            <textarea onChange={(e) => setNote(e.target.value)}
                      className={"add-task-input task-note-input"}>
            </textarea>
            <p className={"input-title"}>Кількість балів</p>
            <input onChange={(e) => setScore(Number(e.target.value))}
                   className={"add-task-input task-score-input"}
                   type="number"/>
            <button className={"create-task-button standard-button"} onClick={addTask}>Зберегти</button>
        </div>
    );

}

export default AddTaskModal;