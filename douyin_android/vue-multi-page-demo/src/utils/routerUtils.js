// 页面跳转工具类

// 新添加的页面路径列表
const newShopPages = [
  '/index',
  '/page1',
  '/page2',
  '/login',
  '/register',
  '/categories',
  '/address',
  '/detail',
  '/publish',
  '/profile',
  '/search',
  '/orders',
  '/orderDetail',
  '/favorites',
  '/messages',
  '/chat',
  '/myProducts',
  '/settings',
  '/cart',
  '/websocket-test'
];

/**
 * 跳转到指定页面
 * @param {string} url - 目标页面URL
 * @param {Object} params - 跳转参数
 * @param {Object} options - 跳转选项
 */
export function navigateTo(url, params = {}, options = {}) {
  const { replace = false, target = '_self', native = false } = options;

  // 移除.html后缀，适配Vue Router路由
  const routerUrl = url.replace(/\.html$/, '');

  // 检查是否需要添加/newshop前缀
  let finalUrl = routerUrl;
  const pathWithoutParams = routerUrl.split('?')[0];
  if (newShopPages.includes(pathWithoutParams)) {
    // 添加/newshop前缀
    finalUrl = `/newshop${routerUrl}`;
  }

  // 构建完整URL
  const fullUrl = buildUrl(finalUrl, params);

  // 尝试使用Vue Router进行单页跳转
  if (window.Vue && window.Vue.prototype && window.Vue.prototype.$router) {
    const router = window.Vue.prototype.$router;
    console.log('使用Vue Router跳转:', fullUrl);
    if (replace) {
      router.replace(fullUrl);
    } else {
      router.push(fullUrl);
    }
    return;
  }

  if (native && window.Android && window.Android.navigateTo) {
    // 调用原生跳转
    try {
      window.Android.navigateTo(fullUrl, JSON.stringify(params));
      return;
    } catch (error) {
      console.error('原生跳转失败，使用Web跳转:', error);
    }
  }
  
  // Web环境下的跳转
  if (target === '_self') {
    // 在当前窗口跳转，优先使用location.href，这在WebView中更可靠
    console.log('使用window.location.href跳转:', fullUrl);
    if (replace) {
      window.location.replace(fullUrl);
    } else {
      window.location.href = fullUrl;
    }
  } else {
    // 打开新窗口
    window.open(fullUrl, target);
  }
}

/**
 * 跳转到首页
 * @param {Object} params - 跳转参数
 * @param {Object} options - 跳转选项
 */
export function navigateToHome(params = {}, options = {}) {
  navigateTo('/index.html', params, options);
}

/**
 * 跳转到指定页面并关闭当前页面
 * @param {string} url - 目标页面URL
 * @param {Object} params - 跳转参数
 * @param {Object} options - 跳转选项
 */
export function redirectTo(url, params = {}, options = {}) {
  navigateTo(url, params, { ...options, replace: true });
}

/**
 * 返回上一页
 * @param {number} delta - 返回的层级，默认1
 */
export function navigateBack(delta = 1) {
  if (window.Android && window.Android.navigateBack) {
    try {
      window.Android.navigateBack(delta);
      return;
    } catch (error) {
      console.error('原生返回失败，使用Web返回:', error);
    }
  }
  
  // Web环境下的返回
  window.history.go(-delta);
}

/**
 * 重新加载当前页面
 */
export function reloadPage() {
  window.location.reload();
}

/**
 * 构建带参数的URL
 * @param {string} url - 基础URL
 * @param {Object} params - 参数对象
 * @returns {string} 完整URL
 */
export function buildUrl(url, params = {}) {
  if (!params || Object.keys(params).length === 0) {
    return url;
  }
  
  const urlObj = new URL(url, window.location.origin);
  
  Object.entries(params).forEach(([key, value]) => {
    if (value !== null && value !== undefined) {
      urlObj.searchParams.append(key, value);
    }
  });
  
  // 保留相对路径
  if (url.startsWith('/')) {
    return urlObj.pathname + urlObj.search;
  }
  
  return urlObj.href;
}

/**
 * 获取URL参数
 * @param {string} url - 可选，指定URL，默认当前页面URL
 * @returns {Object} 参数对象
 */
export function getUrlParams(url) {
  const urlObj = new URL(url || window.location.href);
  const params = {};
  
  urlObj.searchParams.forEach((value, key) => {
    params[key] = value;
  });
  
  return params;
}

export default {
  navigateTo,
  navigateToHome,
  redirectTo,
  navigateBack,
  reloadPage,
  buildUrl,
  getUrlParams
};
