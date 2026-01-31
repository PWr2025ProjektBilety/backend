package com.example.backend.purchasedticket;

import com.example.backend.service.purchasedTicket.VehicleTicketValidationLockService;
import com.example.backend.model.purchasedTicket.VehicleTicketValidationLock;
import com.example.backend.repository.purchasedTicket.VehicleTicketValidationLockRepository;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class VehicleTicketValidationLockServiceTest {

    private static VehicleTicketValidationLockService createServiceWithInMemoryRepo(Clock clock) {
        VehicleTicketValidationLockRepository repo = mock(VehicleTicketValidationLockRepository.class);
        Map<String, VehicleTicketValidationLock> store = new ConcurrentHashMap<>();

        when(repo.save(any(VehicleTicketValidationLock.class))).thenAnswer((Answer<VehicleTicketValidationLock>) invocation -> {
            VehicleTicketValidationLock lock = invocation.getArgument(0);
            store.put(lock.getVehicleId(), lock);
            return lock;
        });

        when(repo.findById(anyString())).thenAnswer((Answer<Optional<VehicleTicketValidationLock>>) invocation -> {
            String id = invocation.getArgument(0);
            return Optional.ofNullable(store.get(id));
        });

        doAnswer(invocation -> {
            String id = invocation.getArgument(0);
            store.remove(id);
            return null;
        }).when(repo).deleteById(anyString());

        return new VehicleTicketValidationLockService(clock, repo);
    }

    @Test
    void lock_shouldClampDurationToMinimum() {
        Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        VehicleTicketValidationLockService service = createServiceWithInMemoryRepo(clock);

        Instant until = service.lock("BUS1", Duration.ofMinutes(1));
        assertEquals(Instant.parse("2026-01-01T00:15:00Z"), until);
        assertTrue(service.isLocked("BUS1"));
    }

    @Test
    void lock_shouldClampDurationToMaximum() {
        Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        VehicleTicketValidationLockService service = createServiceWithInMemoryRepo(clock);

        Instant until = service.lock("BUS1", Duration.ofMinutes(999));
        assertEquals(Instant.parse("2026-01-01T00:30:00Z"), until);
        assertTrue(service.isLocked("BUS1"));
    }

    @Test
    void lock_shouldUseDefaultDurationWhenNull() {
        Clock clock = Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        VehicleTicketValidationLockService service = createServiceWithInMemoryRepo(clock);

        Instant until = service.lock("BUS1", null);
        assertEquals(Instant.parse("2026-01-01T00:20:00Z"), until);
        assertTrue(service.isLocked("BUS1"));
    }

    @Test
    void expiry_shouldBeDetectedWithMutableClock() {
        MutableClock clock = new MutableClock(Instant.parse("2026-01-01T00:00:00Z"));
        VehicleTicketValidationLockService service = createServiceWithInMemoryRepo(clock);

        service.lock("BUS1", Duration.ofMinutes(15));
        assertTrue(service.isLocked("BUS1"));

        clock.advance(Duration.ofMinutes(16));
        assertNull(service.getLockedUntil("BUS1"));
        assertFalse(service.isLocked("BUS1"));
    }

    @Test
    void unlock_shouldRemoveLock() {
        MutableClock clock = new MutableClock(Instant.parse("2026-01-01T00:00:00Z"));
        VehicleTicketValidationLockService service = createServiceWithInMemoryRepo(clock);

        service.lock("BUS1", Duration.ofMinutes(15));
        assertTrue(service.isLocked("BUS1"));

        service.unlock("BUS1");
        assertFalse(service.isLocked("BUS1"));
        assertNull(service.getLockedUntil("BUS1"));
    }

    private static final class MutableClock extends Clock {
        private Instant now;

        private MutableClock(Instant initial) {
            this.now = initial;
        }

        @Override
        public ZoneOffset getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now;
        }

        public void advance(Duration duration) {
            now = now.plus(duration);
        }
    }
}
