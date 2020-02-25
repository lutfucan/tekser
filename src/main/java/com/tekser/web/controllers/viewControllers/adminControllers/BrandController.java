package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.business.Brand;
import com.tekser.service.business.BrandService;
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
public class BrandController {

    private BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    public ModelAndView showBrands() {
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/brands");
        modelAndView.addObject("brands", brandService.findAll());
        return modelAndView;
    }

    @GetMapping("/brand/{id}")
    public ModelAndView getEditBrandForm(@PathVariable Long id) {
        Optional<Brand> brand = brandService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/editBrand");
        modelAndView.addObject("brand", brand.get());
        return modelAndView;
    }

    @PostMapping("/brand/{id}")
    public String updateBrand(Model model, @PathVariable Long id,
                                  @ModelAttribute("oldBrand") @Valid final Brand brand,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/forms/brands/edit";
        Optional<Brand> persistedBrand = brandService.findById(id);
        List<Brand> allBrands = brandService.findAll();

        boolean brandNameAlreadyExists = brandService.checkIfBrandNameExists(allBrands, brand, persistedBrand.get());
        boolean hasErrors = false;

        if (brandNameAlreadyExists) {
            bindingResult.rejectValue("name", "brandNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("brand", brand);
            model.addAttribute("org.springframework.validation.BindingResult.brand", bindingResult);
            return formWithErrors;
        }
        brandService.save(brand);
        redirectAttributes.addFlashAttribute("brandHasBeenUpdated", true);
        return "redirect:/adminPage/brands";

    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        brandService.deleteById(id);
        return "redirect:/adminPage/brands";
    }

    @GetMapping("/brands/newBrand")
    public String getAddNewBrandForm(Model model) {
        model.addAttribute("newBrand", new Brand());
        return "adminPage/forms/newBrand";
    }

    @PostMapping("/brands/newBrand")
    public String saveNewBrand(@ModelAttribute("newBrand") @Valid final Brand newBrand,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Brand brandNameAlreadyExists = brandService.findByName(newBrand.getName());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/forms/brands";

        if (brandNameAlreadyExists != null) {
            bindingResult.rejectValue("name", "brandNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        brandService.save(newBrand);
        redirectAttributes.addFlashAttribute("brandHasBeenSaved", true);
        return "redirect:/adminPage/brands";
    }
}
