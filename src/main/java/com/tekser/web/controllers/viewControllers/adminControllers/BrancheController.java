package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.business.Branche;
import com.tekser.service.business.BrancheService;
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
public class BrancheController {

    private BrancheService brancheService;

    public BrancheController(BrancheService brancheService) {
        this.brancheService = brancheService;
    }

    @GetMapping("/branches")
    public ModelAndView showBranches() {
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/branches");
        modelAndView.addObject("branches", brancheService.findAll());
        return modelAndView;
    }

    @GetMapping("/branche/{id}")
    public ModelAndView getEditBrancheForm(@PathVariable Long id) {
        Optional<Branche> branche = brancheService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/editBranche");
        modelAndView.addObject("branche", branche.get());
        return modelAndView;
    }

    @PostMapping("/branche/{id}")
    public String updateBranche(Model model, @PathVariable Long id,
                                  @ModelAttribute("oldBranche") @Valid final Branche branche,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/forms/branches/edit";
        Optional<Branche> persistedBranche = brancheService.findById(id);
        List<Branche> allBranches = brancheService.findAll();

        boolean brancheNameAlreadyExists = brancheService.checkIfBrancheNameExists(allBranches, branche, persistedBranche.get());
        boolean hasErrors = false;

        if (brancheNameAlreadyExists) {
            bindingResult.rejectValue("name", "brancheNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("branche", branche);
            model.addAttribute("org.springframework.validation.BindingResult.branche", bindingResult);
            return formWithErrors;
        }
        brancheService.save(branche);
        redirectAttributes.addFlashAttribute("brancheHasBeenUpdated", true);
        return "redirect:/adminPage/branches";

    }

    @GetMapping("/branches/delete/{id}")
    public String deleteBranche(@PathVariable Long id) {
        brancheService.deleteById(id);
        return "redirect:/adminPage/branches";
    }

    @GetMapping("/branches/newBranche")
    public String getAddNewBrancheForm(Model model) {
        model.addAttribute("newBranche", new Branche());
        return "adminPage/forms/newBranche";
    }

    @PostMapping("/branches/newBranche")
    public String saveNewBranche(@ModelAttribute("newBranche") @Valid final Branche newBranche,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Branche brancheNameAlreadyExists = brancheService.findByName(newBranche.getName());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/forms/branches";

        if (brancheNameAlreadyExists != null) {
            bindingResult.rejectValue("name", "brancheNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        brancheService.save(newBranche);
        redirectAttributes.addFlashAttribute("brancheHasBeenSaved", true);
        return "redirect:/adminPage/branches";
    }
}
