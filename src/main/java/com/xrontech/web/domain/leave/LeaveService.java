package com.xrontech.web.domain.leave;

import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {
    private final LeaveRepository leaveRepository;
    private final UserService userService;

    public ApplicationResponseDTO requestLeave(LeaveDTO leaveDTO) {
        if (leaveDTO.getToDate().isBefore(leaveDTO.getFromDate())){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_DATE_RANGE", "Invalid date range");
        }
        leaveRepository.save(
                Leave.builder()
                        .fromDate(leaveDTO.getFromDate())
                        .toDate(leaveDTO.getToDate())
                        .status(LeaveStatus.PENDING)
                        .type(leaveDTO.getType())
                        .employeeId(userService.getCurrentUser().getId())
                        .build());
        return new ApplicationResponseDTO(HttpStatus.CREATED, "LEAVE_REQUEST_SENT_SUCCESSFULLY", "Leave request sent successfully!");
    }

    //admin
    public List<Leave> getLeaves() {
        return leaveRepository.findAll();
    }
    public Leave getLeaveById(Long id) {
        return leaveRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_LEAVE_ID", "Invalid leave id"));
    }
    public List<Leave> getLeaveByEmployeeId(Long userId) {
        return leaveRepository.findAllByEmployeeId(userId);
    }
    public List<Leave> getUserLeaveByStatus(Long userId,LeaveStatus leaveStatus) {
        return leaveRepository.findAllByEmployeeIdAndStatus(userId,leaveStatus);
    }
    public List<Leave> getUserLeaveByType(Long userId,LeaveType leaveType) {
        return leaveRepository.findAllByEmployeeIdAndType(userId,leaveType);
    }
    public List<Leave> getLeaveByStatus(LeaveStatus leaveStatus) {
        return leaveRepository.findByStatus(leaveStatus);
    }
    public List<Leave> getLeaveByType(LeaveType leaveType) {
        return leaveRepository.findByType(leaveType);
    }

    //user
    public List<Leave> getOwnLeaves() {
        return leaveRepository.findAllByEmployeeId(userService.getCurrentUser().getId());
    }
    public Leave getOwnLeaveById(Long id) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_LEAVE_ID", "Invalid leave id"));
        if (!leave.getEmployeeId().equals(userService.getCurrentUser().getId())) {
            throw new ApplicationCustomException(HttpStatus.FORBIDDEN, "UNAUTHORIZED_FILE_ACCESS", "You are not authorized to access this resource");
        }
        return leave;
    }
    public List<Leave> getOwnLeaveByStatus(LeaveStatus leaveStatus) {
        return leaveRepository.findByStatusAndEmployeeId(leaveStatus,userService.getCurrentUser().getId());
    }
    public List<Leave> getOwnLeaveByType(LeaveType leaveType) {
        return leaveRepository.findByTypeAndEmployeeId(leaveType,userService.getCurrentUser().getId());
    }

    public ApplicationResponseDTO updateLeave(Long id, LeaveDTO leaveDTO) {

        Leave leave = leaveRepository.findById(id).orElseThrow(() ->
            new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_LEAVE_ID", "Invalid leave id")
        );

        if (leaveDTO.getToDate().isBefore(leaveDTO.getFromDate())){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_DATE_RANGE", "Invalid date range");
        }

        if (leave.getStatus().equals(LeaveStatus.PENDING)){
            leave.setFromDate(leaveDTO.getFromDate());
            leave.setToDate(leaveDTO.getToDate());
            leave.setType(leaveDTO.getType());
            leaveRepository.save(leave);
            return new ApplicationResponseDTO(HttpStatus.CREATED, "LEAVE_UPDATED_SUCCESSFULLY", "Leave updated successfully");
        }

        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "LEAVE_CANNOT_BE_UPDATED", "Leave cannot be updated");
    }


    public ApplicationResponseDTO cancelLeave(Long id) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_LEAVE_ID", "Invalid leave id")
        );

        if (leave.getStatus().equals(LeaveStatus.PENDING)){
            leave.setStatus(LeaveStatus.CANCELLED);
            leaveRepository.save(leave);
            return new ApplicationResponseDTO(HttpStatus.OK, "LEAVE_CANCELLED_SUCCESSFULLY", "Leave cancelled successfully");
        } else if (leave.getStatus().equals(LeaveStatus.CANCELLED)){
            return new ApplicationResponseDTO(HttpStatus.BAD_REQUEST, "LEAVE_ALREADY_SUCCESSFULLY", "Leave already successfully");
        }

        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "YOU_CANNOT_CANCEL_APPROVED_OR_REJECTED_LEAVE", "You cannot cancel approved or rejected leave");
    }

    public ApplicationResponseDTO approveLeave(Long id) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_LEAVE_ID", "Invalid leave id")
        );

        if (leave.getStatus().equals(LeaveStatus.PENDING)) {
            leave.setStatus(LeaveStatus.APPROVED);
            leaveRepository.save(leave);
            return new ApplicationResponseDTO(HttpStatus.CREATED, "LEAVE_APPROVED_SUCCESSFULLY", "Leave approved successfully");
        } else if (leave.getStatus().equals(LeaveStatus.APPROVED)) {
            return new ApplicationResponseDTO(HttpStatus.BAD_REQUEST, "LEAVE_ALREADY_SUCCESSFULLY", "Leave already successfully");
        }

        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "YOU_CANNOT_APPROVE_REJECTED_LEAVE", "You cannot approve rejected leave");
    }
}
