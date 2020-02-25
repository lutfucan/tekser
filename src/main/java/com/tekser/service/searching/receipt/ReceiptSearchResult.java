package com.tekser.service.searching.receipt;

import com.tekser.web.dto.ReceiptDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptSearchResult {
    private Page<ReceiptDto> receiptPage;
    private boolean numberFormatException;
}
