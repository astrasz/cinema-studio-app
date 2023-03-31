package com.cinemastudio.cinemastudioapp.service.impl;

import com.cinemastudio.cinemastudioapp.dto.request.RegisterRequest;
import com.cinemastudio.cinemastudioapp.dto.response.ApiUserResponse;
import com.cinemastudio.cinemastudioapp.exception.InvalidRequestParameterException;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.ApiUser;
import com.cinemastudio.cinemastudioapp.repository.ApiUserRepository;
import com.cinemastudio.cinemastudioapp.service.ApiUserService;
import com.cinemastudio.cinemastudioapp.util.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ApiUserServiceImpl implements ApiUserService {

    private final ApiUserRepository apiUserRepository;

    public ApiUserServiceImpl(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ApiUserResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNr, number, sort);
        Page<ApiUser> users = apiUserRepository.findAll(pageable);

        List<ApiUser> userList = users.getContent();
        return userList.stream().map(this::mapToApiUserResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ApiUserResponse getOneById(String userId) {
        ApiUser apiUser = apiUserRepository.findById(userId).orElseThrow(() -> new ResourceNofFoundException(ApiUser.class.getSimpleName(), "id", userId));
        return mapToApiUserResponse(apiUser);
    }

    @Transactional
    @Override
    public ApiUserResponse create(RegisterRequest userRequest) {
        ApiUser user = saveNewUser(userRequest);
        return mapToApiUserResponse(user);
    }

    @Transactional
    @Override
    public ApiUserResponse updateRole(String userId, String role) {
        checkRequestedRole(role);
        ApiUser user = apiUserRepository.findById(userId).orElseThrow(() -> new ResourceNofFoundException(ApiUser.class.getSimpleName(), "id", userId));
        user.setRole(RoleName.valueOf(role));
        apiUserRepository.save(user);
        return mapToApiUserResponse(user);
    }

    @Transactional
    @Override
    public String remove(String userId) {
        ApiUser user = apiUserRepository.findById(userId).orElseThrow(() -> new ResourceNofFoundException(ApiUser.class.getSimpleName(), "id", userId));
        String username = user.getUsername();
        apiUserRepository.delete(user);
        return String.format("User %s has been removed successfully", username);
    }

    private ApiUserResponse mapToApiUserResponse(ApiUser apiUser) {
        return ApiUserResponse.builder()
                .id(apiUser.getId())
                .firstname(apiUser.getFirstname())
                .lastname(apiUser.getLastname())
                .email(apiUser.getEmail())
                .role(apiUser.getRole().toString())
                .build();
    }

    private ApiUser saveNewUser(RegisterRequest userRequest) {

        ApiUser user = ApiUser.builder()
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();

        apiUserRepository.save(user);

        return user;
    }

    private void checkRequestedRole(String role) {
        if (!Arrays.toString(RoleName.values()).contains(role)) {
            throw new InvalidRequestParameterException("role", role);
        }
    }
}
