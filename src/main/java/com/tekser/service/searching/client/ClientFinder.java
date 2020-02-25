package com.tekser.service.searching.client;

import com.tekser.service.business.ClientDtoService;
import com.tekser.web.dto.ClientDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Data
@Service
public class ClientFinder {
    private ClientDtoService clientDtoService;

    @Autowired
    public ClientFinder(ClientDtoService clientDtoService) {
        this.clientDtoService = clientDtoService;
    }

    public ClientSearchResult searchClientsByProperty(PageRequest pageRequest, ClientSearchParameters clientSearchParameters) {
        Page<ClientDto> clientDtoPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        switch (clientSearchParameters.getClientsProperty().get()) {
            case "ID":
                try {
                    long id = Long.parseLong(clientSearchParameters.getPropertyValue().get());
                    clientDtoPage = clientDtoService.findByIdPageable(id, pageRequest);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return new ClientSearchResult(clientDtoService.findAllPageable(pageRequest), true);
                }
                break;
            case "İsim":
                clientDtoPage = clientDtoService.findByNameContaining(clientSearchParameters.getPropertyValue().get(), pageRequest);
                break;
            case "Soyadı":
                clientDtoPage = clientDtoService.findBySurnameContaining(clientSearchParameters.getPropertyValue().get(), pageRequest);
                break;
            case "Telefon":
                clientDtoPage = clientDtoService.findByPhoneContaining(clientSearchParameters.getPropertyValue().get(), pageRequest);
                break;
            case "Email":
                clientDtoPage = clientDtoService.findByEmailContaining(clientSearchParameters.getPropertyValue().get(), pageRequest);
                break;
        }
        return new ClientSearchResult(clientDtoPage, false);
    }
}
