package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.business.ClientDetail;
import com.tekser.service.business.ClientDetailService;
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
public class ClientDetailController {

    private ClientDetailService clientDetailService;

    public ClientDetailController(ClientDetailService clientDetailService) {
        this.clientDetailService = clientDetailService;
    }

    @GetMapping("/clientDetails")
    public ModelAndView showClientDetails() {
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/clientDetails");
        modelAndView.addObject("clientDetails", clientDetailService.findAll());
        return modelAndView;
    }

    @GetMapping("/clientDetail/{id}")
    public ModelAndView getEditClientDetailForm(@PathVariable Long id) {
        Optional<ClientDetail> clientDetail = clientDetailService.findById(id);
        ModelAndView modelAndView = new ModelAndView("adminPage/forms/editClientDetail");
        modelAndView.addObject("clientDetail", clientDetail.get());
        return modelAndView;
    }

    @PostMapping("/clientDetail/{id}")
    public String updateClientDetail(Model model, @PathVariable Long id,
                                  @ModelAttribute("oldClientDetail") @Valid final ClientDetail clientDetail,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/forms/clientDetails/edit";
        Optional<ClientDetail> persistedClientDetail = clientDetailService.findById(id);
        List<ClientDetail> allClientDetails = clientDetailService.findAll();

        boolean clientDetailNameAlreadyExists = clientDetailService.checkIfClientDetailNameExists(allClientDetails, clientDetail, persistedClientDetail.get());
        boolean hasErrors = false;

        if (clientDetailNameAlreadyExists) {
            bindingResult.rejectValue("name", "clientDetailNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) {
            model.addAttribute("clientDetail", clientDetail);
            model.addAttribute("org.springframework.validation.BindingResult.clientDetail", bindingResult);
            return formWithErrors;
        }
        clientDetailService.save(clientDetail);
        redirectAttributes.addFlashAttribute("clientDetailHasBeenUpdated", true);
        return "redirect:/adminPage/clientDetails";

    }

    @GetMapping("/clientDetails/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        clientDetailService.deleteById(id);
        return "redirect:/adminPage/clientDetails";
    }

    @GetMapping("/clientDetails/newClientDetail")
    public String getAddNewClientDetailForm(Model model) {
        model.addAttribute("newClientDetail", new ClientDetail());
        return "adminPage/forms/newClientDetail";
    }

    @PostMapping("/clientDetails/newClientDetail")
    public String saveNewClientDetail(@ModelAttribute("newClientDetail") @Valid final ClientDetail newClientDetail,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        ClientDetail clientDetailNameAlreadyExists = clientDetailService.findByName(newClientDetail.getDetailName());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/forms/clientDetails";

        if (clientDetailNameAlreadyExists != null) {
            bindingResult.rejectValue("name", "clientDetailNameAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;

        if (hasErrors) return formWithErrors;

        clientDetailService.save(newClientDetail);
        redirectAttributes.addFlashAttribute("clientDetailHasBeenSaved", true);
        return "redirect:/adminPage/clientDetails";
    }
}
