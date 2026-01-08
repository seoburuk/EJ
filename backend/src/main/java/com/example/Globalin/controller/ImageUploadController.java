package com.example.Globalin.controller;

import com.example.Globalin.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, allowCredentials = "true")
public class ImageUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, HttpSession session) {
        try {
            // 로그인 체크
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("message", "로그인이 필요합니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            // 파일 업로드
            String imageUrl = fileUploadService.uploadFile(file);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("imageUrl", imageUrl);
            response.put("originalFilename", file.getOriginalFilename());
            response.put("fileSize", file.getSize());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "파일 업로드 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
