# 前端资源版本管理与自动更新方案

## 1. 方案设计

### 1.1 核心需求

- **版本管理**：通过文件名/版本号区分不同版本的资源
- **版本检查**：启动时检查服务器上的版本是否最新
- **后台更新**：如果有新版本，后台下载并更新
- **平滑切换**：下次启动时使用最新版本
- **回滚机制**：保留旧版本，以便出现问题时回滚

### 1.2 实现思路

#### 1.2.1 服务器端

- 维护一个 `version.json` 文件，包含最新版本信息
- 存储不同版本的资源压缩包

#### 1.2.2 客户端

- 本地维护两个资源目录：
  - `current`：当前正在使用的版本
  - `next`：下次要使用的新版本
  - `backup`：备份目录（可选）
- 启动时检查服务器版本
- 如果有新版本，下载到 `next` 目录
- 下次启动时，将 `next` 目录设为 `current`

### 1.3 版本号格式

使用语义化版本号：`v{major}.{minor}.{patch}`，例如 `v1.0.0`

## 2. 服务器端实现（MinIO 版本）

### 2.1 MinIO 存储结构

```
bucket: frontend-assets
├── version.json          # 最新版本信息
├── v1.0.0.zip           # 版本1.0.0的资源包
├── v1.0.1.zip           # 版本1.0.1的资源包
└── v1.1.0.zip           # 版本1.1.0的资源包
```

### 2.2 version.json 格式

```json
{
  "version": "v1.1.0",
  "url": "http://minio.example.com/frontend-assets/v1.1.0.zip",
  "checksum": "a1b2c3d4e5f6...", // 可选，用于验证文件完整性
  "releaseNotes": "修复了页面跳转bug，优化了加载性能"
}
```

### 2.3 MinIO 配置

- **Endpoint**: http://minio.example.com
- **Bucket**: frontend-assets
- **Access Key**: your-access-key
- **Secret Key**: your-secret-key

### 2.4 MinIO 客户端依赖

```gradle
dependencies {
    implementation 'io.minio:minio:8.5.2'
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
}
```

## 3. 客户端实现

### 3.1 本地目录结构

```
├── assets/
│   ├── current/         # 当前使用的版本
│   ├── next/            # 待切换的新版本
│   └── backup/          # 备份目录（可选）
└── version_info.json     # 本地版本信息
```

### 3.2 核心流程

1. **启动检查**：

   - 读取本地 `version_info.json`，获取当前版本
   - 请求服务器 `version.json`，获取最新版本
   - 比较版本号

2. **版本更新**：

   - 如果服务器版本更新，下载压缩包到临时目录
   - 解压到 `next` 目录
   - 验证文件完整性
   - 更新本地 `version_info.json`

3. **版本切换**：

   - 下次启动时，检查 `next` 目录是否存在新版本
   - 如果存在，将 `current` 目录备份到 `backup`
   - 将 `next` 目录重命名为 `current`
   - 清空 `next` 目录

4. **回滚机制**：
   - 如果新版本出现问题，手动切换回 `backup` 目录
   - 或者自动检测崩溃，回滚到上一个版本

### 3.3 本地 version_info.json 格式

```json
{
  "currentVersion": "v1.0.1",
  "nextVersion": "v1.1.0",
  "lastCheckTime": 1634567890000,
  "isUpdateAvailable": true
}
```

## 4. 具体实现代码

### 4.1 Android 版本检查与更新代码

```java
public class VersionManager {
    private static final String TAG = "VersionManager";
    private static final String SERVER_VERSION_URL = "http://example.com/version.json";
    private static final String LOCAL_VERSION_FILE = "version_info.json";
    private static final String ASSETS_DIR = "assets";
    private static final String CURRENT_DIR = "current";
    private static final String NEXT_DIR = "next";
    private static final String BACKUP_DIR = "backup";

    private Context context;
    private OkHttpClient okHttpClient;

    public VersionManager(Context context) {
        this.context = context;
        this.okHttpClient = new OkHttpClient();
    }

    /**
     * 检查版本更新
     */
    public void checkForUpdates() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 获取服务器版本信息
                    ServerVersionInfo serverVersionInfo = getServerVersionInfo();
                    if (serverVersionInfo == null) {
                        Log.e(TAG, "Failed to get server version info");
                        return;
                    }

                    // 获取本地版本信息
                    LocalVersionInfo localVersionInfo = getLocalVersionInfo();

                    // 比较版本号
                    if (isNewVersion(serverVersionInfo.getVersion(), localVersionInfo.getCurrentVersion())) {
                        Log.d(TAG, "New version available: " + serverVersionInfo.getVersion());
                        // 下载并更新
                        downloadAndUpdate(serverVersionInfo);
                    } else {
                        Log.d(TAG, "No new version available");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error checking for updates", e);
                }
            }
        }).start();
    }

    /**
     * 获取服务器版本信息
     */
    private ServerVersionInfo getServerVersionInfo() throws IOException {
        Request request = new Request.Builder()
                .url(SERVER_VERSION_URL)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            String json = response.body().string();
            return new Gson().fromJson(json, ServerVersionInfo.class);
        }
        return null;
    }

    /**
     * 获取本地版本信息
     */
    private LocalVersionInfo getLocalVersionInfo() {
        File versionFile = new File(context.getFilesDir(), LOCAL_VERSION_FILE);
        if (versionFile.exists()) {
            try {
                String json = new String(Files.readAllBytes(versionFile.toPath()));
                return new Gson().fromJson(json, LocalVersionInfo.class);
            } catch (IOException e) {
                Log.e(TAG, "Error reading local version info", e);
            }
        }
        // 默认版本
        return new LocalVersionInfo("v1.0.0", "", System.currentTimeMillis(), false);
    }

    /**
     * 比较版本号
     */
    private boolean isNewVersion(String serverVersion, String localVersion) {
        // 简单实现，实际应使用语义化版本号比较
        return !serverVersion.equals(localVersion);
    }

    /**
     * 下载并更新资源
     */
    private void downloadAndUpdate(ServerVersionInfo serverVersionInfo) {
        try {
            // 下载压缩包
            File tempZip = downloadZip(serverVersionInfo.getUrl());
            if (tempZip == null) {
                return;
            }

            // 解压到next目录
            File nextDir = new File(context.getFilesDir(), ASSETS_DIR + File.separator + NEXT_DIR);
            unzip(tempZip, nextDir);

            // 更新本地版本信息
            LocalVersionInfo localVersionInfo = getLocalVersionInfo();
            localVersionInfo.setNextVersion(serverVersionInfo.getVersion());
            localVersionInfo.setLastCheckTime(System.currentTimeMillis());
            localVersionInfo.setUpdateAvailable(true);
            saveLocalVersionInfo(localVersionInfo);

            Log.d(TAG, "Update downloaded successfully, will be applied on next launch");
        } catch (Exception e) {
            Log.e(TAG, "Error downloading and updating", e);
        }
    }

    // 其他辅助方法：downloadZip, unzip, saveLocalVersionInfo等
}
```

### 4.2 版本切换代码

```java
/**
 * 在Application的onCreate方法中调用，切换版本
 */
public void switchVersion() {
    try {
        LocalVersionInfo localVersionInfo = getLocalVersionInfo();
        if (localVersionInfo.isUpdateAvailable() && !localVersionInfo.getNextVersion().isEmpty()) {
            File assetsDir = new File(context.getFilesDir(), ASSETS_DIR);
            File currentDir = new File(assetsDir, CURRENT_DIR);
            File nextDir = new File(assetsDir, NEXT_DIR);
            File backupDir = new File(assetsDir, BACKUP_DIR);

            // 删除旧的备份
            if (backupDir.exists()) {
                deleteDir(backupDir);
            }

            // 备份当前版本
            if (currentDir.exists()) {
                currentDir.renameTo(backupDir);
            }

            // 将next目录设为current
            if (nextDir.exists()) {
                nextDir.renameTo(currentDir);
            }

            // 创建新的next目录
            nextDir.mkdirs();

            // 更新本地版本信息
            localVersionInfo.setCurrentVersion(localVersionInfo.getNextVersion());
            localVersionInfo.setNextVersion("");
            localVersionInfo.setUpdateAvailable(false);
            saveLocalVersionInfo(localVersionInfo);

            Log.d(TAG, "Version switched to: " + localVersionInfo.getCurrentVersion());
        }
    } catch (Exception e) {
        Log.e(TAG, "Error switching version", e);
    }
}
```

## 5. 优化建议

1. **使用增量更新**：只下载变化的文件，减少下载量
2. **使用 MD5 校验**：确保文件完整性
3. **添加下载进度监听**：显示更新进度
4. **支持手动检查更新**：允许用户手动触发更新检查
5. **添加更新日志**：显示新版本的更新内容
6. **实现回滚机制**：如果新版本出现问题，自动回滚到旧版本
7. **使用 CDN 加速**：加速资源下载

## 6. 总结

这个方案实现了完整的前端资源版本管理和自动更新机制，具有以下优点：

1. **平滑更新**：后台下载，下次启动生效，不影响当前使用
2. **回滚安全**：保留旧版本，出现问题可回滚
3. **版本清晰**：使用语义化版本号，便于管理
4. **实现简单**：核心逻辑清晰，易于实现
5. **扩展性强**：可扩展为增量更新、CDN 加速等

用户可以根据实际需求选择是否使用增量更新、MD5 校验等高级功能。
