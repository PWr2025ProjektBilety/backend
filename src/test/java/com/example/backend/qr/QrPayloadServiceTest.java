package com.example.backend.qr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QrPayloadServiceTest {

    @Test
    void shouldCreateAndVerifyPayload() {
        QrPayloadService service = new QrPayloadService("unit-test-secret");

        String payload = service.createPayload("CODE123");
        assertNotNull(payload);

        String extracted = service.extractTicketCodeOrNull(payload);
        assertEquals("CODE123", extracted);
    }

    @Test
    void shouldReturnNullForInvalidPayload() {
        QrPayloadService service = new QrPayloadService("unit-test-secret");

        String extracted = service.extractTicketCodeOrNull("v1.CODE123.123456.bad");
        assertNull(extracted);
    }
}
