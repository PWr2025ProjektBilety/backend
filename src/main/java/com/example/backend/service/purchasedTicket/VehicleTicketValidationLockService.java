package com.example.backend.service.purchasedTicket;

import com.example.backend.model.purchasedTicket.VehicleTicketValidationLock;
import com.example.backend.repository.purchasedTicket.VehicleTicketValidationLockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Service
public class VehicleTicketValidationLockService {

    private static final Duration DEFAULT_LOCK_DURATION = Duration.ofMinutes(20);
    private static final Duration MIN_LOCK_DURATION = Duration.ofMinutes(15);
    private static final Duration MAX_LOCK_DURATION = Duration.ofMinutes(30);

    private final Clock clock;
    private final VehicleTicketValidationLockRepository repository;

    public VehicleTicketValidationLockService(Clock clock, VehicleTicketValidationLockRepository repository) {
        this.clock = clock;
        this.repository = repository;
    }

    @Transactional
    public Instant lock(String vehicleId, Duration requestedDuration) {
        String normalizedVehicleId = normalizeVehicleId(vehicleId);
        if (normalizedVehicleId.isEmpty()) {
            throw new IllegalArgumentException("vehicleId must not be blank");
        }

        Duration duration = normalizeDuration(requestedDuration);
        Instant until = Instant.now(clock).plus(duration);
        repository.save(new VehicleTicketValidationLock(normalizedVehicleId, until));
        return until;
    }

    @Transactional
    public void unlock(String vehicleId) {
        String normalizedVehicleId = normalizeVehicleId(vehicleId);
        if (normalizedVehicleId.isEmpty()) {
            throw new IllegalArgumentException("vehicleId must not be blank");
        }
        repository.deleteById(normalizedVehicleId);
    }

    public boolean isLocked(String vehicleId) {
        return getLockedUntil(vehicleId) != null;
    }

    @Transactional
    public Instant getLockedUntil(String vehicleId) {
        String normalizedVehicleId = normalizeVehicleId(vehicleId);
        if (normalizedVehicleId.isEmpty()) {
            return null;
        }

        VehicleTicketValidationLock lock = repository.findById(normalizedVehicleId).orElse(null);
        if (lock == null) return null;

        Instant until = lock.getLockedUntil();

        Instant now = Instant.now(clock);
        if (!until.isAfter(now)) {
            repository.deleteById(normalizedVehicleId);
            return null;
        }

        return until;
    }

    private Duration normalizeDuration(Duration requestedDuration) {
        if (requestedDuration == null) {
            return DEFAULT_LOCK_DURATION;
        }
        if (requestedDuration.compareTo(MIN_LOCK_DURATION) < 0) {
            return MIN_LOCK_DURATION;
        }
        if (requestedDuration.compareTo(MAX_LOCK_DURATION) > 0) {
            return MAX_LOCK_DURATION;
        }
        return requestedDuration;
    }

    private String normalizeVehicleId(String vehicleId) {
        return vehicleId == null ? "" : vehicleId.trim();
    }
}
