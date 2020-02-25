package com.tekser.service.business;

import com.tekser.domain.business.Receipt;
import com.tekser.web.dto.ReceiptUpdateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ReceiptUpdateDtoService {

    private ReceiptService receiptService;
    private ReceiptDtoService receiptDtoService;
    private ModelMapper modelMapper;

    public ReceiptUpdateDtoService(ReceiptService receiptService,
                                   ReceiptDtoService receiptDtoService, ModelMapper modelMapper) {
        this.receiptService = receiptService;
        this.receiptDtoService = receiptDtoService;
        this.modelMapper = modelMapper;
    }

    public List<ReceiptUpdateDto> findAll(){
        List<Receipt> receiptList = receiptService.findAllEagerly();
        List<ReceiptUpdateDto> receiptUpdateDtosList = new ArrayList<>();

        for(Receipt receipt : receiptList){
            receiptUpdateDtosList.add(modelMapper.map(receipt, ReceiptUpdateDto.class));
        }
        return receiptUpdateDtosList;
    }

    public ReceiptUpdateDto findById(Long id){
        return modelMapper.map(receiptService.findByIdEagerly(id), ReceiptUpdateDto.class);
    }


}
