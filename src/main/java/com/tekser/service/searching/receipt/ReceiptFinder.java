package com.tekser.service.searching.receipt;

import com.tekser.service.business.ReceiptDtoService;
import com.tekser.web.dto.ReceiptDto;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Data
@Service
public class ReceiptFinder {
    private ReceiptDtoService receiptDtoService;

    @Autowired
    public ReceiptFinder(ReceiptDtoService receiptDtoService) {
        this.receiptDtoService = receiptDtoService;
    }

    public ReceiptSearchResult searchReceiptsByProperty(PageRequest pageRequest, ReceiptSearchParameters receiptSearchParameters) {
        Page<ReceiptDto> receiptDtoPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        switch (receiptSearchParameters.getReceiptsProperty().get()) {
            case "ID":
                try {
                    long id = Long.parseLong(receiptSearchParameters.getPropertyValue().get());
                    receiptDtoPage = receiptDtoService.findByIdPageable(id, pageRequest);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return new ReceiptSearchResult(receiptDtoService.findAllPageable(pageRequest), true);
                }
                break;
            case "Seri No":
                receiptDtoPage = receiptDtoService.findBySerialNumberContaining(receiptSearchParameters.getPropertyValue().get(), pageRequest);
                break;
            case "Müşteri EMail":
                receiptDtoPage = receiptDtoService.findByClientEmail(receiptSearchParameters.getPropertyValue().get(), pageRequest);
                break;
            case "Müşteri No":
                try {
                    long id = Long.parseLong(receiptSearchParameters.getPropertyValue().get());
                    receiptDtoPage = receiptDtoService.findByClientId(id, pageRequest);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return new ReceiptSearchResult(receiptDtoService.findAllPageable(pageRequest), true);
                }
                break;
        }
        return new ReceiptSearchResult(receiptDtoPage, false);
    }
}
