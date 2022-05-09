# coding=utf-8

from util import Singleton
from notify.Handler import Handler
from notify.Event import Event


class Notify(Singleton):

    def __init__(self):
        self.arrEvent = []
        self.arrNeedSolveEvent = []
        self.kwHandler = {}
        self.main = None
        self.poolHandler = None
        self.poolEvent = None

    def start(self, main):
        self.main = main
        self.poolHandler = self.main.getPoolManager().getClassPool(Handler)
        self.poolEvent = self.main.getPoolManager().getClassPool(Event)

    def update(self):
        if len(self.arrEvent) > 0:
            self.solveEvent()

    def solveEvent(self):
        self.arrNeedSolveEvent = self.arrEvent
        self.arrEvent.clear()

        for event in self.arrNeedSolveEvent:
            handler = self.getHandlerByKey(event.getKey())
            handler(*event.args, *handler.kwargs)

        for event in self.arrNeedSolveEvent:
            self.poolEvent.recycleInstance(event)

        self.arrNeedSolveEvent.clear()

    def getHandlerByKey(self, strKey):
        if strKey not in self.kwHandler:
            self.kwHandler[strKey] = self.poolHandler.getInstance()
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
        self.poolEvent.recycleInstance(handler)

    def removeAllListener(self, ins):
        # 不好 不要
        pass

    def sendEvent(self, strKey, *args, **kwargs):
        event = self.poolEvent.getInstance()
        event.init(strKey, *args, **kwargs)
        self.arrEvent.append(event)
