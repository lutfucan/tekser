package com.tekser.service.searching.client;

import com.tekser.service.business.ClientDtoService;
import com.tekser.web.paging.InitialPagingSizes;
import com.tekser.web.paging.Pager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ClientSearchErrorResponse {

    private ClientDtoService clientDtoService;

    public ClientSearchErrorResponse(ClientDtoService clientDtoService) {
        this.clientDtoService = clientDtoService;
    }

    public ModelAndView respondToNumberFormatException(ClientSearchResult clientSearchResult, ModelAndView modelAndView) {
        Pager pager = new Pager(clientSearchResult.getClientPage().getTotalPages(), clientSearchResult.getClientPage().getNumber(),
                                InitialPagingSizes.BUTTONS_TO_SHOW, clientSearchResult.getClientPage().getTotalElements());

        modelAndView.addObject("numberFormatException", true);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("clients", clientSearchResult.getClientPage());
        modelAndView.setViewName("adminPage/client/clients");
        return modelAndView;
    }

    public ModelAndView respondToEmptySearchResult(ModelAndView modelAndView, PageRequest pageRequest) {
        modelAndView.addObject("noMatches", true);
        modelAndView.addObject("clients", clientDtoService.findAllPageable(pageRequest));
        return modelAndView;
    }
}
