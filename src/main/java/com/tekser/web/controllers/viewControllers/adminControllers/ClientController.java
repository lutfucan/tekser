package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.domain.business.Client;
import com.tekser.service.business.ClientDetailService;
import com.tekser.service.business.ClientDtoService;
import com.tekser.service.business.ClientService;
import com.tekser.service.business.ClientUpdateDtoService;
import com.tekser.service.searching.client.ClientFinder;
import com.tekser.service.searching.client.ClientSearchErrorResponse;
import com.tekser.service.searching.client.ClientSearchParameters;
import com.tekser.service.searching.client.ClientSearchResult;
import com.tekser.web.dto.ClientDto;
import com.tekser.web.dto.ClientUpdateDto;
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
import java.util.Optional;

@Controller
@RequestMapping("/adminPage")
public class ClientController {
    private ClientService clientService;
    private ClientUpdateDtoService clientUpdateDtoService;
    private ClientDtoService clientDtoService;
    private ClientDetailService clientDetailService;
    private ClientFinder clientFinder;
    private ClientSearchErrorResponse clientSearchErrorResponse;

    public ClientController(ClientService clientService, ClientUpdateDtoService clientUpdateDtoService,
                            ClientDtoService clientDtoService, ClientDetailService clientDetailService, ClientFinder clientFinder,
                            ClientSearchErrorResponse clientSearchErrorResponse) {
        this.clientService = clientService;
        this.clientUpdateDtoService = clientUpdateDtoService;
        this.clientDtoService = clientDtoService;
        this.clientDetailService = clientDetailService;
        this.clientFinder = clientFinder;
        this.clientSearchErrorResponse = clientSearchErrorResponse;
    }

    /*
     * Get all clients or search clients if there are searching parameters
     */
    @GetMapping("/clients")
    public ModelAndView getClients(ModelAndView modelAndView, ClientSearchParameters clientSearchParameters) {
        int selectedPageSize = clientSearchParameters.getPageSize().orElse(InitialPagingSizes.INITIAL_PAGE_SIZE);
        int selectedPage = (clientSearchParameters.getPage().orElse(0) < 1) ? InitialPagingSizes.INITIAL_PAGE :
                (clientSearchParameters.getPage().get() - 1);

        PageRequest pageRequest = PageRequest.of(selectedPage, selectedPageSize, Sort.by(Sort.Direction.ASC, "id"));
        ClientSearchResult clientSearchResult = new ClientSearchResult();

        //Empty search parameters
        if (!clientSearchParameters.getPropertyValue().isPresent() || clientSearchParameters.getPropertyValue().get().isEmpty())
            clientSearchResult.setClientPage(clientDtoService.findAllPageable(pageRequest));

            //Search queries
        else {
            clientSearchResult = clientFinder.searchClientsByProperty(pageRequest, clientSearchParameters);

            if (clientSearchResult.isNumberFormatException())
                return clientSearchErrorResponse.respondToNumberFormatException(clientSearchResult, modelAndView);

            if (clientSearchResult.getClientPage().getTotalElements() == 0) {
                modelAndView = clientSearchErrorResponse.respondToEmptySearchResult(modelAndView, pageRequest);
                clientSearchResult.setClientPage(clientDtoService.findAllPageable(pageRequest));
            }
            modelAndView.addObject("clientsProperty", clientSearchParameters.getClientsProperty().get());
            modelAndView.addObject("propertyValue", clientSearchParameters.getPropertyValue().get());
        }

        Pager pager = new Pager(clientSearchResult.getClientPage().getTotalPages(),
                clientSearchResult.getClientPage().getNumber(),
                InitialPagingSizes.BUTTONS_TO_SHOW,
                clientSearchResult.getClientPage().getTotalElements());
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("clients", clientSearchResult.getClientPage());
        modelAndView.addObject("selectedPageSize", selectedPageSize);
        modelAndView.addObject("pageSizes", InitialPagingSizes.PAGE_SIZES);
        modelAndView.setViewName("adminPage/op/clients");
        return modelAndView;
    }

    @GetMapping("/clients/{id}")
    public String getEditClientForm(@PathVariable Long id, Model model) {
        ClientUpdateDto clientUpdateDto = clientUpdateDtoService.findById(id);
        model.addAttribute("clientUpdateDto", clientUpdateDto);
        model.addAttribute("clientDetailList", clientDetailService.findAll());
        return "adminPage/op/editClient";
    }

    @PostMapping("/clients/{id}")
    public String updateClient(Model model, @PathVariable Long id, @ModelAttribute("oldClient") @Valid ClientUpdateDto clientUpdateDto,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/op/editClient";
        Optional<Client> persistedClient = clientService.findById(id);

        Client emailAlreadyExists = clientService.findByEmailAndIdNot(clientUpdateDto.getEmail(), id);
        boolean hasErrors = false;

        hasErrors = checkNewClient(bindingResult, emailAlreadyExists, hasErrors);

        if (hasErrors) {
            model.addAttribute("clientUpdateDto", clientUpdateDto);
            model.addAttribute("org.springframework.validation.BindingResult.clientUpdateDto", bindingResult);
            return formWithErrors;
        } else {
            clientService.save(clientService.getUpdatedClient(persistedClient.get(), clientUpdateDto));
            redirectAttributes.addFlashAttribute("clientHasBeenUpdated", true);
            return "redirect:/adminPage/clients";
        }
    }

    @GetMapping("/clients/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        clientService.deleteById(id);
        return "redirect:/adminPage/clients";
    }

    @GetMapping("/clients/newClient")
    public String getAddNewClientForm(Model model) {
        model.addAttribute("newClient", new ClientDto());
        model.addAttribute("clientDetailList", clientDetailService.findAll());
        return "adminPage/op/newClient";
    }

    @PostMapping("/clients/newClient")
    public String saveNewClient(@ModelAttribute("newClient") @Valid ClientDto newClient, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        Client emailAlreadyExists = clientService.findByEmail(newClient.getEmail());
        boolean hasErrors = false;
        String formWithErrors = "adminPage/op/newClient";

        hasErrors = checkNewClient(bindingResult, emailAlreadyExists, hasErrors);

        if (hasErrors) return formWithErrors;

        else {
            Client client = clientService.createNewClient(newClient);

            clientService.save(client);
            redirectAttributes.addFlashAttribute("clientHasBeenSaved", true);
            return "redirect:/adminPage/clients";
        }
    }

    private boolean checkNewClient(BindingResult bindingResult, Client emailAlreadyExists, boolean hasErrors) {
        if (emailAlreadyExists != null) {
            bindingResult.rejectValue("email", "emailAlreadyExists");
            hasErrors = true;
        }

        if (bindingResult.hasErrors()) hasErrors = true;
        return hasErrors;
    }

}
