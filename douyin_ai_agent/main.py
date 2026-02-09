
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
AI Agent 入门项目主文件

这是一个最小化的 AI Agent 实现，展示了 Agent 的核心组件：
1. Agent 循环（核心灵魂）
2. 记忆系统（短期记忆）
3. 工具系统（能力扩展）
4. 模拟大模型（决策引擎）

项目结构：
agent/
  ├── core.py        # Agent循环（核心）
  ├── memory.py      # 短期记忆
  ├── tools.py       # 工具系统
  ├── llm_mock.py    # 模拟大模型
main.py              # 主入口

运行方式：
python main.py

测试示例：
1. 计算功能：calculate 1+2*3
2. 天气功能：weather Beijing
3. 普通对话：hello
"""

# 导入 SimpleAgent 类
from agent.core import SimpleAgent

if __name__ == "__main__":
    # 初始化 Agent 实例
    # 内部会创建：
    # 1. 记忆系统（Memory）
    # 2. 工具注册表（TOOL_REGISTRY）
    # 3. 模拟大模型决策系统
    agent = SimpleAgent()

    # 打印欢迎信息
    print("=== Simple AI Agent ===")
    print("Type 'exit' to quit")

    # 主交互循环
    while True:
        # 获取用户输入
        user = input("\nYou: ")
        # 退出条件
        if user.lower() == "exit":
            break

        # 执行 Agent 循环
        # 流程：
        # 1. 接收用户输入
        # 2. 存储到记忆
        # 3. 调用大模型决策
        # 4. 执行工具（如果需要）
        # 5. 存储执行结果
        # 6. 返回响应
        response = agent.run(user)
        # 打印 Agent 响应
        print("Agent:", response)
