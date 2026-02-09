
def fake_llm_decision(user_text):
    txt = user_text.lower()

    if "calculate" in txt:
        expr = txt.replace("calculate", "").strip()
        return {
            "type": "tool",
            "tool": "calculator",
            "args": {"expression": expr}
        }

    if "weather" in txt:
        city = txt.split()[-1]
        return {
            "type": "tool",
            "tool": "weather",
            "args": {"city": city}
        }

    return {
        "type": "text",
        "response": "Hello! I am a minimal AI agent."
    }
