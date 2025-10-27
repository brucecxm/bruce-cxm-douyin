// uni-app 兼容的存储工具
// 统一处理不同平台的存储 API

/**
 * 获取存储值
 * @param {string} key 存储键
 * @param {any} defaultValue 默认值
 * @returns {any} 存储值
 */
export function getStorage(key, defaultValue = null) {
  try {
    // #ifdef H5
    if (typeof localStorage !== 'undefined') {
      const value = localStorage.getItem(key);
      return value ? JSON.parse(value) : defaultValue;
    }
    // #endif
    
    // #ifndef H5
    const value = uni.getStorageSync(key);
    return value || defaultValue;
    // #endif
  } catch (error) {
    console.error(`Failed to get storage for key ${key}:`, error);
    return defaultValue;
  }
}

/**
 * 设置存储值
 * @param {string} key 存储键
 * @param {any} value 存储值
 * @returns {boolean} 是否成功
 */
export function setStorage(key, value) {
  try {
    // #ifdef H5
    if (typeof localStorage !== 'undefined') {
      localStorage.setItem(key, JSON.stringify(value));
      return true;
    }
    // #endif
    
    // #ifndef H5
    uni.setStorageSync(key, value);
    return true;
    // #endif
  } catch (error) {
    console.error(`Failed to set storage for key ${key}:`, error);
    return false;
  }
}

/**
 * 删除存储值
 * @param {string} key 存储键
 * @returns {boolean} 是否成功
 */
export function removeStorage(key) {
  try {
    // #ifdef H5
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem(key);
      return true;
    }
    // #endif
    
    // #ifndef H5
    uni.removeStorageSync(key);
    return true;
    // #endif
  } catch (error) {
    console.error(`Failed to remove storage for key ${key}:`, error);
    return false;
  }
}

/**
 * 清空所有存储
 * @returns {boolean} 是否成功
 */
export function clearStorage() {
  try {
    // #ifdef H5
    if (typeof localStorage !== 'undefined') {
      localStorage.clear();
      return true;
    }
    // #endif
    
    // #ifndef H5
    uni.clearStorageSync();
    return true;
    // #endif
  } catch (error) {
    console.error('Failed to clear storage:', error);
    return false;
  }
}

/**
 * 异步获取存储值
 * @param {string} key 存储键
 * @param {any} defaultValue 默认值
 * @returns {Promise<any>} 存储值
 */
export function getStorageAsync(key, defaultValue = null) {
  return new Promise((resolve) => {
    try {
      // #ifdef H5
      if (typeof localStorage !== 'undefined') {
        const value = localStorage.getItem(key);
        resolve(value ? JSON.parse(value) : defaultValue);
        return;
      }
      // #endif
      
      // #ifndef H5
      uni.getStorage({
        key,
        success: (res) => resolve(res.data || defaultValue),
        fail: () => resolve(defaultValue)
      });
      // #endif
    } catch (error) {
      console.error(`Failed to get storage for key ${key}:`, error);
      resolve(defaultValue);
    }
  });
}

/**
 * 异步设置存储值
 * @param {string} key 存储键
 * @param {any} value 存储值
 * @returns {Promise<boolean>} 是否成功
 */
export function setStorageAsync(key, value) {
  return new Promise((resolve) => {
    try {
      // #ifdef H5
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(key, JSON.stringify(value));
        resolve(true);
        return;
      }
      // #endif
      
      // #ifndef H5
      uni.setStorage({
        key,
        data: value,
        success: () => resolve(true),
        fail: () => resolve(false)
      });
      // #endif
    } catch (error) {
      console.error(`Failed to set storage for key ${key}:`, error);
      resolve(false);
    }
  });
}



