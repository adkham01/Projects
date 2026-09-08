package com.url.shortener.controller;

import com.url.shortener.dtos.UrlMappingDto;
import com.url.shortener.models.User;
import com.url.shortener.service.UrlMappingService;
import com.url.shortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlMappingController {
    private final UrlMappingService service;
    private final UserService userService;

    @PostMapping("/api/urls/short")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDto> createShortUrl(@RequestBody Map<String, String> request,
                                                        Principal principal){
        // TODO: get name for original url from application.yaml
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        UrlMappingDto shortUrl = service.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(shortUrl);
    }
    @PostMapping("/api/urls/test")
    public ResponseEntity<String> test(@RequestBody Map<String, String> request){
        String orgUrl = request.get("originalUrl");
        return ResponseEntity.ok(orgUrl);
    }
}
