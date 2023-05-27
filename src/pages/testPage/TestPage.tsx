import React, {useEffect, useState} from 'react';
import TestService from "../../services/TestService";
import {ITask, ITest} from "../../models/entity/ITest";
import "./testPage.css"
import {useParams} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faPenToSquare, faTrash} from "@fortawesome/free-solid-svg-icons";
import Modal from "../../component/UI/modal/Modal";
import AddTaskModal from "./modals/AddTaskModal";
import EditTaskModal from "./modals/EditTaskModal";
import DeleteTaskModal from "./modals/DeleteTaskModal";
import PageTemplate from "../../component/UI/page-template/PageTemplate";

const TestPage: React.FC = () => {

    const [test, setTest] = useState<ITest>({} as ITest);
    const [changingName, setChangingName] = useState<boolean>(false);
    const [newName, setNewName] = useState<string>("");

    const [addTaskModal, setAddTaskModal] = useState<boolean>(false);
    const [deleteTaskModal, setDeleteTaskModal] = useState<boolean>(false);
    const [taskToDelete, setTaskToDelete] = useState<ITask>({} as ITask);

    const [editTaskModal, setEditTaskModal] = useState<boolean>(false);
    const [taskToEdit, setTaskToEdit] = useState<ITask>({} as ITask);

    const {id} = useParams<string>();

    useEffect(() => {
        loadTest();
    }, []);

    async function loadTest() {
        try {
            const response = await TestService.getTest(Number(id));
            setTest(response.data);
            setNewName(response.data.name);
        } catch (e) {
            console.log(e);
        }
    }

    async function updateName(newName: string) {
        try {
            setChangingName(false);
            await TestService.updateTest(Number(test.id), {name: newName, maxDuration: 100});
            loadTest();
        } catch (e) {
            console.log(e);
        }
    }

    function getTasksSorted(tasks: ITask[]) {
        return tasks?.sort((a, b) => a.id > b.id ? 1 : -1);
    }

    function onAddTask() {
        setAddTaskModal(false);
        loadTest();
    }

    function onEditTask() {
        setEditTaskModal(false);
        setTaskToEdit({} as ITask);
        loadTest();
    }

    function onDeleteTask() {
        setDeleteTaskModal(false);
        loadTest();
    }

    console.log(taskToEdit);

    return (
        <PageTemplate>
            <Modal active={addTaskModal} setActive={setAddTaskModal}>
                <AddTaskModal onSubmit={onAddTask} testId={test.id}/>
            </Modal>
            <Modal active={editTaskModal} setActive={setEditTaskModal}>
                {editTaskModal ? <EditTaskModal onSubmit={onEditTask} testId={test.id} task={taskToEdit}/> : null}
            </Modal>
            <Modal active={deleteTaskModal} setActive={setDeleteTaskModal}>
                {deleteTaskModal ? <DeleteTaskModal onSubmit={onDeleteTask} taskId={taskToDelete.id}/> : null}
            </Modal>
            <div className={"test-page"}>
                <div className={"test-title-block"}>
                    <div>
                        {changingName ?
                            <div className={"change-test-name-block"}>
                                <input onChange={(e) => setNewName(e.target.value)}
                                       className={"test-name-input"}
                                       value={newName}/>
                                <FontAwesomeIcon onClick={() => updateName(newName)}
                                                 className={"ok-button"}
                                                 icon={faCheck}/>
                            </div>
                            :
                            <div className={"test-name-block"}>
                                <p className={"test-name"}>{test.name}</p>
                                <FontAwesomeIcon onClick={() => setChangingName(true)}
                                                 className={"change-button"}
                                                 icon={faPenToSquare}/>
                            </div>
                        }
                        <p className={"test-lang"}>Код для входу : {test.code}</p>
                    </div>
                    <div>
                        <p className={"test-tasks-count"}>
                            <span className={"number"}>{TestService.getTasksCount(test)}</span> завдань
                        </p>
                        <p className={"test-max-score"}>
                            <span className={"number"}>{TestService.getMaxScore(test)}</span> балів
                        </p>
                    </div>
                </div>
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
        </PageTemplate>
    );

}

export default TestPage;