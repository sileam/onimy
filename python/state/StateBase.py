# coding=utf-8

class StateBase:

    def __init__(self, name):
        self.name = name

    def check(self):
        pass
    
    def update(self):
        pass
    
    def changeToNext(self):
        pass

