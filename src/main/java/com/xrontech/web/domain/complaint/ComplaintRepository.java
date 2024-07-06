package com.xrontech.web.domain.complaint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
    List<Complaint> findAllByEmployeeId(Long userId);
    List<Complaint> findAllByType(ComplaintType complaintType);

    List<Complaint> findAllByTypeAndEmployeeId(ComplaintType complaintType, Long id);

    List<Complaint> findAllByEmployeeIdAndType(Long id, ComplaintType complaintType);

    List<Complaint> findAllByStatus(ComplaintStatus complaintStatus);

    List<Complaint> findAllByEmployeeIdAndStatus(Long id, ComplaintStatus complaintStatus);
}
