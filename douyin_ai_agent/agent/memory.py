
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
AI Agent 记忆系统

记忆系统是 Agent 的重要组成部分，用于存储：
1. 对话历史（用户输入、Agent 回复）
2. 工具执行结果（观察结果）

记忆类型：
1. 短期记忆（Short-term Memory）
   - 存储当前对话上下文
   - 用于单次对话的连贯性
   - 本文件实现的就是短期记忆

2. 长期记忆（Long-term Memory）
   - 存储历史对话和知识
   - 用于跨对话的知识积累
   - 真实场景中使用向量数据库实现

工业级长期记忆实现：
- Redis（缓存）
- Pinecone（向量数据库）
- Milvus（向量数据库）
- Weaviate（向量数据库）
- FAISS（本地向量索引）

记忆系统的作用：
1. 提供上下文理解
2. 支持多轮对话
3. 实现知识积累
4. 辅助决策制定
"""


class Memory:
    """
    简单记忆系统实现
    
    这是一个短期记忆系统，用于存储对话历史和执行结果。
    在真实场景中，还会包含长期记忆系统。
    """
    
    def __init__(self):
        """
        初始化记忆系统
        
        创建一个空的历史记录列表，用于存储对话历史。
        每条记录是一个元组：(role, content)
        - role: 角色（user/assistant/observation）
        - content: 内容
        """
        # 初始化历史记录列表
        # 存储格式：[(role, content), (role, content), ...]
        # 角色说明：
        # - user: 用户输入
        # - assistant: Agent 回复
        # - observation: 工具执行结果
        self.history = []

    def add(self, role, content):
        """
        添加记忆
        
        将一条记录添加到历史记录中。
        
        Args:
            role: 角色，如 "user", "assistant", "observation"
            content: 内容，如用户输入、Agent 回复、工具执行结果
        """
        # 添加到历史记录列表
        self.history.append((role, content))

    def get_all(self):
        """
        获取所有记忆
        
        返回完整的历史记录列表。
        
        Returns:
            list: 历史记录列表，格式为 [(role, content), ...]
        """
        # 返回完整的历史记录
        return self.history
