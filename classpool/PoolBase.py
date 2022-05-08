# coding=utf-8

class PoolBase:

    def __init__(self, classType):
        self.strName = classType.__name__
        self.classType = classType
        self.arrUnusedInstance = []
        self.arrUsedInstance = []

    def getInstance(self):
        if len(self.arrUnusedInstance) > 0:
            ins = self.arrUnusedInstance.pop()
        else:
            ins = self.createInstance()

        self.arrUsedInstance.append(ins)
        return ins

    def createInstance(self):
        return self.classType()

    def recycleInstance(self, instance):
        if instance in self.arrUsedInstance:
            self.arrUsedInstance.remove(instance)
            self.resetInstance(instance)
            self.arrUnusedInstance.append(instance)
        else:
            print("recycleInstance error ", self.strName)

    def resetInstance(self, instance):
        if hasattr(instance, "reset"):
            instance.reset()

    def reset(self):
        for ins in self.arrUsedInstance:
            self.resetInstance(ins)

        self.arrUnusedInstance.extend(self.arrUsedInstance)
        self.arrUsedInstance = []

    def debugInfo(self):
        print("pool name " + self.strName)
        print("pool count ", len(self.arrUnusedInstance) + len(self.arrUsedInstance))


class Test:
    def __init__(self):
        self.a = 1
        self.b = 2

    def reset(self):
        self.a = 1
        self.b = 2
        print("reset")


if __name__ == "__main__":
    pool = PoolBase(Test)
    ins1 = pool.getInstance()
    ins2 = pool.getInstance()

    pool.recycleInstance(ins1)
    pool.recycleInstance(ins2)

    ins3 = pool.getInstance()

    pool.debugInfo()

