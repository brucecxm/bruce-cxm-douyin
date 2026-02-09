
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
# 导入工具注册表
from .tools import TOOL_REGISTRY
# 导入模拟大模型决策函数
from .llm_mock import fake_llm_decision


class SimpleAgent:
    """
    简单 AI Agent 实现
    
    这是一个最小化的 Agent 实现，包含了以下核心组件：
    1. 记忆系统（短期记忆）
    2. 工具系统（能力扩展）
    3. 决策系统（模拟大模型）
    4. 执行系统（工具调用）
    """
    
    def __init__(self):
        """
        初始化 Agent
        
        创建记忆系统实例，用于存储对话历史和执行结果。
        在真实场景中，这里还会初始化：
        1. 大模型客户端（如 OpenAI API）
        2. 工具注册表
        3. 长期记忆系统（如向量数据库）
        """
        # 初始化短期记忆系统
        self.memory = Memory()

    def run(self, user_input):
        """
        Agent 核心循环（Agent Loop）
        
        这是 Agent 的灵魂，实现了 ReAct 模式的核心流程：
        1. 接收用户输入
        2. 存储到记忆
        3. 大模型决策（Reason）
        4. 执行工具（Act）
        5. 观察结果（Observe）
        6. 再次思考（Reason）
        7. 返回响应
        
        Args:
            user_input: 用户输入的文本
            
        Returns:
            str: Agent 的响应
        """
        # 1. 将用户输入添加到短期记忆
        # 角色：user
        # 内容：用户输入
        self.memory.add("user", user_input)
        
        # 2. 调用大模型进行决策（Reason）
        # 真实场景：decision = GPT(prompt)
        # 大模型返回决策结果，包括：
        # - 是否调用工具
        # - 调用哪个工具
        # - 工具参数
        decision = fake_llm_decision(user_input)

        # 3. 检查决策类型
        # 如果是工具调用（tool）
        if decision["type"] == "tool":
            # 获取工具名称
            tool_name = decision["tool"]
            # 获取工具参数
            args = decision["args"]
            # 从工具注册表中获取工具函数
            tool_fn = TOOL_REGISTRY[tool_name]
            # 4. 执行工具（Act）
            # 调用工具函数，传入参数
            result = tool_fn(**args)
            # 5. 观察结果（Observe）
            # 将工具执行结果添加到短期记忆
            # 角色：observation
            # 内容：工具执行结果
            self.memory.add("observation", str(result))
            # 返回包含工具调用信息的响应
            return f"(used tool {tool_name}) -> {result}"

        # 如果是直接回答（text）
        # 获取大模型生成的响应
        response = decision["response"]
        # 将响应添加到短期记忆
        # 角色：assistant
        # 内容：Agent 响应
        self.memory.add("assistant", response)
        # 返回响应
        return response
