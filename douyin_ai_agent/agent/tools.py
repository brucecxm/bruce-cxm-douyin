
def calculator(expression: str):
    return eval(expression)

def weather(city: str):
    return f"Weather in {city}: Sunny 30C (mock data)"

TOOL_REGISTRY = {
    "calculator": calculator,
    "weather": weather
}
