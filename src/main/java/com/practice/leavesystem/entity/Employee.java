package com.practice.leavesystem.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate dateOfJoining;

    private int leaveBalance = 20;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequests;

    public Employee() {}

    public Employee(String firstName, String lastName, String email, LocalDate dateOfJoining, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfJoining = dateOfJoining;
        this.department = department;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(LocalDate dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public int getLeaveBalance() { return leaveBalance; }
    public void setLeaveBalance(int leaveBalance) { this.leaveBalance = leaveBalance; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public List<LeaveRequest> getLeaveRequests() { return leaveRequests; }
    public void setLeaveRequests(List<LeaveRequest> leaveRequests) { this.leaveRequests = leaveRequests; }
}
