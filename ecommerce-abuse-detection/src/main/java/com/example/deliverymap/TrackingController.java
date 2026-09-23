package com.example.deliverymap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/demo")
public class TrackingController {
    private final TrackingService service;
    private final boolean controls;
    public TrackingController(TrackingService service, @Value("${demo.controls-enabled}") boolean controls) {
        this.service = service; this.controls = controls;
    }
    public record Snapshot(TrackingState tracking, Instant serverNow, boolean controlsEnabled) {}
    public record SpeedRequest(double multiplier) {}
    public record ModeRequest(String mode) {}
    private ResponseEntity<Snapshot> response(TrackingState state) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(new Snapshot(state, Instant.now(), controls));
    }
    @GetMapping("/tracking") public ResponseEntity<Snapshot> get() { return response(service.current()); }
    @PostMapping("/start") public ResponseEntity<Snapshot> start() { requireControls(); return response(service.start()); }
    @PostMapping("/restart") public ResponseEntity<Snapshot> restart() { requireControls(); return response(service.restart()); }
    @PostMapping("/mode") public ResponseEntity<Snapshot> mode(@RequestBody ModeRequest request) {
        requireControls(); return response(service.mode(request.mode()));
    }
    @PostMapping("/speed") public ResponseEntity<Snapshot> speed(@RequestBody SpeedRequest request) {
        requireControls(); return response(service.speed(request.multiplier()));
    }
    private void requireControls() { if (!controls) throw new UnsupportedOperationException("데모 제어가 비활성화되어 있습니다."); }
    @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<?> invalid(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
    @ExceptionHandler(IllegalStateException.class) ResponseEntity<?> unavailable(IllegalStateException e) {
        return ResponseEntity.status(503).body(Map.of("message", e.getMessage()));
    }
    @ExceptionHandler(UnsupportedOperationException.class) ResponseEntity<?> disabled(UnsupportedOperationException e) {
        return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
    }
}
