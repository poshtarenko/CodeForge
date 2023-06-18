INSERT INTO Roles (name)
VALUES ('AUTHOR');
INSERT INTO Roles (name)
VALUES ('RESPONDENT');
INSERT INTO Roles (name)
VALUES ('ADMIN');

INSERT INTO languages (name)
VALUES ('Java');
INSERT INTO languages (name)
VALUES ('C#');
INSERT INTO languages (name)
VALUES ('C++');
INSERT INTO languages (name)
VALUES ('Rust');
INSERT INTO languages (name)
VALUES ('Python');
INSERT INTO languages (name)
VALUES ('Typescript');
INSERT INTO languages (name)
VALUES ('Javascript');

INSERT INTO categories (name)
VALUES ('Масиви');
INSERT INTO categories (name)
VALUES ('Числа');
INSERT INTO categories (name)
VALUES ('Строки');
INSERT INTO categories (name)
VALUES ('Регулярні вирази');
INSERT INTO categories (name)
VALUES ('ООП');
INSERT INTO categories (name)
VALUES ('Машинне навчання');

INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Сортування масиву',
        'Відсортуйте масив чисел за збільшенням',
        1, 1,
        'import java.util.Arrays; public class Task1 { public static void main(String[] args) { System.out.println(check(new Solution())); } public static String check(Solution solution){ int[] array1 = {1, 3, 2}; int[] arrayExpected1 = {1, 2, 3}; solution.sort(array1); if (!Arrays.equals(array1, arrayExpected1)) { return "FAILURE"; } int[] array2 = {20, 1, 5, 4, 6, 0}; int[] arrayExpected2 = {0, 1, 4, 5, 6, 20}; solution.sort(array2); if (!Arrays.equals(array2, arrayExpected2)) { return "FAILURE"; } return "SUCCESS"; } } %s',
        'class Solution {
    public void sort(int[] array) {

    }
}');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Знайти максимум та мінімум',
        'Знайти мінімальне та максимальне значення у масиві чисел',
        1, 1, '%s',
        'class Solution {
    public float maximum(int[] numbers) {

    }
}');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Знайти середнє значення',
        'Знайдіть середнє значення у масиві дробових чисел',
        1, 1,
        'public class Task1 { public static void main(String[] args) { System.out.println(check(new Solution())); } public static String check(Solution solution) { float[] array1 = {5, 2, 3, 10}; float expected1 = 5; float result1 = solution.average(array1); if (result1 != expected1) { return "FAILURE"; } return "SUCCESS"; } } %s',
        'class Solution {
    public float average(float[] numbers) {

    }
}');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Поєднати два числа без використання рядків',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        1, 2, '%s', '...');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Розділити рядок за умовою',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        1, 3, '%s', '...');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Конкатенація строк',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        1, 3, '%s', '...');

INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Array sorting',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        2, 1, '%s', '...');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Find maximum and minimum',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        2, 1, '%s', '...');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Знайти середнє значення',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        2, 1, '%s', '...');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Поєднати два числа без використання рядків',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        2, 2, '%s', '...');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Розділити рядок за умовою',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        2, 3, '%s', '...');
INSERT INTO problems (name, description, language_id, category_id, testing_code, template_code)
VALUES ('Конкатенація строк',
        'Хочемо підкорювати киян якістю накопичення і прагнемо розвивати послуги доставки кореспонденції, закупівлю-продаж і передачу даних разом із гуртовими постачальниками. Завдяки довершеним та прогресивним продуктам та послугам, талановитим співробітникам і відповідальному ставленню до інновацій та зберігання даних',
        2, 3, '%s', '...');

