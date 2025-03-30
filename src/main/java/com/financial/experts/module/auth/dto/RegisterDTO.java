package com.financial.experts.module.auth.dto;

import com.financial.experts.module.service.dto.ServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String description;
    private Integer experienceYears;
    private List<Long> specializations;
    private MultipartFile photo;
    private String city;
    private String street;
    private List<String> clientTypes;
    private List<String> ageGroups;
    private List<ServiceDTO> services;
    private List<WorkingHourDTO> workingHours;
    public void setServices(Map<String, String> serviceMap) {
        List<ServiceDTO> servicesList = new ArrayList<>();
        int index = 0;
        while (serviceMap.containsKey("services[" + index + "].name")) {
            ServiceDTO service = new ServiceDTO();
            service.setName(serviceMap.get("services[" + index + "].name"));
            service.setPrice(Double.parseDouble(serviceMap.get("services[" + index + "].price")));
            servicesList.add(service);
            index++;
        }
        this.services = servicesList;
    }

}