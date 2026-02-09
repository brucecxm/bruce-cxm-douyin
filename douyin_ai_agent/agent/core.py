
from .memory import Memory
from .tools import TOOL_REGISTRY
from .llm_mock import fake_llm_decision

class SimpleAgent:
    def __init__(self):
        self.memory = Memory()

    def run(self, user_input):
        self.memory.add("user", user_input)
        decision = fake_llm_decision(user_input)

        if decision["type"] == "tool":
            tool_name = decision["tool"]
            args = decision["args"]
            tool_fn = TOOL_REGISTRY[tool_name]
            result = tool_fn(**args)
            self.memory.add("observation", str(result))
            return f"(used tool {tool_name}) -> {result}"

        response = decision["response"]
        self.memory.add("assistant", response)
        return response
