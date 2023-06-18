package com.poshtarenko.codeforge.x_tasks.task2;

import java.util.Arrays;

public class Task2 {
    public static void main(String[] args) {
        System.out.println(check(new Solution()));
    }

    public static String check(Solution solution){
        int[] array1 = {1, 3, 2};
        int[] arrayExpected1 = {1, 2, 3};
        solution.sort(array1);

        if (!Arrays.equals(array1, arrayExpected1)) {
            return "FAILURE";
        }

        int[] array2 = {20, 1, 5, 4, 6, 0};
        int[] arrayExpected2 = {0, 1, 4, 5, 6, 20};
        solution.sort(array2);

        if (!Arrays.equals(array2, arrayExpected2)) {
            return "FAILURE";
        }

        return "SUCCESS";
    }
}

class Solution {
    public void sort(int[] array) {
        int n = array.length;
        int temp;
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){
                if(array[j-1] > array[j]){
                    temp = array[j-1];
                    array[j-1] = array[j];
                    array[j] = temp;
                }

            }
        }
    }
}
