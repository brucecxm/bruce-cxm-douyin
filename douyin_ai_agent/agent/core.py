
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
import re
from .memory import Memory
from .tools import TOOL_REGISTRY
from .llm_mock import fake_llm_decision


class SimpleAgent:

    def __init__(self):
        self.memory = Memory()

    def run(self, user_input):

        self.memory.add("user", user_input)

        output_log = []

        # ⭐ 简单规划：按 and 拆分任务
        output_log.append(f"Planning: Splitting task -> {user_input}")
        # 使用正则表达式按单词边界拆分，确保只拆分完整的 "and"
        tasks = [t.strip() for t in re.split(r'\s+and\s+', user_input)]
        output_log.append(f"Tasks identified: {tasks}")

        results = []

        for task in tasks:

            decision = fake_llm_decision(task)

            output_log.append(f"\n--- New Step ---")
            output_log.append(f"Thought: Handling task -> {task}")

            # ===== 工具调用 =====
            if decision["type"] == "tool":

                tool_name = decision["tool"]
                args = decision["args"]

                output_log.append(
                    f"Action: Call [{tool_name}] with {args}"
                )

                tool_fn = TOOL_REGISTRY[tool_name]
                result = tool_fn(**args)

                output_log.append(
                    f"Observation: {result}"
                )

                results.append(result)
                self.memory.add("observation", str(result))

            else:
                response = decision["response"]
                results.append(response)

        # ⭐ 汇总最终答案
        final_answer = " | ".join(str(r) for r in results)
        output_log.append(f"\nFinal Answer: {final_answer}")

        return "\n".join(output_log)
