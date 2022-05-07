#coding=utf-8

class CallBack():
    

    def __init__(self, func, *args, **kwargs):
        self.func = func
        self.args = args
        self.kwargs = kwargs
    

    def __call__(self, *args, **kwargs):
        curArgs = self.args + args
        self.kwargs.update(kwargs)
        self.func(*curArgs, **self.kwargs)


def bind(func, *args, **kwargs):
    return CallBack(func, *args, **kwargs)


class Test():
    

    def __init__(self):
        self.test1 = "test"
    
    def testFunc(self, a1, a2, a3, a4, a11, a12, a5, a6, a7, a8):
        print("a1: ", a1)
        print("self.test ", self.test1)
        print("a2: ", a2)
        print("a3: ", a3)
        print("a4: ", a4)
        print("a5: ", a5)
        print("a6: ", a6)
        print("a7: ", a7)
        print("a8: ", a8)
        print("a11: ", a11)
        print("a12: ", a12)
    
    def main(self):
        tesFunc = bind(self.testFunc, None, 13, 3, None, a5 = None, a6 = 5, a7 = None)
        tesFunc(12, None, a8 = 100)

if __name__ == "__main__":
    t = Test()
    t.main()




