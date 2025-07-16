package com.bruce.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FfmpegUtils {

    // 从配置文件读取FFmpeg路径（需在服务器安装FFmpeg）
    @Value("${ffmpeg.path:/usr/local/bin/ffmpeg}") // Windows可能为"C:/ffmpeg/bin/ffmpeg.exe"
    private String ffmpegPath;

    /**
     * 将完整视频转换为HLS格式（.m3u8+.ts）
     * @param inputFilePath 输入视频路径（本地临时文件）
     * @param outputDir 输出目录（存放.m3u8和.ts）
     * @throws IOException 执行命令异常
     * @throws InterruptedException 进程中断异常
     */
    public void convertToHls(String inputFilePath, String outputDir) throws IOException, InterruptedException {
        // 创建输出目录
        File outputDirFile = new File(outputDir);
        if (!outputDirFile.exists()) {
            outputDirFile.mkdirs();
        }

        // 构建FFmpeg命令：将视频切割为10秒/片的.ts，生成index.m3u8
        ProcessBuilder processBuilder = new ProcessBuilder(
                ffmpegPath,
                "-i", inputFilePath,          // 输入文件
                "-c:v", "h264",               // 视频编码（HLS标准）
                "-c:a", "aac",                // 音频编码
                "-hls_time", "10",            // 每个.ts片段时长（秒）
                "-hls_list_size", "0",        // 0表示包含所有片段（非直播场景）
                "-hls_segment_filename", outputDir + "/%03d.ts", // ts片段命名（001.ts,002.ts...）
                outputDir + "/index.m3u8"     // 生成的m3u8索引文件
        );

        // 重定向错误输出到标准输出
        processBuilder.redirectErrorStream(true);

        // 执行命令
        Process process = processBuilder.start();

        // 异步读取输出流，防止阻塞
        Thread outputReader = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 打印输出信息，可根据需要改为日志记录
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        outputReader.start();

        // 等待进程完成
        int exitCode = process.waitFor();

        // 等待输出读取线程完成
        outputReader.join();

        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg转换失败，退出码：" + exitCode);
        }
    }


    public int getVideoDuration(String videoPath) throws Exception {
        // 使用Path类处理路径，自动适应不同操作系统的路径分隔符
        Path path = Paths.get(videoPath);
        String formattedPath = path.toString();

        // 构建FFprobe命令，使用配置的FFmpeg路径作为前缀
        List<String> commands = new ArrayList<>();

        // 获取FFmpeg所在目录作为FFprobe的路径前缀
        Path ffmpegFilePath = Paths.get(ffmpegPath);
        Path ffmpegDir = ffmpegFilePath.getParent();

        // 构建FFprobe命令，根据操作系统决定是否添加.exe后缀
        String ffprobeExecutable = "ffprobe";
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            ffprobeExecutable += ".exe";
        }

        // 如果配置了FFmpeg路径，则在执行ffprobe时使用相同目录
        if (ffmpegDir != null) {
            Path ffprobePath = ffmpegDir.resolve(ffprobeExecutable);
            commands.add(ffprobePath.toString());
        } else {
            // 如果无法获取目录，则直接使用命令名（依赖PATH环境变量）
            commands.add(ffprobeExecutable);
        }

        // 添加ffprobe命令参数
        commands.add("-v");
        commands.add("error");
        commands.add("-show_entries");
        commands.add("format=duration");
        commands.add("-of");
        commands.add("default=noprint_wrappers=1:nokey=1");
        commands.add(formattedPath); // 使用处理后的路径

        ProcessBuilder pb = new ProcessBuilder(commands);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String durationStr = reader.readLine();
            if (durationStr != null) {
                return (int) Math.round(Double.parseDouble(durationStr));
            }
        }

        // 读取错误流，记录可能的错误信息
        try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            StringBuilder errorMessage = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorMessage.append(line).append("\n");
            }
            if (errorMessage.length() > 0) {
                System.err.println("FFprobe错误信息: " + errorMessage);
                // 额外日志：输出完整的命令行，便于调试
                System.err.println("执行的命令: " + String.join(" ", commands));
            }
        }

        return 0; // 异常时返回0
    }
}