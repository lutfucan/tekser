package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.Role;
import com.tekser.domain.User;
import com.tekser.service.RoleService;
import com.tekser.service.UserDtoService;
import com.tekser.service.UserService;
import com.tekser.service.UserUpdateDtoService;
import com.tekser.service.searching.UserFinder;
import com.tekser.service.searching.UserSearchErrorResponse;
import com.tekser.service.searching.UserSearchParameters;
import com.tekser.service.searching.UserSearchResult;
import com.tekser.web.dto.UserDto;
import com.tekser.web.dto.UserUpdateDto;
import com.tekser.web.paging.InitialPagingSizes;
import com.tekser.web.paging.Pager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/adminPage")
public class UsersController {
    private UserService userService;
    private RoleService roleService;
    private UserUpdateDtoService userUpdateDtoService;
    private UserDtoService userDtoService;
    private UserFinder userFinder;
    private UserSearchErrorResponse userSearchErrorResponse;

    public UsersController(UserService userService, RoleService roleService,
                           UserUpdateDtoService userUpdateDtoService, UserDtoService userDtoService,
                           UserFinder userFinder, UserSearchErrorResponse userSearchErrorResponse) {
        this.userService = userService;
        this.roleService = roleService;
        this.userUpdateDtoService = userUpdateDtoService;
        this.userDtoService = userDtoService;
        this.userFinder = userFinder;
        this.userSearchErrorResponse = userSearchErrorResponse;
    }

    /*
     * Get all users or search users if there are searching parameters
     */
    @GetMapping("/users")
    public ModelAndView getUsers(ModelAndView modelAndView, UserSearchParameters userSearchParameters) {
        int selectedPageSize = userSearchParameters.getPageSize().orElse(InitialPagingSizes.INITIAL_PAGE_SIZE);
        int selectedPage = (userSearchParameters.getPage().orElse(0) < 1) ? InitialPagingSizes.INITIAL_PAGE : (userSearchParameters.getPage().get() - 1);

        PageRequest pageRequest = PageRequest.of(selectedPage, selectedPageSize, Sort.by(Sort.Direction.ASC, "id"));
        UserSearchResult userSearchResult = new UserSearchResult();

        //Empty search parameters
        if (!userSearchParameters.getPropertyValue().isPresent() || userSearchParameters.getPropertyValue().get().isEmpty())
            userSearchResult.setUserPage(userDtoService.findAllPageable(pageRequest));

            //Search queries
        else {
            userSearchResult = userFinder.searchUsersByProperty(pageRequest, userSearchParameters);

            if (userSearchResult.isNumberFormatException())
                return userSearchErrorResponse.respondToNumberFormatException(userSearchResult, modelAndView);

            if (userSearchResult.getUserPage().getTotalElements() == 0) {
                modelAndView = userSearchErrorResponse.respondToEmptySearchResult(modelAndView, pageRequest);
                userSearchResult.setUserPage(userDtoService.findAllPageable(pageRequest));
            }
            modelAndView.addObject("usersProperty", userSearchParameters.getUsersProperty().get());
            modelAndView.addObject("propertyValue", userSearchParameters.getPropertyValue().get());
        }

        Pager pager = new Pager(userSearchResult.getUserPage().getTotalPages(),
                userSearchResult.getUserPage().getNumber(),
                InitialPagingSizes.BUTTONS_TO_SHOW,
                userSearchResult.getUserPage().getTotalElements());
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("users", userSearchResult.getUserPage());
        modelAndView.addObject("selectedPageSize", selectedPageSize);
        modelAndView.addObject("pageSizes", InitialPagingSizes.PAGE_SIZES);
        modelAndView.setViewName("adminPage/user/users");
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public String getEditUserForm(@PathVariable Long id, Model model) {
        UserUpdateDto userUpdateDto = userUpdateDtoService.findById(id);
        List<Role> allRoles = roleService.findAll();

        userUpdateDto.setRoles(userService.getAssignedRolesList(userUpdateDto));

        model.addAttribute("userUpdateDto", userUpdateDto);
        model.addAttribute("allRoles", allRoles);
        return "adminPage/user/editUser";
    }

    @PostMapping("/users/{id}")
    public String updateUser(Model model, @PathVariable Long id, @ModelAttribute("oldUser") @Valid UserUpdateDto userUpdateDto,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/user/editUser";
        Optional<User> persistedUser = userService.findById(id);
        List<Role> allRoles = roleService.findAll();

        User emailAlreadyExists = userService.findByEmailAndIdNot(userUpdateDto.getEmail(), id);
        User usernameAlreadyExists = userService.findByUsernameAndIdNot(userUpdateDto.getUsername(), id);
        boolean hasErrors = false;

        hasErrors = checkNewUser(bindingResult, emailAlreadyExists, usernameAlreadyExists, hasErrors);

        if (hasErrors) {
            model.addAttribute("userUpdateDto", userUpdateDto);
            model.addAttribute("rolesList", allRoles);
            model.addAttribute("org.springframework.validation.BindingResult.userUpdateDto", bindingResult);
            return formWithErrors;
        } else {
            userService.save(userService.getUpdatedUser(persistedUser.get(), userUpdateDto));
            redirectAttributes.addFlashAttribute("userHasBeenUpdated", true);
            return "redirect:/adminPage/users";
        }
    }

    @GetMapping("/users/newUser")
    public String getAddNewUserForm(Model model) {
        model.addAttribute("newUser", new UserDto());
        return "adminPage/user/newUser";
    }

    @PostMapping("/users/newUser")
    public String saveNewUser(@ModelAttribute("newUser") @Valid UserDto newUser,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        User emailAlreadyExists = userService.findByEmail(newUser.getEmail());
        User usernameAlreadyExists = userService.findByUsername(newUser.getUsername());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/user/newUser";

        hasErrors = checkNewUser(bindingResult, emailAlreadyExists, usernameAlreadyExists, hasErrors);

        if (hasErrors) return formWithErrors;

        else {
            User user = userService.createNewAccount(newUser);
            user.setEnabled(true);

            userService.save(user);
            redirectAttributes.addFlashAttribute("userHasBeenSaved", true);
            return "redirect:/adminPage/users";
        }
    }

    private boolean checkNewUser(BindingResult bindingResult, User emailAlreadyExists,
                                 User usernameAlreadyExists, boolean hasErrors) {
        if (emailAlreadyExists != null) {
            bindingResult.rejectValue("email", "emailAlreadyExists");
            hasErrors = true;
        }

        if (usernameAlreadyExists != null) {
            bindingResult.rejectValue("username", "usernameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;
        return hasErrors;
    }

}
