
class Memory:
    def __init__(self):
        self.history = []

    def add(self, role, content):
        self.history.append((role, content))

    def get_all(self):
        return self.history
