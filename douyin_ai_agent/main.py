
from agent.core import SimpleAgent

if __name__ == "__main__":
    agent = SimpleAgent()

    print("=== Simple AI Agent ===")
    print("Type 'exit' to quit")

    while True:
        user = input("\nYou: ")
        if user.lower() == "exit":
            break

        response = agent.run(user)
        print("Agent:", response)
