package com.bruce;
public class TrappingRainWater {
    public static void main(String[] args) {
        TrappingRainWater solution = new TrappingRainWater();
        int[] height = {0,1,0,2,1,0,1,3,2,1,2,1};
        int trappedWater = solution.trappingRainWater(height);
        System.out.println("接水量为: " + trappedWater); // 应输出6
    }

    /**
     * 计算给定高度数组的柱子能接住的雨水量
     * @param height 表示柱子高度的数组
     * @return 总接水量
     */
    public int trappingRainWater(int[] height) {
        // 在此处实现你的代码
        return 0;
    }
}