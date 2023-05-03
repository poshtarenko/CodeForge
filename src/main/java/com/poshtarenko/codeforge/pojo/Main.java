package com.poshtarenko.codeforge.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private static final Solution solution = new Solution();

    public static void main(String[] args) {
        List<Integer> listUnsorted = new ArrayList<>();
        listUnsorted.add(3);
        listUnsorted.add(5);
        listUnsorted.add(2);
        List<Integer> listExpected = new ArrayList<>();
        listExpected.add(2);
        listExpected.add(3);
        listExpected.add(5);

        List<Integer> result = solution.sort(listUnsorted);

        if (result.equals(listExpected)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILURE");
        }
    }
}

class Solution {
    public List<Integer> sort(List<Integer> list) {
        Collections.sort(list);
        return list;
    }
}