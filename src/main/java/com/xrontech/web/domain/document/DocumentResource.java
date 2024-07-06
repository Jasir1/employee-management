package com.xrontech.web.domain.document;

import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/document")
@SecurityRequirement(name = "ems")
public class DocumentResource {
    private final DocumentService documentService;

    @PostMapping("/upload-details")
    public ResponseEntity<ApplicationResponseDTO> uploadDocumentDetails(@Valid @RequestBody DocumentDTO documentDTO){
        return ResponseEntity.ok(documentService.uploadDocumentDetails(documentDTO));
    }
    @PutMapping("/upload-file/{id}")
    public ResponseEntity<ApplicationResponseDTO> uploadDocument(@PathVariable("id") Long id,@RequestBody MultipartFile file){
        return ResponseEntity.ok(documentService.uploadDocument(id,file));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateDocumentDetails(@PathVariable("id") Long id, @Valid @RequestBody DocumentUpdateDTO documentUpdateDTO){
        return ResponseEntity.ok(documentService.updateDocumentDetails(id,documentUpdateDTO));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteDocument(@PathVariable("id") Long id) {
        return ResponseEntity.ok(documentService.deleteDocument(id));
    }

    //admin
    @GetMapping("/get")
    public ResponseEntity<List<Document>> getAllDocuments(){
        return ResponseEntity.ok(documentService.getAllDocuments());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable("id") Long id){
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }
    @GetMapping("/get/user/{id}")
    public ResponseEntity<List<Document>> getDocumentByEmployeeId(@PathVariable("id") Long id){
        return ResponseEntity.ok(documentService.getDocumentByEmployeeId(id));
    }
    @GetMapping("/get/type/{user-id}/{type}")
    public ResponseEntity<List<Document>> getEmployeeDocumentsByType(@PathVariable("user-id") Long id,@PathVariable("type") DocumentType type){
        return ResponseEntity.ok(documentService.getEmployeeDocumentsByType(id,type));
    }
    @GetMapping("/get/type/{type}")
    public ResponseEntity<List<Document>> getDocumentByType(@PathVariable("type") DocumentType type){
        return ResponseEntity.ok(documentService.getDocumentByType(type));
    }


    //own
    @GetMapping("/get-own")
    public ResponseEntity<List<Document>> getOwnDocuments(){
        return ResponseEntity.ok(documentService.getOwnDocuments());
    }
    @GetMapping("/get-own/{id}")
    public ResponseEntity<Document> getOwnDocumentById(@PathVariable("id") Long id){
        return ResponseEntity.ok(documentService.getOwnDocumentById(id));
    }
    @GetMapping("/get-own/type/{type}")
    public ResponseEntity<List<Document>> getOwnDocumentByType(@PathVariable("type") DocumentType type){
        return ResponseEntity.ok(documentService.getOwnDocumentByType(type));
    }

}
