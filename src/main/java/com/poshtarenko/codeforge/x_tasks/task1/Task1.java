package com.poshtarenko.codeforge.x_tasks.task1;

public class Task1 {
    public static void main(String[] args) {
        System.out.println(check(new Solution()));
    }

    public static String check(Solution solution) {
        float[] array1 = {5, 2, 3, 10};
        float expected1 = 5;
        float result1 = solution.average(array1);
        if (result1 != expected1) {
            return "FAILURE";
        }
        return "SUCCESS";
    }
}

class Solution {
    public float average(float[] numbers) {
        float sum = 0;
        for (float number : numbers) {
            sum += number;
        }
        return sum / numbers.length;
    }
}
