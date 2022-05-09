# coding=utf-8

# 打开游戏，登陆界面，
# 按H弹出help界面
# imageManager
# config
from classpool import ClassPoolManager
from notify import Notify


class Main:
    def __new__(cls, *args, **kwargs):
        if not hasattr(cls, "_ins"):
            cls._ins = super().__new__(cls, *args, *kwargs)

        return cls._ins

    def __init__(self):
        self.poolManager = ClassPoolManager.getInstance()
        self.stateManager = None
        self.imageManager = None
        self.configManager = None
        self.notifyManager = Notify.getInstance()
        self.timeManager = None

    def start(self):
        self.notifyManager.start(self)

    def getPoolManager(self):
        return self.poolManager

    def getStateManager(self):
        return self.stateManager

    def getImageManager(self):
        return self.imageManager

    def getConfigManager(self):
        return self.configManager


if __name__ == "__main__":
    main = Main()
