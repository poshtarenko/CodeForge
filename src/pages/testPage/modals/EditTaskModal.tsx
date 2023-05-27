import React, {useEffect, useRef, useState} from "react";
import {ILanguage} from "../../../models/entity/ILanguage";
import {ICategory} from "../../../models/entity/ICategory";
import {IProblem} from "../../../models/entity/IProblem";
import LanguageService from "../../../services/LanguageService";
import CategoryService from "../../../services/CategoryService";
import ProblemService from "../../../services/ProblemService";
import "./addTaskModal.css";
import TaskService from "../../../services/TaskService";
import {ITask} from "../../../models/entity/ITest";


interface IProps {
    testId: number,
    onSubmit: Function,
    task: ITask
}

const AddTaskModal: React.FC<IProps> = ({testId, onSubmit, task}) => {

    const [languages, setLanguages] = useState<ILanguage[]>([]);
    const [categories, setCategories] = useState<ICategory[]>([]);
    const [problems, setProblems] = useState<IProblem[]>([]);

    const [language, setLanguage] = useState<ILanguage>({} as ILanguage);
    const [category, setCategory] = useState<ICategory>({} as ICategory);

    const [problem, setProblem] = useState<IProblem>({} as IProblem);
    const [note, setNote] = useState<string>("");
    const [score, setScore] = useState<number>(0);

    const languageSelect = useRef<HTMLSelectElement>(null);
    const categorySelect = useRef<HTMLSelectElement>(null);
    const problemSelect = useRef<HTMLSelectElement>(null);

    useEffect(() => {
        loadLanguages();
        loadCategories();
        loadProblems();
        setNote(task.note);
        setScore(task.maxScore);
        setCategory(task.problem.category);
        setLanguage(task.problem.language);
        setProblem(task.problem);
    }, []);

    async function loadLanguages() {
        try {
            const response = await LanguageService.getAllLanguages();
            setLanguages(response.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function loadCategories() {
        try {
            const response = await CategoryService.getAllCategories();
            setCategories(response.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function loadProblems() {
        try {
            const response = await ProblemService.getAllProblems();
            await setProblems(response.data);
        } catch (e) {
            console.log(e);
        }
    }

    async function updateTask() {
        await TaskService.updateTask({id: task.id, note: note, maxScore: score, problemId: problem.id, testId: testId});
        await onSubmit();
    }

    function getProblemsFiltered(problems: IProblem[], category: string, language: string): IProblem[] {
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
            <p className={"modal-title"}>Редагувати завдання</p>
            <p className={"input-title"}>Задача</p>
            <div className={"problem-selection"}>
                <select ref={languageSelect} className={"add-task-input language-select add-task-modal-select"}
                        onChange={(e) => selectLanguage(Number(e.target.value))}>
                    {language.name ? <option value="NONE" disabled selected>Мова</option> : null}
                    {languages.map(lang =>
                        <option selected={language.name === lang.name}
                                key={Number(lang.id)}
                                value={String(lang.id)}>
                            {lang.name}
                        </option>)}
                </select>
                <select ref={categorySelect} className={"add-task-input category-select add-task-modal-select"}
                        onChange={(e) => selectCategory(Number(e.target.value))}>
                    {category.name ? <option value="NONE" disabled selected>Категорія</option> : null}
                    {categories.map(c =>
                        <option selected={category.name === c.name}
                                key={Number(c.id)}
                                value={String(c.id)}>
                            {c.name}
                        </option>)}
                </select>
            </div>
            <select ref={problemSelect} className={"add-task-input problem-select add-task-modal-select"}
                    onChange={(e) => setProblem(problems.find(p => p.id === Number(e.target.value))!)}>
                {problem.name ? <option value="NONE" disabled selected>Задача</option> : null}
                {getProblemsFiltered(problems, category.name, language.name).map(p =>
                    <option selected={problem.name === p.name}
                            key={Number(p.id)}
                            value={String(p.id)}>
                        {p.name}
                    </option>)}
            </select>
            <p className={"problem-desc"}>{problem.description}</p>
            <p className={"input-title"}>Примітка</p>
            <textarea onChange={(e) => setNote(e.target.value)}
                      value={note} className={"add-task-input task-note-input"}>
            </textarea>
            <p className={"input-title"}>Кількість балів</p>
            <input onChange={(e) => setScore(Number(e.target.value))}
                   className={"add-task-input task-score-input"}
                   value={score} type="number"/>
            <button className={"create-task-button standard-button"} onClick={updateTask}>Зберегти</button>
        </div>
    );

}

export default AddTaskModal;