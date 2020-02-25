package com.tekser.service.business;

import com.tekser.domain.business.Client;
import com.tekser.web.dto.ClientDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientDtoService {

    private ClientService clientService;
    private ModelMapper modelMapper;

    public ClientDtoService(ClientService clientService, ModelMapper modelMapper) {
        this.clientService = clientService;
        this.clientService = clientService;
        this.modelMapper = modelMapper;
    }

    public List<ClientDto> findAll(){
        List<Client> clients = clientService.findAll();
        return clients.stream().map(client -> modelMapper.map(client, ClientDto.class)).collect(Collectors.toList());
    }

    public Page<ClientDto> findAllPageable(Pageable pageable) {
        Page<Client> clients = clientService.findAllPageable(pageable);
        List<ClientDto> clientDtos = clients.stream().map(client -> modelMapper.map(client, ClientDto.class)).collect(Collectors.toList());
        return new PageImpl<>(clientDtos, pageable, clients.getTotalElements());
    }

    public Optional<ClientDto> findById(Long id) {
        Optional<Client> retrievedClient = clientService.findById(id);
        return retrievedClient.map(client -> modelMapper.map(client, ClientDto.class));
    }

    public ClientDto findByEmail(String email){
        return modelMapper.map(clientService.findByEmail(email), ClientDto.class);
    }

    public Page<ClientDto> findByIdPageable(Long id, PageRequest pageRequest) {
        Page<Client> clients = clientService.findByIdPageable(id, pageRequest);
        List<ClientDto> clientDtos = clients.stream().map(client -> modelMapper.map(client, ClientDto.class)).collect(Collectors.toList());
        return new PageImpl<>(clientDtos, pageRequest, clients.getTotalElements());
    }

    public Page<ClientDto> findByNameContaining(String name, PageRequest pageRequest) {
        Page<Client> clients = clientService.findByNameContaining(name, pageRequest);
        List<ClientDto> clientDtos = clients.stream().map(client -> modelMapper.map(client, ClientDto.class)).collect(Collectors.toList());
        return new PageImpl<>(clientDtos, pageRequest, clients.getTotalElements());
    }

    public Page<ClientDto> findBySurnameContaining(String surname, PageRequest pageRequest) {
        Page<Client> clients = clientService.findBySurnameContaining(surname, pageRequest);
        List<ClientDto> clientDtos = clients.stream().map(client -> modelMapper.map(client, ClientDto.class)).collect(Collectors.toList());
        return new PageImpl<>(clientDtos, pageRequest, clients.getTotalElements());
    }

    public Page<ClientDto> findByPhoneContaining(String phone, PageRequest pageRequest) {
        Page<Client> clients = clientService.findByPhoneContaining(phone, pageRequest);
        List<ClientDto> clientDtos = clients.stream().map(client -> modelMapper.map(client, ClientDto.class)).collect(Collectors.toList());
        return new PageImpl<>(clientDtos, pageRequest, clients.getTotalElements());
    }

    public Page<ClientDto> findByEmailContaining(String email, PageRequest pageRequest) {
        Page<Client> clients = clientService.findByEmailContaining(email, pageRequest);
        List<ClientDto> clientDtos = clients.stream().map(client -> modelMapper.map(client, ClientDto.class)).collect(Collectors.toList());
        return new PageImpl<>(clientDtos, pageRequest, clients.getTotalElements());
    }
}
