package org.softuni.productshop.web.controllers;

import org.modelmapper.ModelMapper;
import org.softuni.productshop.domain.models.binding.UserProfileBindingModel;
import org.softuni.productshop.domain.models.binding.UserRegisterBindingModel;
import org.softuni.productshop.domain.models.service.UserServiceModel;
import org.softuni.productshop.domain.models.view.UsersViewModel;
import org.softuni.productshop.domain.models.view.EditProfileViewModel;
import org.softuni.productshop.domain.models.view.ProfileViewModel;
import org.softuni.productshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register() {
        return super.view("register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel model) {
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            return super.view("register");
        }

        this.userService.registerUser(this.modelMapper.map(model, UserServiceModel.class));

        return super.redirect("/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login() {
        return super.view("login");
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView) {

        UserServiceModel userServiceModel = this.userService.findUserByUserName(principal.getName());

        ProfileViewModel profileViewModel = this.modelMapper.map(userServiceModel, ProfileViewModel.class);

        modelAndView.addObject("view", profileViewModel);

        return super.view("profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView){
        EditProfileViewModel editProfileViewModel = this.modelMapper.map(
                this.userService.findUserByUserName(principal.getName()), EditProfileViewModel.class);

        modelAndView.addObject("view", editProfileViewModel);

        return super.view("edit-profile", modelAndView);
    }

    @PutMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@ModelAttribute UserProfileBindingModel model) {
        if(!model.getPassword().equals(model.getConfirmNewPassword())) {
            return super.redirect("/users/edit");
        }
        if(model.getOldPassword().equals(model.getPassword())) {
            return super.redirect("/users/edit");
        }

        this.userService.editProfile(this.modelMapper.map(model, UserServiceModel.class));

        return super.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<UsersViewModel> users = this.userService.allUsers()
                .stream()
                .map(u -> {
                    UsersViewModel user = this.modelMapper.map(u, UsersViewModel.class);
                    user.setAuthorities(u.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));

                    return user;
                })
                .collect(Collectors.toList());

        modelAndView.addObject("users", users);

        return super.view("all-users", modelAndView);
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUser(@PathVariable String id) {
        this.userService.setUserRole(id, "user");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModerator(@PathVariable String id) {
        this.userService.setUserRole(id, "moderator");

        return super.redirect("/users/all");
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdmin(@PathVariable String id) {
        this.userService.setUserRole(id, "admin");

        return super.redirect("/users/all");
    }

}
