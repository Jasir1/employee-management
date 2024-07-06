package com.xrontech.web.domain.complaint;

import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ApplicationResponseDTO raiseCompliant(ComplaintDTO complaintDTO) {

        complaintRepository.save(
                Complaint.builder()
                        .type(complaintDTO.getType())
                        .description(complaintDTO.getDescription())
                        .employeeId(userService.getCurrentUser().getId())
                        .status(ComplaintStatus.RAISED)
                        .build());
        return new ApplicationResponseDTO(HttpStatus.CREATED, "COMPLAINT_ADDED_SUCCESSFULLY", "Complaint added successfully!");
    }

    public ApplicationResponseDTO updateComplaintToInProgress(Long id) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_COMPLAINT_ID", "Invalid complaint id"));
        if (complaint.getStatus().equals(ComplaintStatus.RAISED)){
            complaint.setStatus(ComplaintStatus.IN_PROGRESS);
            complaintRepository.save(complaint);
            return new ApplicationResponseDTO(HttpStatus.OK, "COMPLAINT_STATUS_UPDATED_SUCCESSFULLY", "Complaint status updated successfully!");
        }
        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "COMPLAINT_ALREADY_"+complaint.getStatus(), "Complaint already "+complaint.getStatus());
    }
    public ApplicationResponseDTO updateComplaintToResolved(Long id) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_COMPLAINT_ID", "Invalid complaint id"));
        if (complaint.getStatus().equals(ComplaintStatus.RESOLVED)){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "COMPLAINT_ALREADY_RESOLVED", "Complaint already resolved");
        }

        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaintRepository.save(complaint);
        return new ApplicationResponseDTO(HttpStatus.OK, "COMPLAINT_STATUS_UPDATED_SUCCESSFULLY", "Complaint status updated successfully!");
    }

    //admin
    public List<Complaint> getComplaints() {
        return complaintRepository.findAll();
    }

    public Complaint getComplaintById(Long id) {
        return complaintRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_COMPLAINT_ID", "Invalid complaint id"));
    }

    public List<Complaint> getComplaintByEmployeeId(Long userId) {
        return complaintRepository.findAllByEmployeeId(userId);
    }
    public List<Complaint> getComplaintByType(ComplaintType complaintType) {
        return complaintRepository.findAllByType(complaintType);
    }
    public List<Complaint> getComplaintByStatus(ComplaintStatus complaintStatus) {
        return complaintRepository.findAllByStatus(complaintStatus);
    }
    public List<Complaint> getUserComplaintByType(Long userId, ComplaintType complaintType) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_USER_ID", "Invalid user id"));
        return complaintRepository.findAllByEmployeeIdAndType(user.getId(),complaintType);
    }
    public List<Complaint> getUserComplaintByStatus(Long userId, ComplaintStatus complaintStatus) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_USER_ID", "Invalid user id"));
        return complaintRepository.findAllByEmployeeIdAndStatus(user.getId(),complaintStatus);
    }



    //user
    public List<Complaint> getOwnComplaints() {
        return complaintRepository.findAllByEmployeeId(userService.getCurrentUser().getId());
    }
    public Complaint getOwnComplaintById(Long id) {
        Complaint complaint = complaintRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_COMPLAINT_ID", "Invalid complaint id"));
        if (!complaint.getEmployeeId().equals(userService.getCurrentUser().getId())) {
            throw new ApplicationCustomException(HttpStatus.FORBIDDEN, "UNAUTHORIZED_FILE_ACCESS", "You are not authorized to access this resource");
        }
        return complaint;
    }
    public List<Complaint> getOwnComplaintByType(ComplaintType complaintType) {
        return complaintRepository.findAllByTypeAndEmployeeId(complaintType, userService.getCurrentUser().getId());
    }
    public List<Complaint> getOwnComplaintByStatus(ComplaintStatus complaintStatus) {
        return complaintRepository.findAllByEmployeeIdAndStatus(userService.getCurrentUser().getId(),complaintStatus);
    }


}
