package com.tekser.web.controllers.viewControllers.adminControllers;

import com.tekser.configuration.LocalDateTimeFormatter;
import com.tekser.domain.business.Receipt;
import com.tekser.service.business.*;
import com.tekser.service.searching.receipt.ReceiptFinder;
import com.tekser.service.searching.receipt.ReceiptSearchErrorResponse;
import com.tekser.service.searching.receipt.ReceiptSearchParameters;
import com.tekser.service.searching.receipt.ReceiptSearchResult;
import com.tekser.web.dto.ReceiptDto;
import com.tekser.web.dto.ReceiptUpdateDto;
import com.tekser.web.paging.InitialPagingSizes;
import com.tekser.web.paging.Pager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/adminPage")
public class ReceiptController {
    private ReceiptService receiptService;
    private ReceiptUpdateDtoService receiptUpdateDtoService;
    private ReceiptDtoService receiptDtoService;
    private ReceiptFinder receiptFinder;
    private ReceiptSearchErrorResponse receiptSearchErrorResponse;
    private ClientService clientService;
    private DeliveryService deliveryService;
    private BrancheService brancheService;
    private BrandService brandService;
    private AccessoryService accessoryService;
    private ProductService productService;
    private ProductModelService productModelService;
    private LocalDateTimeFormatter localDateTimeFormatter;

    public ReceiptController(ReceiptService receiptService, ReceiptUpdateDtoService receiptUpdateDtoService,
                             ReceiptDtoService receiptDtoService, ReceiptFinder receiptFinder,
                             ReceiptSearchErrorResponse receiptSearchErrorResponse, ClientService clientService,
                             DeliveryService deliveryService, BrancheService brancheService, BrandService brandService,
                             AccessoryService accessoryService, ProductService productService,
                             ProductModelService productModelService,LocalDateTimeFormatter localDateTimeFormatter) {
        this.receiptService = receiptService;
        this.receiptUpdateDtoService = receiptUpdateDtoService;
        this.receiptDtoService = receiptDtoService;
        this.receiptFinder = receiptFinder;
        this.receiptSearchErrorResponse = receiptSearchErrorResponse;
        this.clientService = clientService;
        this.deliveryService = deliveryService;
        this.brancheService = brancheService;
        this.brandService = brandService;
        this.accessoryService = accessoryService;
        this.productService = productService;
        this.productModelService = productModelService;
        this.localDateTimeFormatter = localDateTimeFormatter;
    }

    /*
     * Get all receipts or search receipts if there are searching parameters
     */
    @GetMapping("/receipts")
    public ModelAndView getReceipts(ModelAndView modelAndView, ReceiptSearchParameters receiptSearchParameters) {
        int selectedPageSize = receiptSearchParameters.getPageSize().orElse(InitialPagingSizes.INITIAL_PAGE_SIZE);
        int selectedPage = (receiptSearchParameters.getPage().orElse(0) < 1) ? InitialPagingSizes.INITIAL_PAGE :
                (receiptSearchParameters.getPage().get() - 1);

        PageRequest pageRequest = PageRequest.of(selectedPage, selectedPageSize, Sort.by(Sort.Direction.ASC, "id"));
        ReceiptSearchResult receiptSearchResult = new ReceiptSearchResult();

        //Empty search parameters
        if (!receiptSearchParameters.getPropertyValue().isPresent()
                || receiptSearchParameters.getPropertyValue().get().isEmpty())
            receiptSearchResult.setReceiptPage(receiptDtoService.findAllPageable(pageRequest));

            //Search queries
        else {
            receiptSearchResult = receiptFinder.searchReceiptsByProperty(pageRequest, receiptSearchParameters);

            if (receiptSearchResult.isNumberFormatException())
                return receiptSearchErrorResponse.respondToNumberFormatException(receiptSearchResult, modelAndView);

            if (receiptSearchResult.getReceiptPage().getTotalElements() == 0) {
                modelAndView = receiptSearchErrorResponse.respondToEmptySearchResult(modelAndView, pageRequest);
                receiptSearchResult.setReceiptPage(receiptDtoService.findAllPageable(pageRequest));
            }
            modelAndView.addObject("receiptsProperty", receiptSearchParameters.getReceiptsProperty().get());
            modelAndView.addObject("propertyValue", receiptSearchParameters.getPropertyValue().get());
        }

        Pager pager = new Pager(receiptSearchResult.getReceiptPage().getTotalPages(),
                receiptSearchResult.getReceiptPage().getNumber(),
                InitialPagingSizes.BUTTONS_TO_SHOW,
                receiptSearchResult.getReceiptPage().getTotalElements());
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("receipts", receiptSearchResult.getReceiptPage());
        modelAndView.addObject("selectedPageSize", selectedPageSize);
        modelAndView.addObject("pageSizes", InitialPagingSizes.PAGE_SIZES);
        modelAndView.setViewName("adminPage/op/receipts");
        return modelAndView;
    }

    @GetMapping("/receipts/clientname/{client}")
    public String getReceiptsByClientName(@PathVariable String client, Model model) {
        List<Receipt> receipts = receiptService.findAllByNameContaining(client);
        model.addAttribute("receipts", receipts);
        return "adminPage/op/receipts";
    }

    @GetMapping("/receipts/clientsurname/{client}")
    public String getReceiptsByClientSurname(@PathVariable String client, Model model) {
        List<Receipt> receipts = receiptService.findAllBySurnameContaining(client);
        model.addAttribute("receipts", receipts);
        return "adminPage/op/receipts";
    }

    @GetMapping("/receipts/{id}")
    public String getEditReceiptForm(@PathVariable Long id, Model model) {
        Optional<ReceiptDto> receiptUpdateDto = receiptDtoService.findById(id);

        model.addAttribute("receiptUpdateDto", receiptUpdateDto.get());
        model.addAttribute("clientList", clientService.findAll());
        model.addAttribute("accessoryList", accessoryService.findAll());
        model.addAttribute("brancheList", brancheService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("deliveryList", deliveryService.findAll());
        model.addAttribute("productList", productService.findAll());
        model.addAttribute("productModelList", productModelService.findAll());
        return "adminPage/op/editReceipt";
    }

    @PostMapping("/receipts/{id}")
    public String updateReceipt(Model model, @PathVariable Long id,
                                @ModelAttribute("oldReceipt") @Valid ReceiptDto receiptUpdateDto,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        String formWithErrors = "adminPage/op/editReceipt";
        Optional<Receipt> persistedReceipt = receiptService.findById(id);

        boolean hasErrors = false;

        hasErrors = checkNewReceipt(bindingResult, hasErrors);

        if (hasErrors) {
            model.addAttribute("receiptUpdateDto", receiptUpdateDto);
            model.addAttribute("org.springframework.validation.BindingResult.receiptUpdateDto", bindingResult);
            return formWithErrors;
        } else {
            receiptService.save(receiptService.getUpdatedReceipt(persistedReceipt.get(), receiptUpdateDto));
            redirectAttributes.addFlashAttribute("receiptHasBeenUpdated", true);
            return "redirect:/adminPage/receipts";
        }
    }

    @GetMapping("/receipts/delete/{id}")
    public String deleteReceipt(@PathVariable Long id) {
        receiptService.deleteById(id);
        return "redirect:/adminPage/receipts";
    }

    @GetMapping("/receipts/newReceipt")
    public String getAddNewReceiptForm(Model model) {
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setDateOfDeliverToClient(localDateTimeFormatter.dateTimeToString(LocalDateTime.now().withNano(0)));
        receiptDto.setDateOfEndingRepair(localDateTimeFormatter.dateTimeToString(LocalDateTime.now().withNano(0)));
        receiptDto.setDateOfReceipt(localDateTimeFormatter.dateTimeToString(LocalDateTime.now().withNano(0)));
        receiptDto.setDateOfStartToRepair(localDateTimeFormatter.dateTimeToString(LocalDateTime.now().withNano(0)));
        model.addAttribute("newReceipt", receiptDto);
        model.addAttribute("clientList", clientService.findAll());
        model.addAttribute("accessoryList", accessoryService.findAll());
        model.addAttribute("brancheList", brancheService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("deliveryList", deliveryService.findAll());
        model.addAttribute("productList", productService.findAll());
        model.addAttribute("productModelList", productModelService.findAll());
        return "adminPage/op/newReceipt";
    }

    @PostMapping("/receipts/newReceipt")
    public String saveNewReceipt(@ModelAttribute("newReceipt") @Valid ReceiptDto newReceipt,
                                 BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        boolean hasErrors = false;
        String formWithErrors = "adminPage/op/newReceipt";

        hasErrors = checkNewReceipt(bindingResult, hasErrors);

        if (hasErrors) return formWithErrors;

        else {
            Receipt receipt = receiptService.createNewReceipt(newReceipt);
            receiptService.save(receipt);
            redirectAttributes.addFlashAttribute("receiptHasBeenSaved", true);
            return "redirect:/adminPage/receipts";
        }
    }

    private boolean checkNewReceipt(BindingResult bindingResult, boolean hasErrors) {
        if (bindingResult.hasErrors()) hasErrors = true;
        return hasErrors;
    }

}
