package com.tekser.service.business;

import com.tekser.domain.business.ClientDetail;
import com.tekser.domain.repositories.ClientDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class ClientDetailService {
    private ClientDetailRepository clientDetailRepository;

    public ClientDetailService(ClientDetailRepository clientDetailRepository) {
        this.clientDetailRepository = clientDetailRepository;
    }


    public List<ClientDetail> findAll() {
        return clientDetailRepository.findAll();
    }

    public Page<ClientDetail> findAllPageable(Pageable pageable) {
        return clientDetailRepository.findAll(pageable);
    }

    public ClientDetail findByName(String name) {
        return clientDetailRepository.findByDetailName(name);
    }

    public Optional<ClientDetail> findById(Long id) {
        return clientDetailRepository.findById(id);
    }

    public Page<ClientDetail> findByIdPageable(Long id, Pageable pageRequest){
        Optional<ClientDetail> clientDetail = clientDetailRepository.findById(id);
        List<ClientDetail> clientDetails = clientDetail.isPresent() ? Collections.singletonList(clientDetail.get()) : Collections.emptyList();
        return new PageImpl<>(clientDetails, pageRequest, clientDetails.size());
    }

    public Page<ClientDetail> findByNameContaining (String name, Pageable pageable){
        return clientDetailRepository.findByDetailNameContainingOrderByIdAsc(name, pageable);
    }


    @Transactional
    public void save(ClientDetail clientDetail) {
        clientDetailRepository.save(clientDetail);
    }

    public void deleteById(Long id) {
        clientDetailRepository.deleteById(id);
    }


    public boolean checkIfClientDetailNameExists(List<ClientDetail> allClientDetails, ClientDetail clientDetail, ClientDetail persistedClientDetail) {
        boolean clientDetailNameAlreadyExists = false;
        for (ClientDetail clientDetail1 : allClientDetails) {
            //Check if the clientDetail name is edited and if it is taken
            if (!clientDetail.getDetailName().equals(persistedClientDetail.getDetailName())
                    && clientDetail.getDetailName().equals(clientDetail1.getDetailName())) {
                clientDetailNameAlreadyExists = true;
            }
        }
        return clientDetailNameAlreadyExists;
    }


}
