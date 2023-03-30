package com.cinemastudio.cinemastudioapp.service.impl;

import com.cinemastudio.cinemastudioapp.dto.ApiRoleDto;
import com.cinemastudio.cinemastudioapp.dto.AddApiUserRequest;
import com.cinemastudio.cinemastudioapp.dto.ApiUserResponse;
import com.cinemastudio.cinemastudioapp.exception.InvalidRequestParameterException;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.ApiRole;
import com.cinemastudio.cinemastudioapp.model.ApiUser;
import com.cinemastudio.cinemastudioapp.repository.ApiUserRepository;
import com.cinemastudio.cinemastudioapp.service.ApiUserService;
import com.cinemastudio.cinemastudioapp.util.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ApiUserServiceImpl implements ApiUserService {

    private final ApiUserRepository apiUserRepository;

    public ApiUserServiceImpl(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Override
    public List<ApiUserResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNr, number, sort);
        Page<ApiUser> users = apiUserRepository.findAll(pageable);

        List<ApiUser> userList = users.getContent();
        return userList.stream().map(this::mapToApiUserResponse).toList();
    }

    @Override
    public ApiUserResponse getOneById(String userId) {
        ApiUser apiUser = apiUserRepository.findById(userId).orElseThrow(() -> new ResourceNofFoundException(ApiUser.class.getSimpleName(), "id", userId));
        return mapToApiUserResponse(apiUser);
    }

    @Override
    public ApiUserResponse create(AddApiUserRequest userRequest) {
        checkRequestedRoles(userRequest.getRoles());
        ApiUser user = saveNewUser(userRequest);
        return mapToApiUserResponse(user);
    }

    @Override
    public ApiUserResponse updateRoles(String userId, List<String> roles) {
        checkRequestedRoles(roles);
        ApiUser user = apiUserRepository.findById(userId).orElseThrow(() -> new ResourceNofFoundException(ApiUser.class.getSimpleName(), "id", userId));
        user.setRoles(roles.stream().map(roleName -> ApiRole.builder().name(roleName).build()).toList());
        apiUserRepository.save(user);
        return mapToApiUserResponse(user);
    }

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
                .name(apiUser.getName())
                .username(apiUser.getUsername())
                .roles(apiUser.getRoles().stream().map(apiRole -> ApiRoleDto.builder().name(apiRole.getName()).build()).toList())
                .build();
    }


    private void checkRequestedRoles(List<String> roles) {
        for (String roleName : roles) {
            if (!Arrays.toString(RoleName.values()).contains(roleName)) {
                throw new InvalidRequestParameterException(roles.getClass().getSimpleName(), roles.toString());
            }
        }
    }

    private ApiUser saveNewUser(AddApiUserRequest userRequest) {

        ApiUser user = ApiUser.builder()
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .roles(userRequest.getRoles().stream().map(roleName -> ApiRole.builder().name(roleName).build()).toList())
                .build();

        apiUserRepository.save(user);

        return user;
    }
}
