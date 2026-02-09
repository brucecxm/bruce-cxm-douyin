#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试 AI Agent 工具参数抽取功能

测试新的工具参数抽取能力，验证以下功能：
1. random 5 9 - 生成5到9之间的随机数
2. random 100 200 - 生成100到200之间的随机数
3. calculate (8+2)*5 - 计算表达式的值
4. weather paris - 返回巴黎的天气信息
5. time - 返回当前时间
"""

from agent.core import SimpleAgent

# 创建 Agent 实例
agent = SimpleAgent()

print("=== 测试 AI Agent 工具参数抽取功能 ===")
print()

# 测试随机数工具（带参数）
print("1. 测试随机数工具（带参数）:")
test_cases = ["random 5 9", "random 100 200"]
for test_input in test_cases:
    response = agent.run(test_input)
    print(f"   输入: {test_input}")
    print(f"   输出: {response}")
    print()

# 测试计算器工具
print("2. 测试计算器工具:")
test_input = "calculate (8+2)*5"
response = agent.run(test_input)
print(f"   输入: {test_input}")
print(f"   输出: {response}")
print()

# 测试天气工具
print("3. 测试天气工具:")
test_input = "weather paris"
response = agent.run(test_input)
print(f"   输入: {test_input}")
print(f"   输出: {response}")
print()

# 测试时间工具
print("4. 测试时间工具:")
test_input = "time"
response = agent.run(test_input)
print(f"   输入: {test_input}")
print(f"   输出: {response}")
print()

# 测试默认回复
print("5. 测试默认回复:")
test_input = "hello"
response = agent.run(test_input)
print(f"   输入: {test_input}")
print(f"   输出: {response}")
print()

print("=== 测试完成 ===")
