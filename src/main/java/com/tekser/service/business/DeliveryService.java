package com.tekser.service.business;

import com.tekser.domain.business.Delivery;
import com.tekser.domain.repositories.DeliveryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class DeliveryService {
    private DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }


    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    public Page<Delivery> findAllPageable(Pageable pageable) {
        return deliveryRepository.findAll(pageable);
    }

    public Delivery findByName(String name) {
        return deliveryRepository.findByName(name);
    }

    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }

    public Page<Delivery> findByIdPageable(Long id, Pageable pageRequest){
        Optional<Delivery> delivery = deliveryRepository.findById(id);
        List<Delivery> deliverys = delivery.isPresent() ? Collections.singletonList(delivery.get()) : Collections.emptyList();
        return new PageImpl<>(deliverys, pageRequest, deliverys.size());
    }

    public Page<Delivery> findByNameContaining (String name, Pageable pageable){
        return deliveryRepository.findByNameContainingOrderByIdAsc(name, pageable);
    }


    @Transactional
    public void save(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    public void deleteById(Long id) {
        deliveryRepository.deleteById(id);
    }


    public boolean checkIfDeliveryNameExists(List<Delivery> allDeliverys, Delivery delivery, Delivery persistedDelivery) {
        boolean deliveryNameAlreadyExists = false;
        for (Delivery delivery1 : allDeliverys) {
            //Check if the delivery name is edited and if it is taken
            if (!delivery.getName().equals(persistedDelivery.getName())
                    && delivery.getName().equals(delivery1.getName())) {
                deliveryNameAlreadyExists = true;
            }
        }
        return deliveryNameAlreadyExists;
    }


}
