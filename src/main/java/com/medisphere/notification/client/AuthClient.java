package com.medisphere.notification.client;

import com.medisphere.notification.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "medisphere-auth-service")
public interface AuthClient {

    @GetMapping("/api/v1/auth/email/{msUserId}")
    ResponseEntity<ApiResponse<String>> getEmailByMsUserId(@PathVariable("msUserId") String msUserId);
}
