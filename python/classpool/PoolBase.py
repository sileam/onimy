#coding=utf-8

class PoolBase():


    def __init__(self, classname):
        self.strName = classname
        self.arrUnusedInstance = []
        self.arrUsedInstance = []
    


    def getInstance(self):
        ins = 0
        if len(self.arrUnusedInstance) > 0:
            ins = self.arrUnusedInstance.pop()
        else:
            ins = self.createInstance()
        
        self.arrUsedInstance.append(ins)
        return ins
    

    #重写这个函数
    def createInstance(self):
        pass
    

    def recycleInstance(self, instance):
        if instance in self.arrUnusedInstance:
            self.arrUnusedInstance.remove(instance)
            self.resetInstance(instance)
            self.arrUnusedInstance.append(instance)
        else:
            print("recycleInstance error")


    
    def resetInstance(self, instance):
        pass
    
    def debugInfo(self):
        print("pool name " + self.strName)
        print("pool count ", len(self.arrUnusedInstance) + len(self.arrUsedInstance))


        

    