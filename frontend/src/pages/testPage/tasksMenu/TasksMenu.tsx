import React, {useEffect, useState} from 'react';
import {IProblem, ITask, ITest} from "../../../models/entity/ITest";
import "./tasksMenu.css"
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPenToSquare, faTrash} from "@fortawesome/free-solid-svg-icons";
import Modal from "../../../component/UI/modal/Modal";
import AddTaskModal from "./../modals/AddTaskModal";
import EditTaskModal from "./../modals/EditTaskModal";
import DeleteTaskModal from "./../modals/DeleteTaskModal";
import {ILanguage} from "../../../models/entity/ILanguage";
import {ICategory} from "../../../models/entity/ICategory";
import ProblemService from "../../../services/ProblemService";

interface IProps {
    isActive: Boolean,
    test: ITest,
    reloadFunc: Function;
}

const TasksMenu: React.FC<IProps> = ({test, reloadFunc, isActive}) => {

    const [problems, setProblems] = useState<IProblem[]>([]);
    const [languages, setLanguages] = useState<ILanguage[]>([]);
    const [categories, setCategories] = useState<ICategory[]>([]);

    const [addTaskModal, setAddTaskModal] = useState<boolean>(false);
    const [deleteTaskModal, setDeleteTaskModal] = useState<boolean>(false);
    const [taskToDelete, setTaskToDelete] = useState<ITask>({} as ITask);
    const [editTaskModal, setEditTaskModal] = useState<boolean>(false);
    const [taskToEdit, setTaskToEdit] = useState<ITask>({} as ITask);

    useEffect(() => {
        loadProblems();
    }, []);

    function extractLanguages(problems: IProblem[]) {
        let languageList: ILanguage[] = [];
        problems.forEach(problem => {
            if (!languageList.find(l => l.id === problem.language.id)) {
                languageList.push(problem.language)
            }
        })
        setLanguages(languageList);
    }

    function extractCategories(problems: IProblem[]) {
        let categoryList: ICategory[] = [];
        problems.forEach(problem => {
            if (!categoryList.find(c => c.id === problem.category.id)) {
                categoryList.push(problem.category)
            }
        })
        setCategories(categoryList);
    }

    async function loadProblems() {
        try {
            const response = await ProblemService.getAvailableProblems();
            await extractLanguages(response.data);
            await extractCategories(response.data);
            await setProblems(response.data);
        } catch (e) {
            console.log(e);
        }
    }

    function getTasksSorted(tasks: ITask[]) {
        return tasks?.sort((a, b) => a.id > b.id ? 1 : -1);
    }

    function onAddTask() {
        setAddTaskModal(false);
        reloadFunc();
    }

    function onEditTask() {
        setEditTaskModal(false);
        setTaskToEdit({} as ITask);
        reloadFunc();
    }

    function onDeleteTask() {
        setDeleteTaskModal(false);
        reloadFunc();
    }

    return (
        <div className={isActive ? "tasks-menu selected" : "tasks-menu"}>
            <Modal active={addTaskModal} setActive={setAddTaskModal}>
                <AddTaskModal onSubmit={onAddTask} testId={test.id} categories={categories}
                              languages={languages}
                              problems={problems}/>
            </Modal>
            <Modal active={editTaskModal} setActive={setEditTaskModal}>
                {editTaskModal ?
                    <EditTaskModal onSubmit={onEditTask} testId={test.id} task={taskToEdit} categories={categories}
                                   languages={languages}
                                   problems={problems}/> : null}
            </Modal>
            <Modal active={deleteTaskModal} setActive={setDeleteTaskModal}>
                {deleteTaskModal ? <DeleteTaskModal onSubmit={onDeleteTask} taskId={taskToDelete.id}/> : null}
            </Modal>
            <div className={"tasks"}>
                {getTasksSorted(test.tasks)?.map((task, index) =>
                    <div key={Number(task.id)} className={"task-cell"}>
                        <FontAwesomeIcon className={"icon-button change-button"} icon={faPenToSquare}
                                         onClick={() => {
                                             setTaskToEdit(task);
                                             setEditTaskModal(true)
                                         }}/>
                        <FontAwesomeIcon className={"icon-button delete-button"} icon={faTrash}
                                         onClick={() => {
                                             setTaskToDelete(task);
                                             setDeleteTaskModal(true)
                                         }}/>
                        <div className={"task-main"}>
                            <div className={"task-meta"}>
                                <p className={"task-name"}><span
                                    className={"number"}>{(index + 1) + ". "}</span> {task.problem.name}</p>
                                <p className={"task-lang"}>{task.problem.language.name}</p>
                            </div>
                            <p className={"task-score"}>
                                <span className={"number"}>{task.maxScore}</span> балів
                            </p>
                        </div>
                        <p className={"task-desc"}>{task.problem.description}</p>
                        {task.note ? <p className={"task-note"}>Примітка : {task.note}</p> : null}
                    </div>
                )}
            </div>
            <button onClick={() => setAddTaskModal(true)}
                    className={"add-task-button"}>
                Додати завдання
            </button>
        </div>
    );

}

export default TasksMenu;