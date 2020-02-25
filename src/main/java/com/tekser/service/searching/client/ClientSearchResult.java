package com.tekser.service.searching.client;

import com.tekser.web.dto.ClientDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientSearchResult {
    private Page<ClientDto> clientPage;
    private boolean numberFormatException;
}
