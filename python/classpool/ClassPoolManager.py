# coding=utf-8

from util import Singleton
from classpool.PoolBase import PoolBase


class ClassPoolManager(Singleton):
    def __init__(self):
        self.dicClassPool = {}

    def getClassPool(self, classType) -> PoolBase:
        if classType not in self.dicClassPool:
            self.dicClassPool[classType] = PoolBase(classType)
        return self.dicClassPool[classType]

    def deleteClassPool(self, classType):
        self.dicClassPool.pop(classType, None)


    def debugInfo(self):
        print("ClassPoolManager : count : ", len(self.dicClassPool.keys))


if __name__ == "__main__":
    ins = ClassPoolManager.getInstance()

