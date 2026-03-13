package com.backend.billiards_management.services.employee;

import com.backend.billiards_management.dtos.request.CreateEmployeeReq;
import com.backend.billiards_management.dtos.request.UpdateEmployeeReq;
import com.backend.billiards_management.dtos.response.EmployeeRes;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.repositories.EmployeeRepository;
import com.backend.billiards_management.services.keycloak.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final KeycloakUserService keycloakUserService;

    public EmployeeRes getEmployeeById(Integer employeeId) {
        return modelMapper.map(employeeRepository.findById(employeeId).orElse(null), EmployeeRes.class);
    }

    public List<EmployeeRes> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(e -> modelMapper.map(e, EmployeeRes.class))
                .toList();
    }

    public EmployeeRes createEmployee(CreateEmployeeReq req) {

        String keycloakId = keycloakUserService.createUser(
                req.getUsername(),
                req.getEmail(),
                req.getPassword(),
                req.getFirstName(),
                req.getLastName()
        );

        Employee employee = Employee.builder()
                .keycloakId(keycloakId)
                .fistName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .role(req.getRole())
                .isActive(true)
                .build();

        return modelMapper.map(employeeRepository.save(employee), EmployeeRes.class);
    }

    public EmployeeRes updateEmployee(Integer employeeId, UpdateEmployeeReq req) {
        Optional<Employee> employee = Optional.of(employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Cannot find employee with id: " + employeeId)));
        //map bang tu request sang entity
        modelMapper.map(req, employee.get());
        //save entity
        return modelMapper.map(employeeRepository.save(employee.get()), EmployeeRes.class);
    }

    public void deleteEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Cannot find employee with id: " + employeeId));
        employeeRepository.delete(employee);
    }
}
