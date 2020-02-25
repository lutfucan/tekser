package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.business.Product;
import com.tekser.service.business.ProductService;
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
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ModelAndView showProducts() {
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/products");
        modelAndView.addObject("products", productService.findAll());
        return modelAndView;
    }

    @GetMapping("/product/{id}")
    public ModelAndView getEditProductForm(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/editProduct");
        modelAndView.addObject("product", product.get());
        return modelAndView;
    }

    @PostMapping("/product/{id}")
    public String updateProduct(Model model, @PathVariable Long id,
                                  @ModelAttribute("oldProduct") @Valid final Product product,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/forms/products/edit";
        Optional<Product> persistedProduct = productService.findById(id);
        List<Product> allProducts = productService.findAll();

        boolean productNameAlreadyExists = productService.checkIfProductNameExists(allProducts, product, persistedProduct.get());
        boolean hasErrors = false;

        if (productNameAlreadyExists) {
            bindingResult.rejectValue("name", "productNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("product", product);
            model.addAttribute("org.springframework.validation.BindingResult.product", bindingResult);
            return formWithErrors;
        }
        productService.save(product);
        redirectAttributes.addFlashAttribute("productHasBeenUpdated", true);
        return "redirect:/adminPage/products";

    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/adminPage/products";
    }

    @GetMapping("/products/newProduct")
    public String getAddNewProductForm(Model model) {
        model.addAttribute("newProduct", new Product());
        return "adminPage/forms/newProduct";
    }

    @PostMapping("/products/newProduct")
    public String saveNewProduct(@ModelAttribute("newProduct") @Valid final Product newProduct,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Product productNameAlreadyExists = productService.findByName(newProduct.getName());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/forms/products";

        if (productNameAlreadyExists != null) {
            bindingResult.rejectValue("name", "productNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        productService.save(newProduct);
        redirectAttributes.addFlashAttribute("productHasBeenSaved", true);
        return "redirect:/adminPage/products";
    }
}
