// 原生交互工具类

/**
 * 设置标题栏
 * @param {string} title - 标题文本
 */
export function setTitleBar(title) {
  if (window.Android && window.Android.setTitle) {
    try {
      window.Android.setTitle(title);
    } catch (error) {
      console.error('设置标题栏失败:', error);
    }
  } else {
    // Web环境下设置document标题
    document.title = title;
  }
}

/**
 * 显示加载中提示
 * @param {string} message - 提示信息，默认"加载中..."
 */
export function showLoading(message = '加载中...') {
  if (window.Android && window.Android.showLoading) {
    try {
      window.Android.showLoading(message);
    } catch (error) {
      console.error('显示加载中提示失败:', error);
    }
  } else {
    // Web环境下可以使用其他加载提示方式
    console.log('显示加载中:', message);
  }
}

/**
 * 隐藏加载中提示
 */
export function hideLoading() {
  if (window.Android && window.Android.hideLoading) {
    try {
      window.Android.hideLoading();
    } catch (error) {
      console.error('隐藏加载中提示失败:', error);
    }
  } else {
    console.log('隐藏加载中');
  }
}

/**
 * 显示Toast提示
 * @param {string} message - 提示信息
 * @param {number} duration - 显示时长，默认1500ms
 */
export function showToast(message, duration = 1500) {
  if (window.Android && window.Android.showToast) {
    try {
      window.Android.showToast(message, duration);
    } catch (error) {
      console.error('显示Toast失败:', error);
    }
  } else {
    // Web环境下使用alert或其他提示方式
    console.log('Toast:', message);
  }
}

/**
 * 关闭当前页面
 */
export function closePage() {
  if (window.Android && window.Android.closePage) {
    try {
      window.Android.closePage();
    } catch (error) {
      console.error('关闭页面失败:', error);
    }
  } else {
    // Web环境下可以使用window.close()
    console.log('关闭页面');
  }
}

/**
 * 获取设备信息
 * @returns {Promise<Object>} 设备信息对象
 */
export function getDeviceInfo() {
  return new Promise((resolve) => {
    if (window.Android && window.Android.getDeviceInfo) {
      try {
        const deviceInfo = window.Android.getDeviceInfo();
        resolve(JSON.parse(deviceInfo));
      } catch (error) {
        console.error('获取设备信息失败:', error);
        resolve({});
      }
    } else {
      // Web环境下返回默认信息
      resolve({
        platform: 'web',
        version: navigator.userAgent
      });
    }
  });
}

/**
 * 跳转到登录页面
 */
export function navigateToLoginPage() {
  if (window.Android && window.Android.navigateToLoginPage) {
    try {
      window.Android.navigateToLoginPage();
    } catch (error) {
      console.error('跳转到登录页面失败:', error);
    }
  } else {
    console.log('跳转到登录页面');
  }
}

/**
 * 根据URL或ID跳转到原生页面
 * @param {string} target - 目标页面的URL或ID
 */
export function navigateToNativePage(target) {
  if (window.Android && window.Android.navigateToNativePage) {
    try {
      window.Android.navigateToNativePage(target);
    } catch (error) {
      console.error('跳转到原生页面失败:', error);
    }
  } else {
    console.log('跳转到原生页面:', target);
  }
}

export default {
  setTitleBar,
  showLoading,
  hideLoading,
  showToast,
  closePage,
  getDeviceInfo,
  navigateToLoginPage,
  navigateToNativePage
};
