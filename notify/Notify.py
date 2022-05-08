# coding=utf-8

from util import Singleton
from callback import bind
from classpool import ClassPoolManager
from notify.Handler import Handler
from notify.Event import Event

class Notify(Singleton):

    def __init__(self):
        self.arrMessage = []
        self.kwHandler = {}
        self.main = None
        self.pool = None

    def start(self, main):
        self.main = main

    def update(self):
        pass

    def getHandlerByKey(self, strKey):
        if strKey not in self.kwHandler:
            self.kwHandler[strKey] = ClassPoolManager.getInstance().getClassPool(Handler).getInstance()
            self.kwHandler[strKey].setName(strKey)

        return self.kwHandler[strKey]

    def registerListener(self, strKey, func, ins):
        handler = self.getHandlerByKey(strKey)
        handler.append(func, ins)

    def removeListener(self, strKey, ins):
        handler = self.getHandlerByKey(strKey)
        handler.pop(ins)
        if handler.getLen() == 0:
            self.removeHandler(handler)

    def removeHandler(self, handler):
        ClassPoolManager.getInstance().getClassPool()

    def removeAllListener(self, ins):
        # 不好 不要
        pass

    def sendEvent(self, strKey, *args, **kwargs):
        event = ClassPoolManager.getInstance().getClassPool(Event).getInstance()
        event.init()








