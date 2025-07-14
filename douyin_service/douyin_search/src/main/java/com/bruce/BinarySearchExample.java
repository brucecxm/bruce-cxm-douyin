package com.bruce;

public class BinarySearchExample {

    /**
     * 二分查找的迭代实现
     *
     * @param arr    待查找的有序数组（升序）
     * @param target 目标值
     * @return 目标值在数组中的索引，如果不存在返回-1
     */
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (target == arr[mid]) {
                return mid;
            }
            if (target < arr[mid]) {
                right = mid - 1;
            }
            if (target > arr[mid]) {
                left = mid + 1;
            }
        }
        return -1;
    }



    public static void main(String[] args) {
        // 测试用例
        int[] evenArray = {2, 4, 6, 8, 10, 12}; // 偶数长度数组
        int[] oddArray = {1, 3, 5, 7, 9, 11, 13}; // 奇数长度数组

        // 测试迭代方法
        System.out.println("=== 迭代方法测试 ===");
        testBinarySearch(evenArray, 8);  // 存在于偶数数组
        testBinarySearch(evenArray, 7);  // 不存在于偶数数组
        testBinarySearch(oddArray, 7);   // 存在于奇数数组
        testBinarySearch(oddArray, 4);   // 不存在于奇数数组


    }

    // 辅助方法：测试迭代二分查找并打印结果
    private static void testBinarySearch(int[] arr, int target) {
        int result = binarySearch(arr, target);
        if (result != -1) {
            System.out.printf("数组 %s 中找到目标值 %d，索引为 %d%n",
                    java.util.Arrays.toString(arr), target, result);
        } else {
            System.out.printf("数组 %s 中未找到目标值 %d%n",
                    java.util.Arrays.toString(arr), target);
        }
    }


}