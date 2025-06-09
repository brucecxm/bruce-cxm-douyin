package com.bruce;
import java.util.regex.Pattern;

public class NameValidatorTest {

    public static void main(String[] args) {
        Pattern CHINESE_PATTERN = Pattern.compile("^[^\\s|]*[\u4e00-\u9fa5][^\\s|]*$");
        Pattern ENGLISH_PATTERN = Pattern.compile("^[^\\s|][^|]*[^\\s|]$");

        String[] testInputs = {
                "张三", "李雷", "韩梅梅",
                "John", "John Smith", "Alice Johnson",
                "张 三",         // 含空格
                "张|三",         // 含竖线
                "张三123",       // 含数字
                "（张三）",      // 含括号
                "张-三",         // 含特殊符号
                "123张三",       // 含数字
                " John",         // 首部空格
                "John ",         // 尾部空格
                "John|Smith",    // 含竖线
                "J@hn",          // 特殊符号（正则暂时允许）
                "John123",       // 含数字
                " John Smith ",  // 首尾空格
        };

        for (String value : testInputs) {
            try {
                validateName(value, CHINESE_PATTERN, ENGLISH_PATTERN);
                System.out.println("✅ 通过: \"" + value + "\"");
            } catch (IllegalArgumentException e) {
                System.out.println("❌ 不通过: \"" + value + "\" - 原因: " + e.getMessage());
            }
        }
    }

    public static void validateName(String value, Pattern CHINESE_PATTERN, Pattern ENGLISH_PATTERN) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("请输入收款人姓名");
        }

        if (value.contains("|")) {
            throw new IllegalArgumentException("姓名中不能含有竖线符号");
        }

        boolean containsChinese = value.matches(".*[\u4e00-\u9fa5].*");

        if (containsChinese) {
            if (value.contains(" ")) {
                throw new IllegalArgumentException("中文姓名中不能含有空格");
            }
            if (!CHINESE_PATTERN.matcher(value).matches()) {
                throw new IllegalArgumentException("中文姓名格式不正确");
            }
        } else {
            if (value.startsWith(" ") || value.endsWith(" ")) {
                throw new IllegalArgumentException("英文姓名首尾不能有空格");
            }
            if (!ENGLISH_PATTERN.matcher(value).matches()) {
                throw new IllegalArgumentException("英文姓名格式不正确");
            }
        }
    }
}