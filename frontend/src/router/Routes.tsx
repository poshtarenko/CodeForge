import RegistrationPage from "../pages/authPages/RegistrationPage";
import Login from "../pages/authPages/LoginPage";
import {FC} from "react";
import TestsPage from "../pages/testsPage/TestsPage";
import TestPage from "../pages/./testPage/TestPage";
import EnterCodePage from "../pages/enterCodePage/EnterCodePage";
import RespondentSessionPage from "../pages/respondentSessionPage/RespondentSessionPage";
import LoginPage from "../pages/authPages/LoginPage";
import LessonsPage from "../pages/lessonsPage/LessonsPage";
import LessonPage from "../pages/lessonPage/LessonPage";
import LessonSessionPage from "../pages/lessonSessionPage/lessonSessionPage";

export interface IRoute {
    path: string,
    component: FC
}

export const authorRoutes: IRoute[] = [
    {path: '/test/:id', component: TestPage},
    {path: '/tests', component: TestsPage},
    {path: '/lessons', component: LessonsPage},
    {path: '/lesson/:id', component: LessonPage},
    {path: '/lesson/session/:lessonId', component: LessonSessionPage},
]

export const respondentRoutes: IRoute[] = [
    {path: '/start', component: EnterCodePage},
    {path: '/session/:testId', component: RespondentSessionPage},
]

export const publicRoutes: IRoute[] = [
    {path: '/register', component: RegistrationPage},
    {path: '/login', component: LoginPage},
]