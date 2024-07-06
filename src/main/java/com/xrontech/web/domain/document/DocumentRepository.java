package com.xrontech.web.domain.document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findByType(DocumentType type);
    List<Document> findByEmployeeId(Long id);
    Optional<Document> findByIdAndEmployeeId(Long id,Long employeeId);
    List<Document> findAllByType(DocumentType type);
    List<Document> findAllByTypeAndEmployeeId(DocumentType type, Long id);

    List<Document> findByEmployeeIdAndType(Long id, DocumentType type);
}
