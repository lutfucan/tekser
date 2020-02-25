package com.tekser.service.business;

import com.tekser.configuration.LocalDateTimeFormatter;
import com.tekser.domain.business.Receipt;
import com.tekser.domain.repositories.ReceiptRepository;
import com.tekser.web.dto.ReceiptDto;
import com.tekser.web.dto.ReceiptUpdateDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReceiptService {

    private ReceiptRepository receiptRepository;
    private CacheManager cacheManager;
    private LocalDateTimeFormatter localDateTimeFormatter;

    public ReceiptService(ReceiptRepository receiptRepository, CacheManager cacheManager,
                          LocalDateTimeFormatter localDateTimeFormatter) {
        this.receiptRepository = receiptRepository;
        this.cacheManager = cacheManager;
        this.localDateTimeFormatter = localDateTimeFormatter;
    }

    //region find methods
    //==============================================================================================
    @Cacheable(value = "cache.allReceipts")
    public List<Receipt> findAll() {
        return receiptRepository.findAll();
    }

    @Cacheable(value = "cache.allReceiptsPageable")
    public Page<Receipt> findAllPageable(Pageable pageable) {
        return receiptRepository.findAll(pageable);
    }

    @Cacheable (value = "cache.receiptsByClientEmail", key = "#email", unless="#result == null")
    public Receipt findByClientEmail(String email) {
        return receiptRepository.findByClientEmailEagerly(email);
    }


    @Cacheable (value = "cache.receiptById", key = "#id", unless="#result == null")
    public Optional<Receipt> findById(Long id) {
        return receiptRepository.findById(id);
    }

    public Page<Receipt> findByIdPageable(Long id, Pageable pageRequest){
        Optional<Receipt> Receipt = receiptRepository.findById(id);
        List<Receipt> Receipts = Receipt.isPresent() ? Collections.singletonList(Receipt.get()) : Collections.emptyList();
        return new PageImpl<>(Receipts, pageRequest, Receipts.size());
    }

    public Page<Receipt> findByNameContaining (String name, String surname, Pageable pageable){
        return receiptRepository.findByClientNameOrClientSurnameContainingOrderByIdAsc(name, surname, pageable);
    }

    public List<Receipt> findAllByNameContaining (String name){
        return receiptRepository.findAllByClientNameContaining(name);
    }

    public List<Receipt> findAllBySurnameContaining (String surname){
        return receiptRepository.findAllByClientSurnameContaining(surname);
    }

    public Page<Receipt> findByDateOfReceiptBetweenOrderByIdAsc(LocalDateTime firstDate, LocalDateTime lastDate, Pageable pageable) {
        return receiptRepository.findByDateOfReceiptBetweenOrderByIdAsc(firstDate,lastDate, pageable);
    }

    public Page<Receipt> findByProductNameContainingOrderByIdAsc(String productName, Pageable pageable) {
        return receiptRepository.findByProductNameContainingOrderByIdAsc(productName, pageable);
    }

    public Page<Receipt> findBySerialNumberContainingOrderByIdAsc(String serial, Pageable pageable) {
        return receiptRepository.findBySerialNumberContainingOrderByIdAsc(serial, pageable);
    }

    public Receipt findByIdEagerly (Long id){
        return receiptRepository.findByIdEagerly(id);
    }


    @Cacheable(value = "cache.allReceiptsEagerly")
    public List<Receipt> findAllEagerly() {
        return receiptRepository.findAllEagerly();
    }
    //endregion

    //region Find by containing
    @Cacheable (value = "cache.byClientEmailContaining")
    public Page<Receipt> findByClientEmailContaining (String email, Pageable pageable){
        return receiptRepository.findByClientEmailContainingOrderById(email, pageable);
    }

    @Cacheable (value = "cache.byClientId")
    public Page<Receipt> findByClientId(Long id, Pageable pageable) {
        return receiptRepository.findByClientIdOrderByIdAsc(id, pageable);
    }

    @Cacheable (value = "cache.bySerialNumber")
    public Page<Receipt> findBySerialNumber(String serial, Pageable pageable) {
        return receiptRepository.findBySerialNumberContainingOrderByIdAsc(serial, pageable);
    }

    @Cacheable (value = "cache.byId")
    public Page<Receipt> findById(Long id, Pageable pageable) {
        return receiptRepository.findByIdContainingOrderByIdAsc(id, pageable);
    }
    //endregion

    //==============================================================================================
    //endregion


    @Transactional
    @CacheEvict(value = {"cache.allReceiptsPageable", "cache.allReceipts", "cache.receiptByClientEmail", "cache.reciptByClientId",
            "cache.allReceiptsEagerly", "cache.bySerialNumberContaining", "cache.byId"}, allEntries = true)
    public void save(Receipt receipt) {
        receiptRepository.save(receipt);
    }

    @CacheEvict(value = {"cache.allReceiptsPageable", "cache.allReceipts", "cache.receiptByClientEmail", "cache.reciptByClientId",
            "cache.allReceiptsEagerly", "cache.bySerialNumberContaining", "cache.byId"}, allEntries = true)
    public void deleteById(Long id) {
        receiptRepository.deleteById(id);
    }

    public Receipt createNewReceipt(ReceiptDto receiptDto) {
        Receipt receipt = new Receipt();

        receipt.setAccessory(receiptDto.getAccessory());
        receipt.setBranche(receiptDto.getBranche());
        receipt.setBrand(receiptDto.getBrand());
        receipt.setClient(receiptDto.getClient());
        receipt.setDateOfDeliverToClient(LocalDateTime.parse(receiptDto.getDateOfDeliverToClient()));
        receipt.setDateOfEndingRepair(LocalDateTime.parse(receiptDto.getDateOfEndingRepair()));
        receipt.setDateOfReceipt(LocalDateTime.parse(receiptDto.getDateOfReceipt()));
        receipt.setDateOfStartToRepair(LocalDateTime.parse(receiptDto.getDateOfStartToRepair()));
        receipt.setDelivery(receiptDto.getDelivery());
        receipt.setHasBackup(receiptDto.isHasBackup());
        receipt.setProblem(receiptDto.getProblem());
        receipt.setProduct(receiptDto.getProduct());
        receipt.setProductModel(receiptDto.getProductModel());
        receipt.setSerialNumber(receiptDto.getSerialNumber());
        receipt.setUnderWarranty(receiptDto.isUnderWarranty());
        return receipt;
    }

    public Receipt getUpdatedReceipt(Receipt receipt, ReceiptDto receiptUpdateDto) {

        receipt.setAccessory(receiptUpdateDto.getAccessory());
        receipt.setBranche(receiptUpdateDto.getBranche());
        receipt.setBrand(receiptUpdateDto.getBrand());
        receipt.setClient(receiptUpdateDto.getClient());
        receipt.setDateOfDeliverToClient(localDateTimeFormatter.stringToDateTime(receiptUpdateDto.getDateOfDeliverToClient()));
        receipt.setDateOfEndingRepair(localDateTimeFormatter.stringToDateTime(receiptUpdateDto.getDateOfEndingRepair()));
        receipt.setDateOfReceipt(localDateTimeFormatter.stringToDateTime(receiptUpdateDto.getDateOfReceipt()));
        receipt.setDateOfStartToRepair(localDateTimeFormatter.stringToDateTime(receiptUpdateDto.getDateOfStartToRepair()));
        receipt.setDelivery(receiptUpdateDto.getDelivery());
        receipt.setHasBackup(receiptUpdateDto.isHasBackup());
        receipt.setProblem(receiptUpdateDto.getProblem());
        receipt.setProduct(receiptUpdateDto.getProduct());
        receipt.setProductModel(receiptUpdateDto.getProductModel());
        receipt.setSerialNumber(receiptUpdateDto.getSerialNumber());
        receipt.setUnderWarranty(receiptUpdateDto.isUnderWarranty());
        return receipt;
    }

}
