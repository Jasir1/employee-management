package com.xrontech.web.domain.payroll;

import com.xrontech.web.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payroll")
@SecurityRequirement(name = "ems")
public class PayrollResource {
    private final PayrollService payrollService;

    @PostMapping("/add")
    public ApplicationResponseDTO addPayroll(@RequestBody PayrollDTO payrollDTO) {
        return payrollService.addPayroll(payrollDTO);
    }
    @PutMapping("/update/{id}")
    public ApplicationResponseDTO updatePayroll(@PathVariable("id") Long id,@RequestBody PayrollUpdateDTO payrollUpdateDTO) {
        return payrollService.updatePayroll(id,payrollUpdateDTO);
    }
    @DeleteMapping("/delete/{id}")
    public ApplicationResponseDTO deletePayroll(@PathVariable("id") Long id) {
        return payrollService.deletePayroll(id);
    }
    @GetMapping("/get")
    public List<Payroll> getAllPayrolls() {
        return payrollService.getAllPayrolls();
    }
    @GetMapping("/get/{id}")
    public Payroll getPayrollById(@PathVariable Long id) {
        return payrollService.getPayrollById(id);
    }
    @GetMapping("/get/user/{user-id}")
    public List<Payroll> getPayrollByEmployeeId(@PathVariable("user-id") Long employeeId) {
        return payrollService.getPayrollByEmployeeId(employeeId);
    }
    @GetMapping("/get/date/{date}")
    public List<Payroll> getPayrollByPayDate(@PathVariable("date") LocalDate payDate) {
        return payrollService.getPayrollByPayDate(payDate);
    }
    @GetMapping("/get/{user-id}/{date}")
    public List<Payroll> getUserPayrollByDate(@PathVariable("user-id") Long employeeId,@PathVariable("date") LocalDate payDate) {
        return payrollService.getUserPayrollByDate(employeeId,payDate);
    }


    @GetMapping("/get-own")
    public List<Payroll> getOwnPayrolls() {
        return payrollService.getOwnPayrolls();
    }
    @GetMapping("/get-own/{id}")
    public Payroll getOwnPayrollById(@PathVariable("id") Long employeeId) {
        return payrollService.getOwnPayrollById(employeeId);
    }
    @GetMapping("/get-own/date/{date}")
    public List<Payroll> getOwnPayrollByDate(@PathVariable("date") LocalDate date) {
        return payrollService.getOwnPayrollByDate(date);
    }

}
