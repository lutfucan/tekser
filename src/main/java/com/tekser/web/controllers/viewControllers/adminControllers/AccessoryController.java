package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.business.Accessory;
import com.tekser.service.business.AccessoryService;
import com.tekser.service.business.BrancheService;
import com.tekser.service.business.BrandService;
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
public class AccessoryController {

    private AccessoryService accessoryService;

    public AccessoryController(AccessoryService accessoryService) {
        this.accessoryService = accessoryService;
    }

    @GetMapping("/accessories")
    public ModelAndView showAccessories() {
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/accessories");
        modelAndView.addObject("accessories", accessoryService.findAll());
        return modelAndView;
    }

    @GetMapping("/accessory/{id}")
    public ModelAndView getEditAccessoryForm(@PathVariable Long id) {
        Optional<Accessory> accessory = accessoryService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/editAccessory");
        modelAndView.addObject("accessory", accessory.get());
        return modelAndView;
    }

    @PostMapping("/accessory/{id}")
    public String updateAccessory(Model model, @PathVariable Long id,
                                  @ModelAttribute("oldAccessory") @Valid final Accessory accessory,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/forms/accessories/edit";
        Optional<Accessory> persistedAccessory = accessoryService.findById(id);
        List<Accessory> allAccessories = accessoryService.findAll();

        boolean accessoryNameAlreadyExists = accessoryService.checkIfAccessoryNameExists(allAccessories, accessory, persistedAccessory.get());
        boolean hasErrors = false;

        if (accessoryNameAlreadyExists) {
            bindingResult.rejectValue("name", "accessoryNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("accessory", accessory);
            model.addAttribute("org.springframework.validation.BindingResult.accessory", bindingResult);
            return formWithErrors;
        }
        accessoryService.save(accessory);
        redirectAttributes.addFlashAttribute("accessoryHasBeenUpdated", true);
        return "redirect:/adminPage/accessories";

    }

    @GetMapping("/accessories/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        accessoryService.deleteById(id);
        return "redirect:/adminPage/accessories";
    }

    @GetMapping("/accessories/newAccessory")
    public String getAddNewAccessoryForm(Model model) {
        model.addAttribute("newAccessory", new Accessory());
        return "adminPage/forms/newAccessory";
    }

    @PostMapping("/accessories/newAccessory")
    public String saveNewAccessory(@ModelAttribute("newAccessory") @Valid final Accessory newAccessory,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Accessory accessoryNameAlreadyExists = accessoryService.findByName(newAccessory.getName());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/forms/accessories";

        if (accessoryNameAlreadyExists != null) {
            bindingResult.rejectValue("name", "accessoryNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        accessoryService.save(newAccessory);
        redirectAttributes.addFlashAttribute("accesoryHasBeenSaved", true);
        return "redirect:/adminPage/accessories";
    }
}
