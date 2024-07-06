package com.xrontech.web.domain.complaint;

import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/complaint")
@SecurityRequirement(name = "ems")
public class ComplaintResource {
    private final ComplaintService complaintService;

    @PostMapping("/raise")
    public ResponseEntity<ApplicationResponseDTO> raiseCompliant(@Valid @RequestBody ComplaintDTO complaintDTO){
        return ResponseEntity.ok(complaintService.raiseCompliant(complaintDTO));
    }

    @PutMapping("/update-to-inprogress/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateComplaintToInProgress(@PathVariable("id") Long id){
        return ResponseEntity.ok(complaintService.updateComplaintToInProgress(id));
    }
    @PutMapping("/update-to-resolved/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateComplaintToResolved(@PathVariable("id") Long id){
        return ResponseEntity.ok(complaintService.updateComplaintToResolved(id));
    }

    //admin
    @GetMapping("/get")
    public ResponseEntity<List<Complaint>> getComplaints(){
        return ResponseEntity.ok(complaintService.getComplaints());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable("id") Long id){
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }
    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<Complaint>> getComplaintByEmployeeId(@PathVariable("id") Long id){
        return ResponseEntity.ok(complaintService.getComplaintByEmployeeId(id));
    }
    @GetMapping("/get/type/{type}")
    public ResponseEntity<List<Complaint>> getComplaintByType(@PathVariable("type") ComplaintType complaintType){
        return ResponseEntity.ok(complaintService.getComplaintByType(complaintType));
    }
    @GetMapping("/get/status/{status}")
    public ResponseEntity<List<Complaint>> getComplaintByStatus(@PathVariable("status") ComplaintStatus complaintStatus){
        return ResponseEntity.ok(complaintService.getComplaintByStatus(complaintStatus));
    }
    @GetMapping("/get/type/{user-id}/{type}")
    public ResponseEntity<List<Complaint>> getUserComplaintByType(@PathVariable("user-id") Long userId, @PathVariable("type") ComplaintType complaintType){
        return ResponseEntity.ok(complaintService.getUserComplaintByType(userId,complaintType));
    }
    @GetMapping("/get/status/{user-id}/{status}")
    public ResponseEntity<List<Complaint>> getUserComplaintByStatus(@PathVariable("user-id") Long userId, @PathVariable("status") ComplaintStatus complaintStatus){
        return ResponseEntity.ok(complaintService.getUserComplaintByStatus(userId,complaintStatus));
    }

    //user
    @GetMapping("/get-own")
    public ResponseEntity<List<Complaint>> getOwnComplaints(){
        return ResponseEntity.ok(complaintService.getOwnComplaints());
    }
    @GetMapping("/get-own/{id}")
    public ResponseEntity<Complaint> getOwnComplaintById(@PathVariable("id") Long id){
        return ResponseEntity.ok(complaintService.getOwnComplaintById(id));
    }
    @GetMapping("/get-own/type/{id}")
    public ResponseEntity<List<Complaint>> getOwnComplaintByType(@PathVariable("id") ComplaintType complaintType){
        return ResponseEntity.ok(complaintService.getOwnComplaintByType(complaintType));
    }
    @GetMapping("/get-own/status/{id}")
    public ResponseEntity<List<Complaint>> getOwnComplaintByStatus(@PathVariable("id") ComplaintStatus complaintStatus){
        return ResponseEntity.ok(complaintService.getOwnComplaintByStatus(complaintStatus));
    }


}
