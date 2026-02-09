
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
AI Agent 模拟大模型决策系统

大模型是 Agent 的决策引擎，负责：
1. 理解用户输入
2. 分析对话上下文
3. 决定是否调用工具
4. 选择合适的工具
5. 生成工具参数
6. 生成最终响应

真实场景中的大模型：
- OpenAI GPT-4
- Anthropic Claude
- Google Gemini
- Meta Llama
- 百度文心一言
- 阿里通义千问
- 腾讯混元

大模型决策过程：
1. 接收提示（Prompt）
2. 生成思考（Reasoning）
3. 做出决策（Decision）
4. 返回结果（Result）

Function Calling / Tool Calling：
这是现代 Agent 的核心能力，允许大模型：
- 以结构化格式返回工具调用请求
- 包含工具名称和参数
- 接收工具执行结果并继续决策

本文件模拟了大模型的决策过程，用于演示 Agent 的工作原理。
"""

import re


def fake_llm_decision(user_text):
    """
    模拟 LLM 推理：
    从自然语言中提取参数

    真实世界：
    这是 GPT 完成的事情
    我们这里只做简化规则
    """

    txt = user_text.lower().strip()

    # ========= RANDOM =========
    # 支持:
    # random 10 50
    match = re.match(r"random\s+(\d+)\s+(\d+)", txt)
    if match:
        min_v = int(match.group(1))
        max_v = int(match.group(2))

        return {
            "type": "tool",
            "tool": "random",
            "args": {
                "min_val": min_v,
                "max_val": max_v
            }
        }

    # ========= CALCULATOR =========
    if txt.startswith("calculate"):
        expr = txt.replace("calculate", "").strip()

        return {
            "type": "tool",
            "tool": "calculator",
            "args": {"expression": expr}
        }

    # ========= WEATHER =========
    match = re.match(r"weather\s+(\w+)", txt)
    if match:
        city = match.group(1)

        return {
            "type": "tool",
            "tool": "weather",
            "args": {"city": city}
        }

    # ========= TIME =========
    if txt.startswith("time"):
        return {
            "type": "tool",
            "tool": "time",
            "args": {}
        }

    # ========= 默认 =========
    return {
        "type": "text",
        "response": "I didn't understand, try using a tool 🙂"
    }


# 真实场景中的大模型调用示例：
# 
# import openai
# 
# def real_llm_decision(user_text, context):
#     """
#     真实大模型决策
#     
#     使用 OpenAI API 进行真实的大模型决策。
#     """
#     # 构建提示
#     prompt = f"""
#     You are an AI agent. Based on the user input and context, decide:
#     1. Whether to call a tool
#     2. Which tool to call (if any)
#     3. What parameters to use
#     
#     Available tools:
#     - calculator: For mathematical calculations
#     - weather: For checking weather in a city
#     
#     Context:
#     {context}
#     
#     User input:
#     {user_text}
#     
#     Format your response as JSON:
#     {{"type": "tool", "tool": "tool_name", "args": {{"param": "value"}}}} or
#     {{"type": "text", "response": "your response"}}
#     """
#     
#     # 调用 OpenAI API
#     response = openai.ChatCompletion.create(
#         model="gpt-3.5-turbo",
#         messages=[{"role": "user", "content": prompt}],
#         response_format={"type": "json_object"}
#     )
#     
#     # 解析响应
#     decision = response.choices[0].message.content
#     return eval(decision)

