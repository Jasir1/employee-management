package com.xrontech.web.domain.document;

import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;

    public ApplicationResponseDTO uploadDocumentDetails(DocumentDTO documentDTO) {
        userRepository.findById(documentDTO.getEmployeeId()).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_USER_Id", "Invalid user id")
        );

        documentRepository.save(
                Document.builder()
                        .employeeId(documentDTO.getEmployeeId())
                        .title(documentDTO.getTitle())
                        .type(documentDTO.getType())
                        .build()
        );

        return new ApplicationResponseDTO(HttpStatus.CREATED, "DOCUMENT_UPLOADED_SUCCESSFULLY", "Document uploaded successfully!");
    }

    public ApplicationResponseDTO uploadDocument(Long id, MultipartFile file) {

        documentRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_DOCUMENT_ID", "Invalid document id")
        );

        if (file.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "FILE_NOT_SELECTED", "File not Selected");
        } else {
            try {
                String projectRoot = System.getProperty("user.dir");
                String originalFilename = file.getOriginalFilename();
                if (originalFilename != null) {
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                    if (!(fileExtension.equalsIgnoreCase(".zip") || fileExtension.equalsIgnoreCase(".pdf"))) {
                        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "Invalid file type. Only zip and pdf are allowed.");
//                        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "Invalid file type. Only JPG, JPEG, and PNG are allowed.");
                    }

                    String newFileName = UUID.randomUUID() + fileExtension;
                    String imagePath = "/uploads/documents/" + newFileName;
                    Path path = Paths.get(projectRoot + imagePath);
                    File saveFile = new File(String.valueOf(path));
                    file.transferTo(saveFile);

                    Document document = documentRepository.findById(id).get();
                    document.setUrl(newFileName);
                    documentRepository.save(document);
                    return new ApplicationResponseDTO(HttpStatus.CREATED, "FILE_UPLOADED_SUCCESSFULLY", "File Uploaded Successfully!");
                } else {
                    throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORIGINAL_FILE_NAME_NOT_FOUND", "Original File Name Not Found");
                }
            } catch (IOException e) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SAVED", "File not Saved");
            }
        }
    }

    public ApplicationResponseDTO updateDocumentDetails(Long id, DocumentUpdateDTO documentUpdateDTO) {

        Document document = documentRepository.findById(id).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_DOCUMENT_ID", "Invalid document id")
        );

        document.setTitle(documentUpdateDTO.getTitle());
        document.setType(documentUpdateDTO.getType());
        documentRepository.save(document);
        return new ApplicationResponseDTO(HttpStatus.CREATED, "DOCUMENT_UPDATED_SUCCESSFULLY", "Document updated successfully");
    }

    public ApplicationResponseDTO deleteDocument(Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "DOCUMENT_ID_NOT_FOUND", "Document id not found"));
        documentRepository.delete(document);
        return new ApplicationResponseDTO(HttpStatus.OK, "DOCUMENT_DELETED_SUCCESSFULLY", "Document deleted successfully!");
    }

    //admin
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public List<Document> getDocumentByType(DocumentType documentType) {
        return documentRepository.findAllByType(documentType);
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_DOCUMENT_ID", "Invalid document id"));
    }

    public List<Document> getDocumentByEmployeeId(Long id) {
        return documentRepository.findByEmployeeId(id);
    }

    public List<Document> getEmployeeDocumentsByType(Long employeeId, DocumentType documentType) {
        User user = userRepository.findById(employeeId).orElseThrow(() ->
                new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_USER_ID", "Invalid user id")
        );
        return documentRepository.findByEmployeeIdAndType(user.getId(), documentType);
    }


    //own
    public List<Document> getOwnDocumentByType(DocumentType documentType) {
        User user = userService.getCurrentUser();
        return documentRepository.findAllByTypeAndEmployeeId(documentType, user.getId());
    }

    public List<Document> getOwnDocuments() {
        User user = userService.getCurrentUser();
        return documentRepository.findByEmployeeId(user.getId());
    }

    public Document getOwnDocumentById(Long id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_DOCUMENT_ID", "Invalid document id"));
        User user = userService.getCurrentUser();
        if (document.getEmployeeId().equals(user.getId())) {
            return document;
        }
        throw new ApplicationCustomException(HttpStatus.FORBIDDEN, "UNAUTHORIZED", "You are not authorized to access this document");
    }

}
