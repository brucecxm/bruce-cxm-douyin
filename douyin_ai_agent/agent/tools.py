
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
AI Agent 工具系统

工具系统是 Agent 的能力扩展模块，允许 Agent 执行：
1. 计算操作
2. 信息查询
3. 外部 API 调用
4. 文件操作
5. 网络搜索
6. 代码执行
7. 数据库操作
8. 等等

工具系统的核心概念：
1. 工具函数：具体执行任务的函数
2. 工具注册表：存储工具名称到工具函数的映射
3. 工具调用：Agent 通过工具名称调用对应的工具函数

真实场景中的工具示例：
- 计算器工具：执行数学运算
- 天气工具：查询天气信息
- 搜索工具：使用 Google 搜索
- 代码执行工具：运行 Python 代码
- 数据库工具：查询数据库
- 邮件工具：发送邮件
- 文件工具：读写文件

工具系统的作用：
1. 扩展 Agent 的能力边界
2. 让 Agent 能够与外部世界交互
3. 提供实时、准确的信息
4. 执行复杂的任务
"""

# 导入必要的模块
import random
from datetime import datetime


def calculator(expression: str):
    """
    计算器工具
    
    执行数学表达式计算。
    
    Args:
        expression: 数学表达式字符串，如 "1+2*3"
        
    Returns:
        计算结果
    """
    # 使用 eval 函数执行数学表达式
    # 注意：在真实场景中，需要进行安全处理，防止代码注入
    return eval(expression)


def weather(city: str):
    """
    天气工具
    
    查询指定城市的天气信息（模拟数据）。
    
    Args:
        city: 城市名称，如 "Beijing"
        
    Returns:
        天气信息字符串
    """
    # 返回模拟的天气数据
    # 真实场景中，这里会调用天气 API 获取真实数据
    return f"Weather in {city}: Sunny 30C (mock data)"


# ===== 新工具1：获取当前时间 =====
def get_time():
    """
    返回当前时间
    这是典型的工具函数
    Agent可以调用它获取外部信息
    """
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")


# ===== 新工具2：生成随机数 =====
def random_number(min_val: int, max_val: int):
    """
    生成随机数
    演示带参数的工具调用
    """
    return random.randint(min_val, max_val)


# 工具注册表
# 存储工具名称到工具函数的映射
# 格式：{工具名称: 工具函数}
# 在真实场景中，工具注册表可能会：
# 1. 自动注册（使用装饰器）
# 2. 动态加载（从配置文件或数据库）
# 3. 包含工具描述（用于大模型理解）
TOOL_REGISTRY = {
    "calculator": calculator,    # 计算器工具
    "weather": weather,          # 天气工具
    
    # 新加 👇
    "time": get_time,            # 当前时间工具
    "random": random_number,     # 随机数工具
}

"""
工具注册表扩展示例（真实场景）：

TOOL_REGISTRY = {
    "calculator": {
        "function": calculator,
        "description": "执行数学计算",
        "parameters": {
            "expression": {
                "type": "string",
                "description": "数学表达式"
            }
        }
    },
    "weather": {
        "function": weather,
        "description": "查询城市天气",
        "parameters": {
            "city": {
                "type": "string",
                "description": "城市名称"
            }
        }
    }
}
"""

