package com.tekser.service.business;

import com.tekser.domain.business.Client;
import com.tekser.web.dto.ClientUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ClientUpdateDtoService {

    private ClientService clientService;
    private ClientDtoService clientDtoService;
    private ModelMapper modelMapper;

    public ClientUpdateDtoService(ClientService clientService,
                                  ClientDtoService clientDtoService, ModelMapper modelMapper) {
        this.clientService = clientService;
        this.clientDtoService = clientDtoService;
        this.modelMapper = modelMapper;
    }

    public List<ClientUpdateDto> findAll(){
        List<Client> clientList = clientService.findAllEagerly();
        List<ClientUpdateDto> clientUpdateDtosList = new ArrayList<>();

        for(Client client : clientList){
            clientUpdateDtosList.add(modelMapper.map(client, ClientUpdateDto.class));
        }
        return clientUpdateDtosList;
    }

    public ClientUpdateDto findById(Long id){
        return modelMapper.map(clientService.findByIdEagerly(id), ClientUpdateDto.class);
    }


}
