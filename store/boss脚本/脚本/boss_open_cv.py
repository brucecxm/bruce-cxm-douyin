import subprocess
import cv2
import pyautogui
import numpy as np

# 使用 adb 截图并保存为 screen.png
adb_command = "adb exec-out screencap -p"
with open("screen.png", "wb") as f:
    # 执行 adb 命令并将输出写入文件
    process = subprocess.Popen(adb_command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
    stdout, stderr = process.communicate()
    f.write(stdout)

# 加载屏幕截图和目标模板图像
screen_image = cv2.imread('screen.png')
template = cv2.imread('button.png')  # 目标图标

# 进行模板匹配
result = cv2.matchTemplate(screen_image, template, cv2.TM_CCOEFF_NORMED)
min_val, max_val, min_loc, max_loc = cv2.minMaxLoc(result)

# 获取目标位置
top_left = max_loc
h, w = template.shape[:2]
bottom_right = (top_left[0] + w, top_left[1] + h)

# 画出匹配结果（可选）
cv2.rectangle(screen_image, top_left, bottom_right, (0, 255, 0), 2)
cv2.imwrite('result.png', screen_image)

# 获取点击位置的坐标
click_x = top_left[0] + w // 2
click_y = top_left[1] + h // 2

# 使用 adb 模拟点击
subprocess.run(["adb", "shell", "input", "tap", str(click_x), str(click_y)])

print(f"已模拟点击位置：({click_x}, {click_y})")
