package com.example.Globalin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${upload.directory:/Users/yunsu-in/Downloads/Globalin/uploads}")
    private String uploadDirectory;

    @Value("${upload.max.file.size:10485760}")
    private long maxFileSize;

    @Value("${upload.allowed.extensions:jpg,jpeg,png,gif,webp}")
    private String allowedExtensions;

    public String uploadFile(MultipartFile file) throws IOException {
        // ファイルバリデーション
        if (file.isEmpty()) {
            throw new IllegalArgumentException("アップロードするファイルがありません");
        }

        // ファイルサイズバリデーション
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("ファイルサイズが大きすぎます。最大 " + (maxFileSize / 1024 / 1024) + "MBまでアップロード可能です");
        }

        // ファイル拡張子バリデーション
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        if (!isAllowedExtension(extension)) {
            throw new IllegalArgumentException("許可されていないファイル形式です。許可形式: " + allowedExtensions);
        }

        // アップロードディレクトリ作成
        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // ユニークなファイル名生成
        String uniqueFilename = UUID.randomUUID().toString() + "." + extension;
        Path filePath = Paths.get(uploadDirectory, uniqueFilename);

        // ファイル保存
        Files.write(filePath, file.getBytes());

        // ファイルURL返却（相対パス）
        return "/uploads/" + uniqueFilename;
    }

    public void deleteFile(String fileUrl) {
        try {
            // /uploads/filename.jpg形式からファイル名のみ抽出
            String filename = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(uploadDirectory, filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // ログのみ出力し、例外は無視（ファイルが既に削除されている可能性がある）
            System.err.println("ファイル削除失敗: " + fileUrl + ", " + e.getMessage());
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String extension) {
        List<String> allowed = Arrays.asList(allowedExtensions.split(","));
        return allowed.contains(extension.toLowerCase());
    }
}
