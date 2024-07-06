package com.xrontech.web.domain.leave;

import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/leave")
@SecurityRequirement(name = "ems")
public class LeaveResource {
    private final LeaveService leaveService;

    @PostMapping("/request")
    public ResponseEntity<ApplicationResponseDTO> requestLeave(@Valid @RequestBody LeaveDTO leaveDTO){
        return ResponseEntity.ok(leaveService.requestLeave(leaveDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateLeave(@PathVariable("id") Long id, @Valid @RequestBody LeaveDTO leaveDTO){
        return ResponseEntity.ok(leaveService.updateLeave(id, leaveDTO));
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<ApplicationResponseDTO> approveLeave(@PathVariable("id") Long id){
        return ResponseEntity.ok(leaveService.approveLeave(id));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Leave>> getLeaves(){
        return ResponseEntity.ok(leaveService.getLeaves());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Leave> getLeaveById(@PathVariable("id") Long id){
        return ResponseEntity.ok(leaveService.getLeaveById(id));
    }
    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<Leave>> getLeaveByEmployeeId(@PathVariable("id") Long id){
        return ResponseEntity.ok(leaveService.getLeaveByEmployeeId(id));
    }
    @GetMapping("/get/status/{user-id}/{status}")
    public ResponseEntity<List<Leave>> getUserLeaveByStatus(@PathVariable("user-id") Long userId, @PathVariable("status") LeaveStatus leaveStatus){
        return ResponseEntity.ok(leaveService.getUserLeaveByStatus(userId,leaveStatus));
    }
    @GetMapping("/get/type/{user-id}/{type}")
    public ResponseEntity<List<Leave>> getUserLeaveByType(@PathVariable("user-id") Long userId, @PathVariable("type") LeaveType leaveType){
        return ResponseEntity.ok(leaveService.getUserLeaveByType(userId,leaveType));
    }
    @GetMapping("/get/status/{status}")
    public ResponseEntity<List<Leave>> getLeaveByStatus(@PathVariable("status") LeaveStatus leaveStatus){
        return ResponseEntity.ok(leaveService.getLeaveByStatus(leaveStatus));
    }
    @GetMapping("/get/type/{type}")
    public ResponseEntity<List<Leave>> getLeaveByType(@PathVariable("type") LeaveType leaveType){
        return ResponseEntity.ok(leaveService.getLeaveByType(leaveType));
    }


    //user
    @GetMapping("/get-own")
    public ResponseEntity<List<Leave>> getOwnLeaves(){
        return ResponseEntity.ok(leaveService.getOwnLeaves());
    }
    @GetMapping("/get-own/{id}")
    public ResponseEntity<Leave> getOwnLeaveById(@PathVariable("id") Long id){
        return ResponseEntity.ok(leaveService.getOwnLeaveById(id));
    }
    @GetMapping("/get-own/status/{status}")
    public ResponseEntity<List<Leave>> getOwnLeaveByStatus(@PathVariable("status") LeaveStatus leaveStatus){
        return ResponseEntity.ok(leaveService.getOwnLeaveByStatus(leaveStatus));
    }
    @GetMapping("/get-own/type/{type}")
    public ResponseEntity<List<Leave>> getOwnLeaveByType(@PathVariable("type") LeaveType leaveType){
        return ResponseEntity.ok(leaveService.getOwnLeaveByType(leaveType));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ApplicationResponseDTO> cancelLeave(@PathVariable("id") Long id){
        return ResponseEntity.ok(leaveService.cancelLeave(id));
    }

}
