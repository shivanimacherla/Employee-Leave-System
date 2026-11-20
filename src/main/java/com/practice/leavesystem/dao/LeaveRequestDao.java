package com.practice.leavesystem.dao;

import com.practice.leavesystem.entity.LeaveRequest;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestDao {
    LeaveRequest save(LeaveRequest leaveRequest);
    Optional<LeaveRequest> findById(Long id);
    List<LeaveRequest> findAll();
    List<LeaveRequest> findByEmployeeId(Long employeeId);
    LeaveRequest update(LeaveRequest leaveRequest);
    void deleteById(Long id);
}
