import subprocess
import time


# 执行 adb 命令的函数
def adb_command(command):
    subprocess.run(command, shell=True)


# 按照顺序执行命令
def perform_actions():
    # 点击滑动按钮
    adb_command("adb shell input swipe 500 1500 500 1100 500")
    time.sleep(1)  # 每次操作间延迟 1 秒

    # 点击选择按钮
    adb_command("adb shell input tap 520 1500")
    time.sleep(1)

    # 点击立即沟通按钮
    adb_command("adb shell input tap 1000 2200")
    time.sleep(2)


    adb_command("adb shell input tap 820 2300")
    time.sleep(1)


# 循环执行 1000 次
for i in range(10000):
    print(f"开始第 {i+1} 次操作...")
    perform_actions()

print("操作已完成 1000 次！")
