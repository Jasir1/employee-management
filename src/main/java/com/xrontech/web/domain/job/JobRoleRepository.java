package com.xrontech.web.domain.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole,Long> {
    List<JobRole> findAllByTitle(String title);
    Optional<JobRole> findByTitle(String title);
    Optional<JobRole> findByTitleAndDepartmentId(String title, Long departmentId);
    List<JobRole> findByDepartmentId(Long departmentId);
    List<JobRole> findAllByTitleNot(String title);

}
