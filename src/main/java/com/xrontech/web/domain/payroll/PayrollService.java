package com.xrontech.web.domain.payroll;

import com.xrontech.web.domain.security.entity.User;
import com.xrontech.web.domain.security.repos.UserRepository;
import com.xrontech.web.domain.user.UserService;
import com.xrontech.web.dto.ApplicationResponseDTO;
import com.xrontech.web.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final PayrollRepository payrollRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public ApplicationResponseDTO addPayroll(PayrollDTO payrollDTO) {

        User user = userRepository.findById(payrollDTO.getEmployeeId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "EMPLOYEE_NOT_FOUND", "Employee not found"));

        Payroll payroll = new Payroll();
        payroll.setEmployeeId(payrollDTO.getEmployeeId());
        payroll.setBonus(payrollDTO.getBonus() == null ? 0.0 : payrollDTO.getBonus());
        payroll.setDeduction(payrollDTO.getDeduction() == null ? 0.0 : payrollDTO.getDeduction());
        payroll.setNetPay(getNetPay(user.getJobRole().getSalary(), payrollDTO.getBonus(), payrollDTO.getDeduction()));
        payroll.setPayDate(payrollDTO.getPayDate());
        payrollRepository.save(payroll);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "PAYROLL_ADDED_SUCCESSFULLY", "Payroll added successfully");
    }

    public ApplicationResponseDTO updatePayroll(Long id, PayrollUpdateDTO payrollUpdateDTO) {

        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PAYROLL_NOT_FOUND", "Payroll not found"));

        User user = payroll.getEmployee();
        payroll.setBonus(payrollUpdateDTO.getBonus() == null ? payroll.getBonus() : payrollUpdateDTO.getBonus());
        payroll.setDeduction(payrollUpdateDTO.getDeduction() == null ? payroll.getDeduction() : payrollUpdateDTO.getDeduction());
        payroll.setNetPay(getNetPay(user.getJobRole().getSalary(), payrollUpdateDTO.getBonus(), payrollUpdateDTO.getDeduction()));
        payroll.setPayDate(payrollUpdateDTO.getPayDate());
        payrollRepository.save(payroll);
        return new ApplicationResponseDTO(HttpStatus.OK, "PAYROLL_UPDATED_SUCCESSFULLY", "Payroll updated successfully");
    }

    public ApplicationResponseDTO deletePayroll(Long id) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PAYROLL_NOT_FOUND", "Payroll not found"));
        payrollRepository.delete(payroll);
        return new ApplicationResponseDTO(HttpStatus.OK, "PAYROLL_DELETED_SUCCESSFULLY", "Payroll deleted successfully");
    }

    //admin
    public List<Payroll> getAllPayrolls() {
        return payrollRepository.findAll();

    }

    public Payroll getPayrollById(Long id) {
        return payrollRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "PAYROLL_NOT_FOUND", "Payroll not found"));
    }

    public List<Payroll> getPayrollByEmployeeId(Long employeeId) {
        return payrollRepository.findAllByEmployeeId(employeeId);
    }

    public List<Payroll> getPayrollByPayDate(LocalDate payDate) {
        return payrollRepository.findByPayDate(payDate);
    }

    public List<Payroll> getUserPayrollByDate(Long userId, LocalDate date) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_USER_ID", "Invalid user id"));
        return payrollRepository.findAllByEmployeeIdAndPayDate(user.getId(), date);
    }


    //user
    public List<Payroll> getOwnPayrolls() {
        return payrollRepository.findAllByEmployeeId(userService.getCurrentUser().getId());
    }

    public Payroll getOwnPayrollById(Long id) {
        Payroll payroll = payrollRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "INVALID_PAYROLL_ID", "Invalid payroll id"));
        if (!payroll.getEmployeeId().equals(userService.getCurrentUser().getId())) {
            throw new ApplicationCustomException(HttpStatus.FORBIDDEN, "UNAUTHORIZED_ACCESS", "You are not authorized to access");
        }
        return payroll;
    }

    public List<Payroll> getOwnPayrollByDate(LocalDate date) {
        return payrollRepository.findAllByPayDateAndEmployeeId(date, userService.getCurrentUser().getId());
    }

    private Double getNetPay(Double salary, Double bonus, Double deduction) {
        return salary + (bonus != null ? bonus : 0.0) - (deduction != null ? deduction : 0.0);
    }
}
