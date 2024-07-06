package com.xrontech.web.domain.attendance;

import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attendance")
@SecurityRequirement(name = "ems")
public class AttendanceResource {
    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<ApplicationResponseDTO> checkIn(){
        return ResponseEntity.ok(attendanceService.checkIn());
    }

    @GetMapping("/get")
    public ResponseEntity<List<Attendance>> getAllAttendances(){
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }
    @GetMapping("/is-check-in")
    public ResponseEntity<Boolean> isCheckIn(){
        return ResponseEntity.ok(attendanceService.isCheckIn());
    }
    @PutMapping("/check-out")
    public ResponseEntity<ApplicationResponseDTO> checkOut(){
        return ResponseEntity.ok(attendanceService.checkOut());
    }

    //admin
    @GetMapping("/get/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable("id") Long id){
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @GetMapping("/get/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(@PathVariable("date") LocalDate date){
        return ResponseEntity.ok(attendanceService.getAttendanceByDate(date));
    }
    @GetMapping("/get/user/{user-id}")
    public ResponseEntity<List<Attendance>> getAttendanceByUserId(@PathVariable("user-id") Long userId){
        return ResponseEntity.ok(attendanceService.getAttendanceByUserId(userId));
    }
    @GetMapping("/get/user/{user-id}/{date}")
    public ResponseEntity<Attendance> getAttendanceUserIdAndByDate(@PathVariable("user-id") Long userId, @PathVariable("date") LocalDate date){
        return ResponseEntity.ok(attendanceService.getAttendanceUserIdAndByDate(userId, date));
    }


    //user
    @GetMapping("/get-own")
    public ResponseEntity<List<Attendance>> getOwnAttendances(){
        return ResponseEntity.ok(attendanceService.getOwnAttendances());
    }
    @GetMapping("/get-own/{id}")
    public ResponseEntity<Attendance> getOwnAttendanceById(@PathVariable("id") Long Id){
        return ResponseEntity.ok(attendanceService.getOwnAttendanceById(Id));
    }
    @GetMapping("/get-own/date/{date}")
    public ResponseEntity<Attendance> getOwnAttendanceByDate(@PathVariable("date") LocalDate date){
        return ResponseEntity.ok(attendanceService.getOwnAttendanceByDate(date));
    }

}
