class Handler:

    def __init__(self):
        self.strKey = ""
        self.dicCallback = {}

    def __call__(self, *args, **kwargs):
        for func in self.dicCallback.values():
            func(*args, **kwargs)

    def setName(self, strName):
        self.strKey = strName

    def reset(self):
        self.strKey = ""
        self.dicCallback = {}

    def append(self, callback, ins):
        self.dicCallback[ins] = callback

    def pop(self, ins):
        self.dicCallback.pop(ins, None)

    def getLen(self):
        return len(self.dicCallback)

