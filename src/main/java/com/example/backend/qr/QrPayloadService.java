package com.example.backend.qr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Service
public class QrPayloadService {

    private static final String HMAC_ALG = "HmacSHA256";
    private static final String VERSION = "v1";

    private final Base64.Encoder base64UrlEncoder = Base64.getUrlEncoder().withoutPadding();
    private final Base64.Decoder base64UrlDecoder = Base64.getUrlDecoder();

    private final String secret;

    public QrPayloadService(@Value("${app.qr.secret:}") String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Missing required configuration: app.qr.secret");
        }
        this.secret = secret;
    }

    public String createPayload(String ticketCode) {
        if (ticketCode == null || ticketCode.isBlank()) {
            throw new IllegalArgumentException("ticketCode must not be blank");
        }

        long issuedAt = Instant.now().getEpochSecond();
        String unsigned = VERSION + "." + ticketCode + "." + issuedAt;
        String sig = sign(unsigned);
        return unsigned + "." + sig;
    }

    public String extractTicketCodeOrNull(String payload) {
        if (payload == null) return null;
        String trimmed = payload.trim();
        if (trimmed.isEmpty()) return null;

        String[] parts = trimmed.split("\\.");
        if (parts.length != 4) return null;

        String version = parts[0];
        if (!VERSION.equals(version)) return null;

        String ticketCode = parts[1];
        String issuedAt = parts[2];
        String sig = parts[3];

        String unsigned = version + "." + ticketCode + "." + issuedAt;
        String expectedSig = sign(unsigned);

        if (!constantTimeEquals(sig, expectedSig)) return null;

        return ticketCode;
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALG);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALG));
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64UrlEncoder.encodeToString(raw);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to sign QR payload", e);
        }
    }

    private boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] aBytes;
        byte[] bBytes;
        try {
            aBytes = base64UrlDecoder.decode(a);
            bBytes = base64UrlDecoder.decode(b);
        } catch (IllegalArgumentException e) {
            return false;
        }

        if (aBytes.length != bBytes.length) return false;

        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }
        return result == 0;
    }
}
