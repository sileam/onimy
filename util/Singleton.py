class Singleton(object):
    def __new__(cls, *args, **kwargs):
        if not hasattr(cls, "_me"):
            cls._me = super().__new__(Singleton)

        return cls._me

    @staticmethod
    def getInstance():
        return Singleton()


#  test
if __name__ == "__main__":
    testA = Singleton.getInstance()
    print(testA)
    testB = Singleton.getInstance()
    print(testB)
    print(testA == testB)
