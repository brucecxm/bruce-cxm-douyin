#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试 AI Agent 多步规划功能

测试新的多步规划功能是否正常工作，验证以下场景：
1. 多个任务组合："weather tokyo and calculate 5*6"
2. 多个不同工具："time and random 1 10"
3. 单个任务（确保兼容性）："weather london"
"""

from agent.core import SimpleAgent

# 创建 Agent 实例
agent = SimpleAgent()

print("=== 测试 AI Agent 多步规划功能 ===")
print()

# 测试用例列表
test_cases = [
    "weather tokyo and calculate 5*6",
    "time and random 1 10",
    "weather london"
]

# 测试每个用例
for i, test_input in enumerate(test_cases, 1):
    print(f"{i}. 测试: {test_input}")
    print("-" * 60)
    response = agent.run(test_input)
    print(response)
    print()
    print("=" * 60)
    print()

print("=== 测试完成 ===")
