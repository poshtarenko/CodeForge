import React from "react";
import "./deleteTaskModal.css";
import TaskService from "../../../services/TaskService";

interface IProps {
    taskId: number,
    onSubmit: Function,
}

const AddTaskModal: React.FC<IProps> = ({taskId, onSubmit}) => {

    async function deleteTask() {
        await TaskService.deleteTask(taskId);
        await onSubmit();
    }

    return (
        <div className={"delete-task-modal"}>
            <p>Видалити завдання?</p>
            <button onClick={deleteTask} className={"delete-button standard-button"}>Видалити</button>
        </div>
    );

}

export default AddTaskModal;