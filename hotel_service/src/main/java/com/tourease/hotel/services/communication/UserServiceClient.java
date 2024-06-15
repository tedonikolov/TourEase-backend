package com.tourease.hotel.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.configuration.exception.InternalServiceException;
import com.tourease.hotel.models.dto.requests.WorkerRegisterVO;
import com.tourease.hotel.models.enums.WorkerType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class UserServiceClient {
    private final RestTemplate defaultRestTemplate;
    private final EurekaClient eurekaClient;

    private final String userAppName = "USER-SERVICE";
    private final String userServiceUrl = "http://user-service";

    public void checkConnection() {
        Application service = eurekaClient.getApplication(userAppName);
        if (service == null)
            throw new InternalServiceException("No connection to user-service.");
    }

    public Long createWorkerUser(String email, WorkerType workerType) {
        try {
            return defaultRestTemplate.postForObject(userServiceUrl + "/internal/createUserForWorker", new HttpEntity<>(new WorkerRegisterVO(email,workerType)), Long.class);
        }catch (HttpClientErrorException exception){
            throw new CustomException("Email already exists", ErrorCode.AlreadyExists);
        }
    }

    public void fireWorker(Long id) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/fireWorker/"+id,null);
    }

    public void reassignWorker(Long id) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/reassignWorker/"+id,null);
    }

    public void changeUserType(Long id, WorkerType workerType) {
        defaultRestTemplate.postForLocation(userServiceUrl + "/internal/changeUserType/"+id, new HttpEntity<>(workerType));
    }
}
