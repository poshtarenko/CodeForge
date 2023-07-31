import React, {useState} from 'react';
import "./answersMenu.css"
import Modal from "../../../component/UI/modal/Modal";
import AnswerModal from "./answerModal/answerModal";
import {IAnswer} from "../../../models/entity/IAnswer";

interface IProps {
    isActive: Boolean,
    answers: IAnswer[],
}

const AnswersMenu: React.FC<IProps> = ({answers, isActive}) => {

    const [answerModal, setAnswerModal] = useState<boolean>(false);
    const [selectedAnswer, setSelectedAnswer] = useState<IAnswer>({} as IAnswer);

    function selectResult(result: IAnswer) {
        setSelectedAnswer(result);
        setAnswerModal(true);
    }

    return (
        <>
            <Modal active={answerModal} setActive={setAnswerModal}>
                <AnswerModal answer={selectedAnswer}/>
            </Modal>
            <div className={isActive ? "results-menu selected" : "results-menu"}>
                {
                    answers.length === 0 ?
                        <p className={"no-answers-msg"}>Поки ніхто не пройшов тестування</p>
                        : null
                }
                {answers.filter(a => a.isFinished).map(answer =>
                    <div key={answer.id} onClick={() => selectResult(answer)} className="result-el">
                        <p className="resp-name">{answer.respondent.username}</p>
                        <p className="score">{answer.score} балів</p>
                    </div>
                )}
            </div>
        </>
    );

}

export default AnswersMenu;