package com.tekser.service.business;

import com.tekser.domain.business.Client;
import com.tekser.domain.repositories.ClientRepository;
import com.tekser.web.dto.ClientDto;
import com.tekser.web.dto.ClientUpdateDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ClientService {

    private ClientRepository clientRepository;
    private CacheManager cacheManager;

    public ClientService(ClientRepository clientRepository,  CacheManager cacheManager) {
        this.clientRepository = clientRepository;
        this.cacheManager = cacheManager;
    }

    //region find methods
    //==============================================================================================
    @Cacheable(value = "cache.allClients")
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Cacheable(value = "cache.allClientsPageable")
    public Page<Client> findAllPageable(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Cacheable (value = "cache.clientByEmail", key = "#email", unless="#result == null")
    public Client findByEmail(String email) {
        return clientRepository.findByEmailEagerly(email);
    }

    @Cacheable (value = "cache.clientById", key = "#id", unless="#result == null")
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Page<Client> findByIdPageable(Long id, Pageable pageRequest){
        Optional<Client> client = clientRepository.findById(id);
        List<Client> clients = client.isPresent() ? Collections.singletonList(client.get()) : Collections.emptyList();
        return new PageImpl<>(clients, pageRequest, clients.size());
    }

    public Client findByEmailAndIdNot (String email, Long id){
        return clientRepository.findByEmailAndIdNot(email, id);
    }

    //region Find eagerly
    public Client findByIdEagerly (Long id){
        return clientRepository.findByIdEagerly(id);
    }

    @Cacheable(value = "cache.allClientsEagerly")
    public List<Client> findAllEagerly() {
        return clientRepository.findAllEagerly();
    }
    //endregion

    //region Find by containing
    @Cacheable (value = "cache.byNameContaining")
    public Page<Client> findByNameContaining (String name, Pageable pageable){
        return clientRepository.findByNameContainingOrderByIdAsc(name, pageable);
    }

    @Cacheable (value = "cache.bySurnameContaining")
    public Page<Client> findBySurnameContaining(String surname, Pageable pageable) {
        return clientRepository.findBySurnameContainingOrderByIdAsc(surname, pageable);
    }

    @Cacheable (value = "cache.byPhoneContaining")
    public Page<Client> findByPhoneContaining(String phone, Pageable pageable) {
        return clientRepository.findByPhoneContainingOrderByIdAsc(phone, pageable);
    }

    @Cacheable (value = "cache.byEmailContaining")
    public Page<Client> findByEmailContaining(String email, Pageable pageable) {
        return clientRepository.findByEmailContainingOrderByIdAsc(email, pageable);
    }
    //endregion

    //==============================================================================================
    //endregion


    @Transactional
    @CacheEvict(value = {"cache.allClientsPageable", "cache.allClients", "cache.clientByEmail", "cache.clientById",
            "cache.allClientsEagerly", "cache.byNameContaining", "cache.bySurnameContaining",
            "cache.byPhoneContaining", "cache.byEmailContaining"}, allEntries = true)
    public void save(Client client) {
        clientRepository.save(client);
    }

    @CacheEvict(value = {"cache.allClientsPageable", "cache.allClients", "cache.clientByEmail", "cache.clientById",
            "cache.allClientsEagerly", "cache.byNameContaining", "cache.bySurnameContaining",
            "cache.byPhoneContaining", "cache.byEmailContaining"}, allEntries = true)
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public Client createNewClient(ClientDto clientDto) {
        Client client = new Client();
        client.setName(clientDto.getName());
        client.setSurname(clientDto.getSurname());
        client.setEmail(clientDto.getEmail());
        client.setClientDetail(clientDto.getClientDetail());
        client.setAddress(clientDto.getAddress());
        client.setGsm(clientDto.getGsm());
        client.setPhone(clientDto.getPhone());
        client.setNotes(clientDto.getNotes());
        client.setReceiptList(clientDto.getReceiptList());
        return client;
    }

    public Client getUpdatedClient(Client persistedClient, ClientUpdateDto clientUpdateDto) {
        persistedClient.setName(clientUpdateDto.getName());
        persistedClient.setSurname(clientUpdateDto.getSurname());
        persistedClient.setEmail(clientUpdateDto.getEmail());
        persistedClient.setClientDetail(clientUpdateDto.getClientDetail());
        persistedClient.setAddress(clientUpdateDto.getAddress());
        persistedClient.setGsm(clientUpdateDto.getGsm());
        persistedClient.setPhone(clientUpdateDto.getPhone());
        persistedClient.setNotes(clientUpdateDto.getNotes());
        persistedClient.setReceiptList(clientUpdateDto.getReceiptList());
        return persistedClient;
    }


}
