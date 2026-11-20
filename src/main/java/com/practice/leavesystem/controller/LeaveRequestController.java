package com.practice.leavesystem.controller;

import com.practice.leavesystem.dto.LeaveRequestDTO;
import com.practice.leavesystem.entity.LeaveRequest;
import com.practice.leavesystem.enums.LeaveStatus;
import com.practice.leavesystem.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping
    public ResponseEntity<LeaveRequest> submitLeaveRequest(@Valid @RequestBody LeaveRequestDTO dto) {
        LeaveRequest created = leaveRequestService.submitLeaveRequest(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestById(id));
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsForEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequestsForEmployee(employeeId));
    }

    // Status transitions - e.g. { "status": "APPROVED" }
    @PatchMapping("/{id}/status")
    public ResponseEntity<LeaveRequest> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        LeaveStatus newStatus = LeaveStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(leaveRequestService.updateStatus(id, newStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }
}
