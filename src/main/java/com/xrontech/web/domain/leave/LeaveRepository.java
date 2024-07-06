package com.xrontech.web.domain.leave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {
    List<Leave> findAllByEmployeeId(Long userId);
    List<Leave> findByStatus(LeaveStatus leaveStatus);
    List<Leave> findByType(LeaveType leaveType);
    List<Leave> findByStatusAndEmployeeId(LeaveStatus leaveStatus, Long id);
    List<Leave> findByTypeAndEmployeeId(LeaveType leaveType, Long id);

    List<Leave> findAllByEmployeeIdAndStatus(Long userId, LeaveStatus leaveStatus);

    List<Leave> findAllByEmployeeIdAndType(Long userId, LeaveType leaveType);
}
