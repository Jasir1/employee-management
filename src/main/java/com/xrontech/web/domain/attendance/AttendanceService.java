package com.xrontech.web.domain.attendance;

import com.xrontech.web.EmployeeManagementApplication;
import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.domain.security.service.AuthService;
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
    private final UserRepository userRepository;

    public ApplicationResponseDTO addAttendance() {

        String username = AuthService.getCurrentUser();
        User user = userService.findByUsername(username);

        if (attendanceRepository.findByDate(LocalDate.now()).isEmpty()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_ALREADY_MARKED", "Attendance already marked");
        }

        attendanceRepository.save(
                new Attendance.AttendanceBuilder()
                        .employeeId(user.getId())
                        .checkIn(LocalTime.now())
                        .build()
        );
        return new ApplicationResponseDTO(HttpStatus.CREATED, "ATTENDANCE_MARKED_SUCCESSFULLY", "Attendance marked successfully");
    }

    public List<Attendance> getAllAttendances() {
        String username = AuthService.getCurrentUser();
        User user = userService.findByUsername(username);
        return attendanceRepository.findAllByEmployeeId(user.getId());
    }

    public Attendance getAttendance(Long id) {
        String username = AuthService.getCurrentUser();
        User user = userService.findByUsername(username);
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(id);
        if (optionalAttendance.isEmpty()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_NOT_FOUND", "Attendance not found");
        }
        Attendance attendance = optionalAttendance.get();
        if (!attendance.getEmployeeId().equals(user.getId())){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_ATTENDANCE", "Invalid attendance");
        }
        return attendance;
    }
    public Attendance getAttendance(LocalDate date) {
        String username = AuthService.getCurrentUser();
        User user = userService.findByUsername(username);
        Optional<Attendance> optionalAttendance = attendanceRepository.findByDate(date);
        if (optionalAttendance.isEmpty()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_NOT_FOUND", "Attendance not found");
        }
        Attendance attendance = optionalAttendance.get();
        if (!attendance.getEmployeeId().equals(user.getId())){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_ATTENDANCE", "Invalid attendance");
        }
        return attendance;
    }

    public ApplicationResponseDTO checkOutAttendance(Long id) {
        if (attendanceRepository.findByDate(LocalDate.now()).isEmpty()){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_NOT_ADDED", "Attendance not added");
        }

        Attendance attendance = attendanceRepository.findById(id).orElseThrow(()-> new
                ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_NOT_FOUND", "Attendance not found")
        );
        if (attendance.getCheckOut() != null){
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "ATTENDANCE_CHECK_OUT", "Attendance check out");
        }

        attendance.setCheckOut(LocalTime.now());
        attendanceRepository.save(attendance);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "ATTENDANCE_MARKED_SUCCESSFULLY", "Attendance marked successfully");
    }
}
