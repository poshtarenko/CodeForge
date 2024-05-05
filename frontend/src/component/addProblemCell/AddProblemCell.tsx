import {observer} from "mobx-react-lite";
import React, {useState} from "react";
import "./addProblemCell.css";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFloppyDisk, faPlus} from "@fortawesome/free-solid-svg-icons";
import ProblemService from "../../services/ProblemService";
import {ILanguage} from "../../models/entity/ILanguage";

interface IProps {
    afterSave: () => void;
    languages: ILanguage[]
}

const AddProblemCell: React.FC<IProps> = ({afterSave, languages}) => {
    const [active, setActive] = useState<boolean>(false);
    const [name, setName] = useState<string>("");
    const [language, setLanguage] = useState<ILanguage>({} as ILanguage);

    async function createProblem() {
        await ProblemService.createCustomProblem({name: name, languageId: language.id});
        afterSave();
        setActive(false);
    }

    function selectLanguage(id: number) {
        setLanguage(languages.find((l => l.id === id))!);
    }

    return (
        active
            ?
            <div className={"add-problem-active"}>
                <div className={"inner"}>
                    <p className={"add-problem-title"}>Нове завдання</p>
                    <div className="inputs">
                        <input onChange={(e) => setName(e.target.value)}
                               className={"problem-name-input standard-input"}
                               type="text"
                               placeholder={"Назва"}
                        />
                        <select className={"problem-language-select"}
                                onChange={(e) => selectLanguage(Number(e.target.value))}
                                defaultValue={"NONE"}>
                            <option value="NONE" disabled>Мова</option>
                            {languages.map(language =>
                                <option key={Number(language.id)} value={String(language.id)}>{language.name}</option>)}
                        </select>
                    </div>
                    <div className={"buttons"}>
                        <button className={"standard-button"} onClick={() => setActive(false)}>Скасувати</button>
                        <button onClick={() => createProblem()}
                                className={"standard-button"}>Створити
                        </button>
                    </div>
                </div>
            </div>
            :
            <div className={"add-problem-inactive"} onClick={() => setActive(true)}>
                <FontAwesomeIcon className={"add-icon"} icon={faPlus}/>
            </div>
    );
};

export default observer(AddProblemCell);