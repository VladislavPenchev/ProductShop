package org.softuni.productshop.service;

import org.softuni.productshop.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUserName(String username);

    UserServiceModel editProfile(UserServiceModel editProfileServiceModel);

    List<UserServiceModel> allUsers();

    void setUserRole(String id, String role);

}
