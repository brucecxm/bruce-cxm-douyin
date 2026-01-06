package com.example.myapplication;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 版本管理类，用于检查和更新前端资源版本
 */
public class VersionManager {
    private static final String TAG = "VersionManager";
    
    // 本地目录常量
    private static final String ASSETS_DIR = "assets";
    private static final String CURRENT_DIR = "current";
    private static final String NEXT_DIR = "next";
    private static final String BACKUP_DIR = "backup";
    private static final String VERSION_INFO_FILE = "version_info.json";
    
    private Context context;
    private Gson gson;
    
    public VersionManager(Context context) {
        this.context = context;
        this.gson = new Gson();
        
        // 初始化MinIO客户端
        MinioUtil.init();
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
                        Log.e(TAG, "获取服务器版本信息失败");
                        return;
                    }
                    
                    // 获取本地版本信息
                    LocalVersionInfo localVersionInfo = getLocalVersionInfo();
                    
                    // 比较版本
                    if (isNewVersion(serverVersionInfo.getVersion(), localVersionInfo.getCurrentVersion())) {
                        Log.d(TAG, "发现新版本: " + serverVersionInfo.getVersion());
                        Log.d(TAG, "更新内容: " + serverVersionInfo.getReleaseNotes());
                        
                        // 下载并更新资源
                        downloadAndUpdate(serverVersionInfo);
                    } else {
                        Log.d(TAG, "当前已是最新版本: " + localVersionInfo.getCurrentVersion());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "检查版本更新失败", e);
                }
            }
        }).start();
    }
    
    /**
     * 获取服务器版本信息
     */
    private ServerVersionInfo getServerVersionInfo() {
        try {
            // 从MinIO获取版本信息文件
            InputStream inputStream = MinioUtil.getVersionInfo();
            if (inputStream == null) {
                return null;
            }
            
            // 读取并解析JSON
            String json = readInputStream(inputStream);
            inputStream.close();
            
            return gson.fromJson(json, ServerVersionInfo.class);
        } catch (Exception e) {
            Log.e(TAG, "解析服务器版本信息失败", e);
            return null;
        }
    }
    
    /**
     * 获取本地版本信息
     */
    private LocalVersionInfo getLocalVersionInfo() {
        File versionFile = new File(context.getFilesDir(), VERSION_INFO_FILE);
        if (versionFile.exists()) {
            try {
                InputStream inputStream = context.openFileInput(VERSION_INFO_FILE);
                String json = readInputStream(inputStream);
                inputStream.close();
                
                return gson.fromJson(json, LocalVersionInfo.class);
            } catch (Exception e) {
                Log.e(TAG, "读取本地版本信息失败", e);
            }
        }
        
        // 默认版本信息
        return new LocalVersionInfo("v1.0.0", "", System.currentTimeMillis(), false);
    }
    
    /**
     * 保存本地版本信息
     */
    private void saveLocalVersionInfo(LocalVersionInfo versionInfo) {
        try {
            String json = gson.toJson(versionInfo);
            FileOutputStream outputStream = context.openFileOutput(VERSION_INFO_FILE, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();
            Log.d(TAG, "本地版本信息保存成功");
        } catch (Exception e) {
            Log.e(TAG, "保存本地版本信息失败", e);
        }
    }
    
    /**
     * 下载并更新资源
     */
    private void downloadAndUpdate(ServerVersionInfo serverVersionInfo) {
        try {
            Log.d(TAG, "开始下载新版本: " + serverVersionInfo.getVersion());
            
            // 从MinIO下载版本包
            InputStream inputStream = MinioUtil.downloadVersionPackage(serverVersionInfo.getVersion());
            if (inputStream == null) {
                Log.e(TAG, "下载版本包失败");
                return;
            }
            
            // 获取Next目录
            File nextDir = new File(context.getFilesDir(), ASSETS_DIR + File.separator + NEXT_DIR);
            if (!nextDir.exists()) {
                nextDir.mkdirs();
            } else {
                // 清空Next目录
                deleteDir(nextDir);
                nextDir.mkdirs();
            }
            
            // 解压版本包到Next目录
            unzip(inputStream, nextDir);
            inputStream.close();
            
            Log.d(TAG, "版本包解压成功，保存到Next目录");
            
            // 更新本地版本信息
            LocalVersionInfo localVersionInfo = getLocalVersionInfo();
            localVersionInfo.setNextVersion(serverVersionInfo.getVersion());
            localVersionInfo.setLastCheckTime(System.currentTimeMillis());
            localVersionInfo.setUpdateAvailable(true);
            saveLocalVersionInfo(localVersionInfo);
            
            Log.d(TAG, "版本更新准备完成，下次启动将使用新版本");
        } catch (Exception e) {
            Log.e(TAG, "下载并更新资源失败", e);
        }
    }
    
    /**
     * 在应用启动时调用，切换版本
     */
    public void switchVersion() {
        try {
            LocalVersionInfo localVersionInfo = getLocalVersionInfo();
            if (!localVersionInfo.isUpdateAvailable() || localVersionInfo.getNextVersion().isEmpty()) {
                Log.d(TAG, "没有待更新的版本");
                return;
            }
            
            Log.d(TAG, "开始切换版本，从 " + localVersionInfo.getCurrentVersion() + " 到 " + localVersionInfo.getNextVersion());
            
            // 获取目录对象
            File assetsDir = new File(context.getFilesDir(), ASSETS_DIR);
            File currentDir = new File(assetsDir, CURRENT_DIR);
            File nextDir = new File(assetsDir, NEXT_DIR);
            File backupDir = new File(assetsDir, BACKUP_DIR);
            
            // 创建必要的目录
            if (!assetsDir.exists()) {
                assetsDir.mkdirs();
            }
            
            // 删除旧的备份
            if (backupDir.exists()) {
                deleteDir(backupDir);
            }
            
            // 备份当前版本
            if (currentDir.exists()) {
                currentDir.renameTo(backupDir);
            }
            
            // 将next目录重命名为current
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
            
            Log.d(TAG, "版本切换完成，当前版本: " + localVersionInfo.getCurrentVersion());
        } catch (Exception e) {
            Log.e(TAG, "切换版本失败", e);
        }
    }
    
    /**
     * 读取输入流内容
     */
    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
    
    /**
     * 解压Zip文件
     */
    private void unzip(InputStream inputStream, File targetDir) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        ZipEntry entry;
        
        while ((entry = zipInputStream.getNextEntry()) != null) {
            String entryName = entry.getName();
            File entryFile = new File(targetDir, entryName);
            
            if (entry.isDirectory()) {
                entryFile.mkdirs();
            } else {
                // 确保父目录存在
                File parentDir = entryFile.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                
                // 写入文件
                OutputStream outputStream = new FileOutputStream(entryFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zipInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
            }
            
            zipInputStream.closeEntry();
        }
        
        zipInputStream.close();
    }
    
    /**
     * 删除目录及其内容
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File child : children) {
                    deleteDir(child);
                }
            }
        }
        return dir.delete();
    }
    
    /**
     * 服务器版本信息类
     */
    private static class ServerVersionInfo {
        private String version;
        private String url;
        private String checksum;
        private String releaseNotes;
        
        // Getters and setters
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getChecksum() { return checksum; }
        public void setChecksum(String checksum) { this.checksum = checksum; }
        public String getReleaseNotes() { return releaseNotes; }
        public void setReleaseNotes(String releaseNotes) { this.releaseNotes = releaseNotes; }
    }
    
    /**
     * 本地版本信息类
     */
    private static class LocalVersionInfo {
        private String currentVersion;
        private String nextVersion;
        private long lastCheckTime;
        private boolean isUpdateAvailable;
        
        // 构造函数
        public LocalVersionInfo(String currentVersion, String nextVersion, long lastCheckTime, boolean isUpdateAvailable) {
            this.currentVersion = currentVersion;
            this.nextVersion = nextVersion;
            this.lastCheckTime = lastCheckTime;
            this.isUpdateAvailable = isUpdateAvailable;
        }
        
        // Getters and setters
        public String getCurrentVersion() { return currentVersion; }
        public void setCurrentVersion(String currentVersion) { this.currentVersion = currentVersion; }
        public String getNextVersion() { return nextVersion; }
        public void setNextVersion(String nextVersion) { this.nextVersion = nextVersion; }
        public long getLastCheckTime() { return lastCheckTime; }
        public void setLastCheckTime(long lastCheckTime) { this.lastCheckTime = lastCheckTime; }
        public boolean isUpdateAvailable() { return isUpdateAvailable; }
        public void setUpdateAvailable(boolean updateAvailable) { isUpdateAvailable = updateAvailable; }
    }
    
    /**
     * 版本比较方法
     */
    private boolean isNewVersion(String serverVersion, String localVersion) {
        if (serverVersion == null || localVersion == null) {
            return false;
        }
        
        // 移除版本号中的"v"前缀
        String[] serverParts = serverVersion.replace("v", "").split("\\.");
        String[] localParts = localVersion.replace("v", "").split("\\.");
        
        int maxLength = Math.max(serverParts.length, localParts.length);
        for (int i = 0; i < maxLength; i++) {
            int serverNum = i < serverParts.length ? Integer.parseInt(serverParts[i]) : 0;
            int localNum = i < localParts.length ? Integer.parseInt(localParts[i]) : 0;
            
            if (serverNum > localNum) {
                return true;
            } else if (serverNum < localNum) {
                return false;
            }
        }
        
        return false;
    }
}