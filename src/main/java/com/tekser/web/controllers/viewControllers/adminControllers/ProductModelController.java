package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.business.ProductModel;
import com.tekser.service.business.ProductModelService;
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
public class ProductModelController {

    private ProductModelService productModelService;

    public ProductModelController(ProductModelService productModelService) {
        this.productModelService = productModelService;
    }

    @GetMapping("/productModels")
    public ModelAndView showProductModels() {
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/productModels");
        modelAndView.addObject("productModels", productModelService.findAll());
        return modelAndView;
    }

    @GetMapping("/productModel/{id}")
    public ModelAndView getEditProductModelForm(@PathVariable Long id) {
        Optional<ProductModel> productModel = productModelService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/editProductModel");
        modelAndView.addObject("productModel", productModel.get());
        return modelAndView;
    }

    @PostMapping("/productModel/{id}")
    public String updateProductModel(Model model, @PathVariable Long id,
                                  @ModelAttribute("oldProductModel") @Valid final ProductModel productModel,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/forms/productModels/edit";
        Optional<ProductModel> persistedProductModel = productModelService.findById(id);
        List<ProductModel> allProductModels = productModelService.findAll();

        boolean productModelNameAlreadyExists = productModelService.checkIfProductModelNameExists(allProductModels, productModel, persistedProductModel.get());
        boolean hasErrors = false;

        if (productModelNameAlreadyExists) {
            bindingResult.rejectValue("name", "productModelNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("productModel", productModel);
            model.addAttribute("org.springframework.validation.BindingResult.productModel", bindingResult);
            return formWithErrors;
        }
        productModelService.save(productModel);
        redirectAttributes.addFlashAttribute("productModelHasBeenUpdated", true);
        return "redirect:/adminPage/productModels";

    }

    @GetMapping("/productModels/delete/{id}")
    public String deleteProductModel(@PathVariable Long id) {
        productModelService.deleteById(id);
        return "redirect:/adminPage/productModels";
    }

    @GetMapping("/productModels/newProductModel")
    public String getAddNewProductModelForm(Model model) {
        model.addAttribute("newProductModel", new ProductModel());
        return "adminPage/forms/newProductModel";
    }

    @PostMapping("/productModels/newProductModel")
    public String saveNewProductModel(@ModelAttribute("newProductModel") @Valid final ProductModel newProductModel,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ProductModel productModelNameAlreadyExists = productModelService.findByName(newProductModel.getName());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/forms/productModels";

        if (productModelNameAlreadyExists != null) {
            bindingResult.rejectValue("name", "productModelNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        productModelService.save(newProductModel);
        redirectAttributes.addFlashAttribute("productModelHasBeenSaved", true);
        return "redirect:/adminPage/productModels";
    }
}
