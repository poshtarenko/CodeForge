import React, {useState} from "react";
import "./answerModal.css";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck, faXmark} from "@fortawesome/free-solid-svg-icons";
import {IAnswer} from "../../../../models/entity/IAnswer";


interface IProps {
    answer: IAnswer
}

const AnswerModal: React.FC<IProps> = ({answer}) => {

    const [selectedAnswerId, setSelectedAnswerId] = useState<number>(0);

    function selectAnswer(answerId: number) {
        if (selectedAnswerId === answerId)
            setSelectedAnswerId(0);
        else
            setSelectedAnswerId(answerId);
    }

    return (
        <div className={"result-modal"}>
            <p className={"modal-title"}>Відповідь {answer?.respondent?.username}</p>
            {answer?.solutions?.map(solution =>
                <div key={solution.id} onClick={() => selectAnswer(solution.id)} className={"answer"}>
                    <div className={"answer-header"}>
                        {solution.solutionResult.isCompleted
                            ?
                            <div className={"answer-icon-wrapper completed"}><FontAwesomeIcon className={"answer-icon"} icon={faCheck} /></div>
                            :
                            <div className={"answer-icon-wrapper uncompleted"}><FontAwesomeIcon className={"answer-icon"} icon={faXmark} /></div>
                        }
                        <p className={"answer-name"}>{solution.task.problem.name}</p>
                    </div>
                    {selectedAnswerId === solution.id ?
                        <div>
                            <div onClick={(event) => event.stopPropagation()} className="answer-code">{solution.code}</div>
                            {solution.solutionResult.error ?
                                <div onClick={(event) => event.stopPropagation()} className="answer-error">
                                    {solution.solutionResult.error}
                                </div> : null}
                        </div>
                        : null
                    }
                </div>
            )}
        </div>
    );

}

export default AnswerModal;