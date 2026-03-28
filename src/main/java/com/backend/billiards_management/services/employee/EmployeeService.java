package com.backend.billiards_management.services.employee;

import com.backend.billiards_management.dtos.request.employee.CreateEmployeeReq;
import com.backend.billiards_management.dtos.request.employee.UpdateEmployeeReq;
import com.backend.billiards_management.dtos.request.employee.UpdateProfileReq;
import com.backend.billiards_management.dtos.response.employee.EmployeeRes;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.EmployeeRepository;
import com.backend.billiards_management.services.keycloak.KeycloakUserService;
import com.backend.billiards_management.services.uploadImage.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final ModelMapper modelMapper;

    private final KeycloakUserService keycloakUserService;

    private final UploadImageService uploadImageService;

    public EmployeeRes getEmployeeById(Integer employeeId) {
        return modelMapper.map(employeeRepository.findById(employeeId).orElse(null), EmployeeRes.class);
    }

    public EmployeeRes getEmployeeByKeycloakId(String keycloakId) {
        Employee e = (Employee) employeeRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Cannot find employee with keycloak id: " + keycloakId));
        return modelMapper.map(e, EmployeeRes.class);
    }

    public Page<EmployeeRes> getAllEmployees(Pageable pageable) {
//        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable)
                .map(e -> modelMapper.map(e, EmployeeRes.class));
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
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .role(req.getRole())
                .isActive(true)
                .build();

        return modelMapper.map(employeeRepository.save(employee), EmployeeRes.class);
    }

    public EmployeeRes updateEmployee( UpdateEmployeeReq req) {
        Employee employee = employeeRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"Cannot find employee with id: " + req.getId()));
//        if(imageFile != null ) {
//            if(employee.getImage() != null) {
//                uploadImageService.deleteImageFromCloudinary(employee.getImage().getPublicId());
//            }
//            employee.setImage(uploadImageService.uploadImageToCloudinary(imageFile));
//        }
        //map bang tu request sang entity
        modelMapper.map(req, employee);
        //save entity
        return modelMapper.map(employeeRepository.save(employee), EmployeeRes.class);
    }

    public EmployeeRes updateEmployeeProfile(UpdateProfileReq req) {
        Employee employee = employeeRepository.findByKeycloakId(getCurrentUserId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"Employee not found"));
        MultipartFile imageFile = req.getImage();
        if(imageFile != null ) {
            if(employee.getImage() != null) {
                uploadImageService.deleteImageFromCloudinary(employee.getImage().getPublicId());
            }
            employee.setImage(uploadImageService.uploadImageToCloudinary(imageFile));
        }
        //map bang tu request sang entity
        modelMapper.map(req, employee);
        //save entity
        return modelMapper.map(employeeRepository.save(employee), EmployeeRes.class);
    }

    public void deleteEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Cannot find employee with id: " + employeeId));
        employeeRepository.delete(employee);
    }


    public EmployeeRes getMyProfile() {
        String keycloakId = getCurrentUserId();

        Employee employee = (Employee) employeeRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,"Employee not found"));

        return modelMapper.map(employee, EmployeeRes.class);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        return jwt.getSubject();
    }
}
