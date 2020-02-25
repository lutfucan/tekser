package com.tekser.service.business;

import com.tekser.domain.business.Receipt;
import com.tekser.web.dto.ReceiptDto;
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
public class ReceiptDtoService {

    private ReceiptService receiptService;
    private ModelMapper modelMapper;

    public ReceiptDtoService(ReceiptService receiptService, ModelMapper modelMapper) {
        this.receiptService = receiptService;
        this.receiptService = receiptService;
        this.modelMapper = modelMapper;
    }

    public List<ReceiptDto> findAll(){
        List<Receipt> receipts = receiptService.findAll();
        return receipts.stream().map(receipt -> modelMapper.map(receipt, ReceiptDto.class)).collect(Collectors.toList());
    }

    public Page<ReceiptDto> findAllPageable(Pageable pageable) {
        Page<Receipt> receipts = receiptService.findAllPageable(pageable);
        List<ReceiptDto> receiptDtos = receipts.stream().map(receipt -> modelMapper.map(receipt, ReceiptDto.class)).collect(Collectors.toList());
        return new PageImpl<>(receiptDtos, pageable, receipts.getTotalElements());
    }

    public Optional<ReceiptDto> findById(Long id) {
        Optional<Receipt> retrievedReceipt = receiptService.findById(id);
        return retrievedReceipt.map(receipt -> modelMapper.map(receipt, ReceiptDto.class));
    }

    public Page<ReceiptDto> findByClientEmail(String email, PageRequest pageRequest) {
        Page<Receipt> receipts = receiptService.findByClientEmailContaining(email, pageRequest);
        List<ReceiptDto> receiptDtos = receipts.stream().map(receipt -> modelMapper.map(receipt, ReceiptDto.class)).collect(Collectors.toList());
        return new PageImpl<>(receiptDtos, pageRequest, receipts.getTotalElements());
    }

    public Page<ReceiptDto> findByIdPageable(Long id, PageRequest pageRequest) {
        Page<Receipt> receipts = receiptService.findByIdPageable(id, pageRequest);
        List<ReceiptDto> receiptDtos = receipts.stream().map(receipt -> modelMapper.map(receipt, ReceiptDto.class)).collect(Collectors.toList());
        return new PageImpl<>(receiptDtos, pageRequest, receipts.getTotalElements());
    }

    public Page<ReceiptDto> findBySerialNumberContaining(String serial, PageRequest pageRequest) {
        Page<Receipt> receipts = receiptService.findBySerialNumber(serial, pageRequest);
        List<ReceiptDto> receiptDtos = receipts.stream().map(receipt -> modelMapper.map(receipt, ReceiptDto.class)).collect(Collectors.toList());
        return new PageImpl<>(receiptDtos, pageRequest, receipts.getTotalElements());
    }

    public Page<ReceiptDto> findByClientId(Long id, PageRequest pageRequest) {
        Page<Receipt> receipts = receiptService.findByClientId(id, pageRequest);
        List<ReceiptDto> receiptDtos = receipts.stream().map(receipt -> modelMapper.map(receipt, ReceiptDto.class)).collect(Collectors.toList());
        return new PageImpl<>(receiptDtos, pageRequest, receipts.getTotalElements());
    }

}
