export const SignatureExample: string = `class Solution {
    public void sort(int[] array) {

    }
}`

export const TestingCodeExample: string = `import java.util.Arrays;
    
class Test {
    public boolean test(Solution solution) {
        int[] array1 = {1, 3, 2};
        int[] arrayExpected1 = {1, 2, 3};
        solution.sort(array1);
        if (!Arrays.equals(array1, arrayExpected1)) {
            return false;
        }
        int[] array2 = {20, 1, 5, 4, 6, 0};
        int[] arrayExpected2 = {0, 1, 4, 5, 6, 20};
        solution.sort(array2);
        if (!Arrays.equals(array2, arrayExpected2)) {
            return false;
        }
        return true;
    }
}`