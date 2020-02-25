package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.business.Delivery;
import com.tekser.service.business.DeliveryService;
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
public class DeliveryController {

    private DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/deliverys")
    public ModelAndView showDeliverys() {
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/deliveries");
        modelAndView.addObject("deliverys", deliveryService.findAll());
        return modelAndView;
    }

    @GetMapping("/delivery/{id}")
    public ModelAndView getEditDeliveryForm(@PathVariable Long id) {
        Optional<Delivery> delivery = deliveryService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/editDelivery");
        modelAndView.addObject("delivery", delivery.get());
        return modelAndView;
    }

    @PostMapping("/delivery/{id}")
    public String updateDelivery(Model model, @PathVariable Long id,
                                  @ModelAttribute("oldDelivery") @Valid final Delivery delivery,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/forms/deliveries/edit";
        Optional<Delivery> persistedDelivery = deliveryService.findById(id);
        List<Delivery> allDeliverys = deliveryService.findAll();

        boolean deliveryNameAlreadyExists = deliveryService.checkIfDeliveryNameExists(allDeliverys, delivery, persistedDelivery.get());
        boolean hasErrors = false;

        if (deliveryNameAlreadyExists) {
            bindingResult.rejectValue("name", "deliveryNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("delivery", delivery);
            model.addAttribute("org.springframework.validation.BindingResult.delivery", bindingResult);
            return formWithErrors;
        }
        deliveryService.save(delivery);
        redirectAttributes.addFlashAttribute("deliveryHasBeenUpdated", true);
        return "redirect:/adminPage/deliverys";

    }

    @GetMapping("/deliverys/delete/{id}")
    public String deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteById(id);
        return "redirect:/adminPage/deliverys";
    }

    @GetMapping("/deliverys/newDelivery")
    public String getAddNewDeliveryForm(Model model) {
        model.addAttribute("newDelivery", new Delivery());
        return "adminPage/forms/newDelivery";
    }

    @PostMapping("/deliverys/newDelivery")
    public String saveNewDelivery(@ModelAttribute("newDelivery") @Valid final Delivery newDelivery,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        Delivery deliveryNameAlreadyExists = deliveryService.findByName(newDelivery.getName());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/forms/deliveries";

        if (deliveryNameAlreadyExists != null) {
            bindingResult.rejectValue("name", "deliveryNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        deliveryService.save(newDelivery);
        redirectAttributes.addFlashAttribute("deliveryHasBeenSaved", true);
        return "redirect:/adminPage/deliverys";
    }
}
