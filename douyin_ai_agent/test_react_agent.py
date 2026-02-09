#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
测试 AI Agent ReAct 模式

测试新的 ReAct 模式是否正常工作，验证以下功能：
1. random 5 20 - 生成5到20之间的随机数
2. time - 返回当前时间
3. weather london - 返回伦敦的天气信息
4. calculate 3*(5+2) - 计算表达式的值

验证输出是否包含完整的推理链：
- Thought: 思考过程
- Action: 行动
- Observation: 观察结果
- Answer: 最终回答
"""

from agent.core import SimpleAgent

# 创建 Agent 实例
agent = SimpleAgent()

print("=== 测试 AI Agent ReAct 模式 ===")
print()

# 测试用例列表
test_cases = [
    "random 5 20",
    "time",
    "weather london",
    "calculate 3*(5+2)"
]

# 测试每个用例
for i, test_input in enumerate(test_cases, 1):
    print(f"{i}. 测试: {test_input}")
    print("-" * 50)
    response = agent.run(test_input)
    print(response)
    print()
    print()

print("=== 测试完成 ===")
