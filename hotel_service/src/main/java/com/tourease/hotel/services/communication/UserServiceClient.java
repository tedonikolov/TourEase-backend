package com.tourease.hotel.services.communication;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.tourease.configuration.exception.InternalServiceException;
import com.tourease.hotel.models.dto.requests.WorkerRegisterVO;
import com.tourease.hotel.models.enums.WorkerType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private final EurekaClient eurekaClient;

    private final String userAppName = "USER-SERVICE";
    private final String userServiceUrl = "http://user-service";

    public void checkConnection() {
        Application service = eurekaClient.getApplication(userAppName);
        if (service == null)
            throw new InternalServiceException("No connection to user-service.");
    }

    public Long createWorkerUser(String email, WorkerType workerType) {
        return restTemplate.postForObject(userServiceUrl + "/internal/createUserForWorker", new HttpEntity<>(new WorkerRegisterVO(email,workerType)), Long.class);
    }

    public void fireWorker(Long id) {
        restTemplate.postForLocation(userServiceUrl + "/internal/fireWorker/"+id,null);
    }

    public void reassignWorker(Long id) {
        restTemplate.postForLocation(userServiceUrl + "/internal/reassignWorker/"+id,null);
    }

    public void changeUserType(Long id, WorkerType workerType) {
        restTemplate.postForLocation(userServiceUrl + "/internal/changeUserType/"+id, new HttpEntity<>(workerType));
    }

}
