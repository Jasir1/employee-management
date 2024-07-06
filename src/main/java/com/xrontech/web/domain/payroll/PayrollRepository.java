package com.xrontech.web.domain.payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Long> {

    List<Payroll> findAllByEmployeeId(Long employeeId);

    List<Payroll> findByPayDate(LocalDate payDate);

    List<Payroll> findAllByEmployeeIdAndPayDate(Long id, LocalDate date);

    List<Payroll> findAllByPayDateAndEmployeeId(LocalDate date, Long id);
}
