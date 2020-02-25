package com.tekser.service.searching.receipt;

import com.tekser.service.business.ReceiptDtoService;
import com.tekser.web.paging.InitialPagingSizes;
import com.tekser.web.paging.Pager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class ReceiptSearchErrorResponse {

    private ReceiptDtoService receiptDtoService;

    public ReceiptSearchErrorResponse(ReceiptDtoService receiptDtoService) {
        this.receiptDtoService = receiptDtoService;
    }

    public ModelAndView respondToNumberFormatException(ReceiptSearchResult receiptSearchResult, ModelAndView modelAndView) {
        Pager pager = new Pager(receiptSearchResult.getReceiptPage().getTotalPages(), receiptSearchResult.getReceiptPage().getNumber(),
                                InitialPagingSizes.BUTTONS_TO_SHOW, receiptSearchResult.getReceiptPage().getTotalElements());

        modelAndView.addObject("numberFormatException", true);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("receipts", receiptSearchResult.getReceiptPage());
        modelAndView.setViewName("adminPage/receipt/receipts");
        return modelAndView;
    }

    public ModelAndView respondToEmptySearchResult(ModelAndView modelAndView, PageRequest pageRequest) {
        modelAndView.addObject("noMatches", true);
        modelAndView.addObject("receipts", receiptDtoService.findAllPageable(pageRequest));
        return modelAndView;
    }
}
