#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
AI Agent 可视化界面（优化版）

使用 tkinter 创建一个美观的 GUI 界面，让用户可以通过图形界面与 AI Agent 交互，而不是在控制台输入。

优化功能：
1. 美观的界面设计
2. 动画效果：思考过程逐个显示，每个过程维持一秒
3. 过程圈：显示加载动画
4. TodoList 格式：按照任务列表格式显示推理过程
"""

import tkinter as tk
from tkinter import scrolledtext, ttk
import time
import threading


class AgentGUI:
    """
    AI Agent 可视化界面类
    """
    
    def __init__(self, root):
        """
        初始化 GUI 界面
        
        Args:
            root: 主窗口实例
        """
        # 设置主窗口标题
        self.root = root
        self.root.title("AI Agent Interface")
        # 设置窗口大小
        self.root.geometry("850x650")
        # 设置窗口背景色
        self.root.config(bg="#f5f5f5")
        
        # 初始化 Agent 实例
        from agent.core import SimpleAgent
        self.agent = SimpleAgent()
        
        # 创建界面组件
        self.create_widgets()
        
        # 初始化加载动画状态
        self.loading = False
        self.loading_counter = 0
        
    def create_widgets(self):
        """
        创建界面组件
        """
        # 1. 创建顶部标题栏
        title_frame = tk.Frame(self.root, bg="#4CAF50", pady=15)
        title_frame.pack(fill=tk.X)
        
        title_label = tk.Label(
            title_frame, 
            text="AI Agent Interface", 
            font=("Arial", 18, "bold"),
            fg="white",
            bg="#4CAF50"
        )
        title_label.pack()
        
        # 2. 创建输入区域
        input_frame = tk.Frame(self.root, pady=20, padx=30, bg="#f5f5f5")
        input_frame.pack(fill=tk.X)
        
        # 输入标签
        input_label = tk.Label(
            input_frame, 
            text="Your Input:", 
            font=("Arial", 12, "bold"),
            bg="#f5f5f5"
        )
        input_label.pack(anchor=tk.W, pady=(0, 5))
        
        # 输入文本框
        self.input_text = tk.Entry(
            input_frame, 
            width=90,
            font=("Arial", 12),
            bd=2,
            relief=tk.GROOVE
        )
        self.input_text.pack(fill=tk.X, pady=(0, 10))
        # 绑定回车键到发送函数
        self.input_text.bind("<Return>", self.send_message)
        
        # 发送按钮
        send_button = tk.Button(
            input_frame, 
            text="Send", 
            command=self.send_message,
            width=12,
            height=1,
            font=("Arial", 11, "bold"),
            bg="#4CAF50",
            fg="white",
            bd=0,
            relief=tk.FLAT,
            cursor="hand2"
        )
        send_button.pack(anchor=tk.E)
        
        # 3. 创建输出区域
        output_frame = tk.Frame(self.root, pady=10, padx=30, bg="#f5f5f5")
        output_frame.pack(fill=tk.BOTH, expand=True)
        
        # 输出标签
        output_label = tk.Label(
            output_frame, 
            text="Agent Response:", 
            font=("Arial", 12, "bold"),
            bg="#f5f5f5"
        )
        output_label.pack(anchor=tk.W, pady=(0, 10))
        
        # 输出容器（带边框）
        output_container = tk.Frame(
            output_frame, 
            bd=2, 
            relief=tk.GROOVE,
            bg="white"
        )
        output_container.pack(fill=tk.BOTH, expand=True)
        
        # 输出文本框（带滚动条）
        self.output_text = scrolledtext.ScrolledText(
            output_container, 
            width=90, 
            height=20,
            font=("Courier New", 11),
            wrap=tk.WORD,
            bd=0,
            bg="white"
        )
        self.output_text.pack(fill=tk.BOTH, expand=True)
        # 设置输出文本框为只读
        self.output_text.config(state=tk.DISABLED)
        
        # 4. 创建底部说明
        info_frame = tk.Frame(self.root, pady=15, padx=30, bg="#e0e0e0")
        info_frame.pack(fill=tk.X)
        
        info_text = "Examples: 'random 5 20', 'time', 'weather london', 'calculate 3*(5+2)'"
        info_label = tk.Label(
            info_frame, 
            text=info_text, 
            font=("Arial", 10),
            bg="#e0e0e0"
        )
        info_label.pack(anchor=tk.W)
    
    def send_message(self, event=None):
        """
        发送消息给 Agent 并显示回复
        
        Args:
            event: 事件对象（当通过回车键触发时）
        """
        # 获取用户输入
        user_input = self.input_text.get().strip()
        
        # 检查输入是否为空
        if not user_input:
            return
        
        # 清空输入框
        self.input_text.delete(0, tk.END)
        
        # 显示用户输入
        self.display_message("You: " + user_input)
        
        # 禁用输入框，防止重复提交
        self.input_text.config(state=tk.DISABLED)
        
        # 在新线程中处理 Agent 响应，避免阻塞 GUI
        def process_agent_response():
            try:
                response = self.agent.run(user_input)
                # 显示 Agent 响应（带动画效果）
                self.display_agent_response_with_animation(response)
            except Exception as e:
                # 显示错误信息
                self.display_message(f"Error: {str(e)}")
            finally:
                # 重新启用输入框
                self.input_text.config(state=tk.NORMAL)
        
        # 启动线程
        thread = threading.Thread(target=process_agent_response)
        thread.daemon = True
        thread.start()
    
    def display_agent_response_with_animation(self, response):
        """
        显示 Agent 响应，带动画效果
        
        Args:
            response: Agent 的响应文本
        """
        # 显示 Agent 标签
        self.display_message("Agent:")
        
        # 分割响应为多个步骤
        lines = response.strip().split('\n')
        steps = []
        
        # 提取步骤
        for line in lines:
            line = line.strip()
            if line:
                steps.append(line)
        
        # 逐个显示步骤，带动画效果
        for i, step in enumerate(steps):
            # 显示步骤（带过程圈）
            self.display_step_with_loading(step, i + 1, len(steps))
            # 等待一秒
            time.sleep(1)
    
    def display_step_with_loading(self, step, step_num, total_steps):
        """
        显示单个步骤，带加载动画
        
        Args:
            step: 步骤文本
            step_num: 步骤序号
            total_steps: 总步骤数
        """
        # 启用输出文本框
        self.output_text.config(state=tk.NORMAL)
        
        # 生成过程圈（使用不同的符号表示不同状态）
        loading_symbols = ['○', '◔', '◑', '◕', '●']
        symbol = loading_symbols[step_num % len(loading_symbols)]
        
        # 按照 TodoList 格式显示
        if "Thought:" in step:
            display_text = f"{symbol} [{step_num}/{total_steps}] {step}"
        elif "Action:" in step:
            display_text = f"{symbol} [{step_num}/{total_steps}] {step}"
        elif "Observation:" in step:
            display_text = f"{symbol} [{step_num}/{total_steps}] {step}"
        elif "Answer:" in step:
            display_text = f"✓ [{step_num}/{total_steps}] {step}"
        else:
            display_text = f"{symbol} [{step_num}/{total_steps}] {step}"
        
        # 插入文本
        self.output_text.insert(tk.END, display_text + "\n")
        # 滚动到末尾
        self.output_text.see(tk.END)
        # 更新 GUI
        self.output_text.update_idletasks()
        
        # 禁用输出文本框（保持只读）
        self.output_text.config(state=tk.DISABLED)
    
    def display_message(self, message):
        """
        在输出区域显示消息
        
        Args:
            message: 要显示的消息
        """
        # 启用输出文本框
        self.output_text.config(state=tk.NORMAL)
        
        # 插入消息，自动换行
        if "You:" in message:
            # 用户输入，使用不同颜色
            self.output_text.tag_config("user", foreground="#2196F3")
            self.output_text.insert(tk.END, message + "\n\n", "user")
        elif "Agent:" in message:
            # Agent 标签，使用不同颜色
            self.output_text.tag_config("agent", foreground="#4CAF50", font=("Arial", 11, "bold"))
            self.output_text.insert(tk.END, message + "\n", "agent")
        elif "Error:" in message:
            # 错误信息，使用不同颜色
            self.output_text.tag_config("error", foreground="#F44336")
            self.output_text.insert(tk.END, message + "\n\n", "error")
        else:
            # 普通消息
            self.output_text.insert(tk.END, message + "\n\n")
        
        # 滚动到末尾
        self.output_text.see(tk.END)
        # 更新 GUI
        self.output_text.update_idletasks()
        
        # 禁用输出文本框（保持只读）
        self.output_text.config(state=tk.DISABLED)


if __name__ == "__main__":
    # 创建主窗口
    root = tk.Tk()
    # 初始化 GUI
    app = AgentGUI(root)
    # 运行主循环
    root.mainloop()
