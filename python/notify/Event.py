class Event:
    args = ()
    kwargs = {}
    strKey = ""

    def init(self, strKey, *args, **kwargs):
        self.strKey = strKey
        self.args = args
        self.kwargs = kwargs

    def reset(self):
        self.strKey = ""
        self.args = ()
        self.kwargs = {}
