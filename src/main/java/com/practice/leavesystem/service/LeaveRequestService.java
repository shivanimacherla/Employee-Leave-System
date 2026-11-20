package com.practice.leavesystem.service;

import com.practice.leavesystem.dao.LeaveRequestDao;
import com.practice.leavesystem.dto.LeaveRequestDTO;
import com.practice.leavesystem.entity.Employee;
import com.practice.leavesystem.entity.LeaveRequest;
import com.practice.leavesystem.enums.LeaveStatus;
import com.practice.leavesystem.exception.InvalidLeaveRequestException;
import com.practice.leavesystem.exception.ResourceNotFoundException;
import com.practice.leavesystem.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestDao leaveRequestDao;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestService(LeaveRequestDao leaveRequestDao, EmployeeRepository employeeRepository) {
        this.leaveRequestDao = leaveRequestDao;
        this.employeeRepository = employeeRepository;
    }

    public LeaveRequest submitLeaveRequest(LeaveRequestDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new InvalidLeaveRequestException("End date cannot be before start date");
        }

        LeaveRequest leaveRequest = new LeaveRequest(employee, dto.getStartDate(), dto.getEndDate(), dto.getReason());

        long requestedDays = leaveRequest.getNumberOfDays();
        if (requestedDays > employee.getLeaveBalance()) {
            throw new InvalidLeaveRequestException(
                    "Requested " + requestedDays + " day(s) exceeds available leave balance of " + employee.getLeaveBalance());
        }

        List<LeaveRequest> existing = leaveRequestDao.findByEmployeeId(employee.getId());
        boolean overlaps = existing.stream()
                .filter(lr -> lr.getStatus() == LeaveStatus.PENDING || lr.getStatus() == LeaveStatus.APPROVED)
                .anyMatch(lr -> !dto.getEndDate().isBefore(lr.getStartDate()) && !dto.getStartDate().isAfter(lr.getEndDate()));

        if (overlaps) {
            throw new InvalidLeaveRequestException("Requested dates overlap with an existing pending or approved leave request");
        }

        return leaveRequestDao.save(leaveRequest);
    }

    public LeaveRequest getLeaveRequestById(Long id) {
        return leaveRequestDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestDao.findAll();
    }

    public List<LeaveRequest> getLeaveRequestsForEmployee(Long employeeId) {
        return leaveRequestDao.findByEmployeeId(employeeId);
    }

    public LeaveRequest updateStatus(Long id, LeaveStatus newStatus) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);
        LeaveStatus currentStatus = leaveRequest.getStatus();

        validateTransition(currentStatus, newStatus);

        if (newStatus == LeaveStatus.APPROVED) {
            Employee employee = leaveRequest.getEmployee();
            long days = leaveRequest.getNumberOfDays();
            if (days > employee.getLeaveBalance()) {
                throw new InvalidLeaveRequestException("Cannot approve - insufficient leave balance");
            }
            employee.setLeaveBalance((int) (employee.getLeaveBalance() - days));
            employeeRepository.save(employee);
        }

        leaveRequest.setStatus(newStatus);
        return leaveRequestDao.update(leaveRequest);
    }

    private void validateTransition(LeaveStatus current, LeaveStatus next) {
        boolean valid = switch (current) {
            case PENDING -> next == LeaveStatus.APPROVED || next == LeaveStatus.REJECTED || next == LeaveStatus.CANCELLED;
            case APPROVED -> next == LeaveStatus.CANCELLED;
            case REJECTED, CANCELLED -> false;
        };

        if (!valid) {
            throw new InvalidLeaveRequestException(
                    "Invalid status transition from " + current + " to " + next);
        }
    }

    public void deleteLeaveRequest(Long id) {
        getLeaveRequestById(id);
        leaveRequestDao.deleteById(id);
    }
}
