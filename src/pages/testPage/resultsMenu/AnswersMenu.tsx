import React, {useState} from 'react';
import "./resultsMenu.css"
import Modal from "../../../component/UI/modal/Modal";
import ResultModal from "./resultModal/ResultModal";
import {IAnswer} from "../../../models/entity/IAnswer";

interface IProps {
    isActive: Boolean,
    answers: IAnswer[],
}

const AnswersMenu: React.FC<IProps> = ({answers, isActive}) => {

    const [answerModal, setAnswerModal] = useState<boolean>(false);
    const [selectedAnswer, setSelectedAnswer] = useState<IAnswer>({} as IAnswer);

    function selectResult(result: IAnswer){
        setSelectedAnswer(result);
        setAnswerModal(true);
    }

    return (
        <>
            <Modal active={answerModal} setActive={setAnswerModal}>
                <ResultModal answer={selectedAnswer}/>
            </Modal>
            <div className={isActive ? "results-menu selected" : "results-menu"}>
                {
                    answers.length === 0 ?
                        <p className={"no-answers-msg"}>Поки ніхто не пройшов тестування</p>
                        : null
                }
                {answers.map(answer =>
                    <div onClick={() => selectResult(answer)} className="result-el">
                        <p className="resp-name">{answer.respondent.name}</p>
                        <p className="score">{answer.score} балів</p>
                    </div>
                )}
            </div>
        </>
    );

}

export default AnswersMenu;