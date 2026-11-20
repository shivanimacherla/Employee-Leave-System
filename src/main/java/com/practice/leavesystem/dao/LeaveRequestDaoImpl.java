package com.practice.leavesystem.dao;

import com.practice.leavesystem.entity.LeaveRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class LeaveRequestDaoImpl implements LeaveRequestDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public LeaveRequest save(LeaveRequest leaveRequest) {
        entityManager.persist(leaveRequest);
        return leaveRequest;
    }

    @Override
    public Optional<LeaveRequest> findById(Long id) {
        LeaveRequest result = entityManager.find(LeaveRequest.class, id);
        return Optional.ofNullable(result);
    }

    @Override
    public List<LeaveRequest> findAll() {
        TypedQuery<LeaveRequest> query = entityManager.createQuery(
                "SELECT lr FROM LeaveRequest lr", LeaveRequest.class);
        return query.getResultList();
    }

    @Override
    public List<LeaveRequest> findByEmployeeId(Long employeeId) {
        TypedQuery<LeaveRequest> query = entityManager.createQuery(
                "SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :employeeId", LeaveRequest.class);
        query.setParameter("employeeId", employeeId);
        return query.getResultList();
    }

    @Override
    @Transactional
    public LeaveRequest update(LeaveRequest leaveRequest) {
        return entityManager.merge(leaveRequest);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        LeaveRequest ref = entityManager.find(LeaveRequest.class, id);
        if (ref != null) {
            entityManager.remove(ref);
        }
    }
}
