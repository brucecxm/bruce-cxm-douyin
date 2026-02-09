#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试 AI Agent 新工具

测试时间工具和随机数工具是否正常工作。
"""

from agent.core import SimpleAgent

# 创建 Agent 实例
agent = SimpleAgent()

print("=== 测试 AI Agent 新工具 ===")
print()

# 测试时间工具
print("1. 测试时间工具:")
response = agent.run("time")
print(f"   输入: time")
print(f"   输出: {response}")
print()

# 测试随机数工具
print("2. 测试随机数工具:")
response = agent.run("random")
print(f"   输入: random")
print(f"   输出: {response}")
print()

# 测试原有工具（计算器）
print("3. 测试计算器工具:")
response = agent.run("calculate 1+2*3")
print(f"   输入: calculate 1+2*3")
print(f"   输出: {response}")
print()

# 测试原有工具（天气）
print("4. 测试天气工具:")
response = agent.run("weather Beijing")
print(f"   输入: weather Beijing")
print(f"   输出: {response}")
print()

print("=== 测试完成 ===")
