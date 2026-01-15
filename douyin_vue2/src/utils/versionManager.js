// 版本管理模块
// MinIO 配置
const MINIO_ENDPOINT = 'http://minio.example.com';
const MINIO_BUCKET = 'frontend-assets';
const VERSION_FILE = `${MINIO_ENDPOINT}/${MINIO_BUCKET}/version.json`;
const LOCAL_VERSION_KEY = 'local_version';

// 版本比较函数
function compareVersions(v1, v2) {
  if (v1 === v2) return 0;

  const v1Parts = v1.replace('v', '').split('.').map(Number);
  const v2Parts = v2.replace('v', '').split('.').map(Number);

  for (let i = 0; i < Math.max(v1Parts.length, v2Parts.length); i++) {
    const num1 = v1Parts[i] || 0;
    const num2 = v2Parts[i] || 0;

    if (num1 > num2) return 1;
    if (num1 < num2) return -1;
  }

  return 0;
}

// 获取本地版本
function getLocalVersion() {
  return localStorage.getItem(LOCAL_VERSION_KEY) || 'v1.0.0';
}

// 检查版本更新
export async function checkForUpdates() {
  try {
    // 获取服务器版本信息
    const response = await fetch(VERSION_FILE);
    const serverVersionInfo = await response.json();

    // 获取本地版本
    const localVersion = getLocalVersion();

    // 比较版本
    const isNewVersion =
      compareVersions(serverVersionInfo.version, localVersion) > 0;

    if (isNewVersion) {
      console.log('发现新版本:', serverVersionInfo.version);
      console.log('更新内容:', serverVersionInfo.releaseNotes);

      // 通知Android客户端有新版本
      if (window.Android && window.Android.notifyNewVersion) {
        window.Android.notifyNewVersion(JSON.stringify(serverVersionInfo));
      }

      return {
        hasUpdate: true,
        versionInfo: serverVersionInfo
      };
    } else {
      console.log('当前已是最新版本:', localVersion);
      return {
        hasUpdate: false,
        versionInfo: null
      };
    }
  } catch (error) {
    console.error('检查版本更新失败:', error);
    return {
      hasUpdate: false,
      versionInfo: null
    };
  }
}

// 初始化版本检查
export function initVersionCheck() {
  console.log('初始化版本检查');
  //   // 页面加载完成后检查版本
  //   window.addEventListener('load', async () => {
  //     await checkForUpdates();
  //   });
  //
  //   // 定期检查版本（可选）
  //   setInterval(() => {
  //     checkForUpdates();
  //   }, 24 * 60 * 60 * 1000); // 每天检查一次
}

export default {
  checkForUpdates,
  initVersionCheck
};
