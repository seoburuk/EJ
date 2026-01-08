package com.example.Globalin.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailVerificationService {

    // 이메일 -> 인증 코드 매핑 (실제로는 Redis 등 사용 권장)
    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();

    private static final int CODE_LENGTH = 6;
    private static final long CODE_EXPIRY_MS = 5 * 60 * 1000; // 5분

    // 認証コード生成および保存
    public String generateVerificationCode(String email) {
        String code = generateRandomCode();
        long expiryTime = System.currentTimeMillis() + CODE_EXPIRY_MS;

        verificationCodes.put(email, new VerificationCode(code, expiryTime));

        // 実際にはメール送信サービスを使用
        System.out.println("=== メール認証コード ===");
        System.out.println("宛先: " + email);
        System.out.println("認証コード: " + code);
        System.out.println("有効時間: 5分");
        System.out.println("====================");

        return code;
    }

    // 認証コード検証
    public boolean verifyCode(String email, String code) {
        VerificationCode storedCode = verificationCodes.get(email);

        if (storedCode == null) {
            return false;
        }

        // 有効期限確認
        if (System.currentTimeMillis() > storedCode.expiryTime) {
            verificationCodes.remove(email);
            return false;
        }

        // コード一致確認
        if (storedCode.code.equals(code)) {
            verificationCodes.remove(email); // 使用済みコードを削除
            return true;
        }

        return false;
    }

    // ランダム6桁数字コード生成
    private String generateRandomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    // 認証コード情報を格納する内部クラス
    private static class VerificationCode {
        String code;
        long expiryTime;

        VerificationCode(String code, long expiryTime) {
            this.code = code;
            this.expiryTime = expiryTime;
        }
    }
}
