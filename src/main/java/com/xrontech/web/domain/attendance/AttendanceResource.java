package com.xrontech.web.domain.attendance;

import com.xrontech.web.dto.ApplicationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attendance")
public class AttendanceResource {
    private final AttendanceService attendanceService;

    @PostMapping("/add")
    public ResponseEntity<ApplicationResponseDTO> addAttendance(){
        return ResponseEntity.ok(attendanceService.addAttendance());
    }

    @GetMapping("/get")
    public ResponseEntity<List<Attendance>> getAllAttendances(){
        return ResponseEntity.ok(attendanceService.getAllAttendances());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Attendance> getAttendance(Long id){
        return ResponseEntity.ok(attendanceService.getAttendance(id));
    }

    @GetMapping("/get/{date}")
    public ResponseEntity<Attendance> getAttendance(LocalDate date){
        return ResponseEntity.ok(attendanceService.getAttendance(date));
    }

    @PutMapping("/check-out/{id}")
    public ResponseEntity<ApplicationResponseDTO> checkOutAttendance(@PathVariable("id") Long id){
        return ResponseEntity.ok(attendanceService.checkOutAttendance(id));
    }

}
