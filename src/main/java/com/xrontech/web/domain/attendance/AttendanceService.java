package com.xrontech.web.domain.attendance;

import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserService userService;

    public ApplicationResponseDTO checkIn() {

        if (isCheckIn()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_ALREADY_CHECKED_IN", "Attendance already checked in!");
        }

        User user = userService.findByUsername(userService.getCurrentUser().getUsername());

        attendanceRepository.save(
                new Attendance.AttendanceBuilder()
                        .employeeId(user.getId())
                        .checkIn(LocalTime.now())
                        .date(LocalDate.now())
                        .build()
        );
        return new ApplicationResponseDTO(HttpStatus.CREATED, "ATTENDANCE_MARKED_SUCCESSFULLY", "Attendance marked successfully");

    }

    public Boolean isCheckIn() {
        User user = userService.findByUsername(userService.getCurrentUser().getUsername());
        return attendanceRepository.findByDate(LocalDate.now()).map(attendance -> attendance.getEmployeeId().equals(user.getId()) && attendance.getCheckOut() == null).orElse(false);
    }

    public ApplicationResponseDTO checkOut() {
        if (isCheckIn()) {
            Optional<Attendance> optionalAttendance = attendanceRepository.findByDate(LocalDate.now());
            if (optionalAttendance.isEmpty()) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_NOT_FOUND", "Attendance not found");
            }

            Attendance attendance = optionalAttendance.get();
            attendance.setCheckOut(LocalTime.now());
            attendanceRepository.save(attendance);

            return new ApplicationResponseDTO(HttpStatus.OK, "ATTENDANCE_CHECKED_OUT", "Attendance checked out");
        }
        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_ATTENDANCE", "Invalid Attendance");
    }


    //admin
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }
    public Attendance getAttendanceById(Long id) {
        return attendanceRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_NOT_FOUND", "Attendance not found"));
    }
    public List<Attendance> getAttendanceByUserId(Long userId) {
        return attendanceRepository.findAllByEmployeeId(userId);
    }
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findAlByDate(date);
    }
    public Attendance getAttendanceUserIdAndByDate(Long userId, LocalDate date) {
        return attendanceRepository.findByEmployeeIdAndDate(userId,date);
    }


    //user
    public List<Attendance> getOwnAttendances() {
        User user = userService.findByUsername(userService.getCurrentUser().getUsername());
        return attendanceRepository.findAllByEmployeeId(user.getId());
    }


    public Attendance getOwnAttendanceById(Long id) {
        User user = userService.findByUsername(userService.getCurrentUser().getUsername());
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(id);

        return getAttendance(user, optionalAttendance);
    }

    public Attendance getOwnAttendanceByDate(LocalDate date) {
        User user = userService.findByUsername(userService.getCurrentUser().getUsername());
        Optional<Attendance> optionalAttendance = attendanceRepository.findByDate(date);
        return getAttendance(user, optionalAttendance);
    }
    private Attendance getAttendance(User user, Optional<Attendance> optionalAttendance) {
        if (optionalAttendance.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_NOT_FOUND", "Attendance not found");
        }
        Attendance attendance = optionalAttendance.get();
        if (!attendance.getEmployeeId().equals(user.getId())) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_ATTENDANCE", "Invalid attendance");
        }
        return attendance;
    }



}