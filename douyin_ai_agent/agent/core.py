
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
AI Agent 核心实现

这是 AI Agent 的核心循环实现，展示了 Agent 的基本工作原理：

Agent = LLM + 工具 + 记忆 + 循环决策

核心模式：ReAct 模式（Reason → Act → Observe → Reason）
- Reason: 大模型思考
- Act: 执行工具
- Observe: 观察结果
- Reason: 再次思考

这是 Google 提出的行业主流模式，被 LangChain、OpenAI Agents 等框架广泛采用。
"""

# 导入记忆系统
from .memory import Memory
from .tools import TOOL_REGISTRY
from .llm_mock import fake_llm_decision


class SimpleAgent:

    def __init__(self):
        self.memory = Memory()

    def run(self, user_input):

        # ===== 记录用户输入 =====
        self.memory.add("user", user_input)

        # ===== 模拟模型思考 =====
        decision = fake_llm_decision(user_input)

        output_log = []

        # ⭐ Thought（思考）
        thought = f"I think user wants: {user_input}"
        output_log.append(f"Thought: {thought}")

        # ===== 是否调用工具 =====
        if decision["type"] == "tool":

            tool_name = decision["tool"]
            args = decision["args"]

            output_log.append(f"Action: Call tool [{tool_name}] with {args}")

            # 执行工具
            tool_fn = TOOL_REGISTRY[tool_name]
            result = tool_fn(**args)

            # ⭐ Observation（观察）
            output_log.append(f"Observation: Tool returned -> {result}")

            self.memory.add("observation", str(result))

            # ⭐ 最终回答
            final_answer = f"Here you go 🙂 Result = {result}"
            output_log.append(f"Answer: {final_answer}")

            return "\n".join(output_log)

        # ===== 不调用工具 =====
        response = decision["response"]

        output_log.append(f"Answer: {response}")

        self.memory.add("assistant", response)

        return "\n".join(output_log)
