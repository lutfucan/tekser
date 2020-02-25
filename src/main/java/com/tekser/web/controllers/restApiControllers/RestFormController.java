package com.tekser.web.controllers.restApiControllers;

import com.tekser.domain.User;
import com.tekser.domain.business.Client;
import com.tekser.domain.business.Receipt;
import com.tekser.service.UserService;
import com.tekser.service.business.ClientService;
import com.tekser.service.business.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RestFormController {
    private UserService userService;
    private ClientService clientService;
    private ReceiptService receiptService;

    public RestFormController(UserService userService, ClientService clientService, ReceiptService receiptService) {
        this.userService = userService;
        this.clientService = clientService;
        this.receiptService = receiptService;
    }

    @GetMapping("/adminPage/json-users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> allUsers = userService.findAll();

        if (allUsers == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        else if (allUsers.isEmpty()) return new ResponseEntity<>(allUsers, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @DeleteMapping("/adminPage/json-users/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        Optional<User> userToDelete = userService.findById(id);

        if (!userToDelete.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        userService.deleteById(id);
        return new ResponseEntity<>(userToDelete.get(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/adminPage/json-clients")
    public ResponseEntity<List<Client>> getClients() {
        List<Client> allClients = clientService.findAll();

        if (allClients == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        else if (allClients.isEmpty()) return new ResponseEntity<>(allClients, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(allClients, HttpStatus.OK);
    }

    @DeleteMapping("/adminPage/json-clients/delete/{id}")
    public ResponseEntity<Client> deleteClient(@PathVariable Long id) {
        Optional<Client> clientToDelete = clientService.findById(id);

        if (!clientToDelete.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        clientService.deleteById(id);
        return new ResponseEntity<>(clientToDelete.get(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/adminPage/json-receipts")
    public ResponseEntity<List<Receipt>> getReceipts() {
        List<Receipt> allReceipts = receiptService.findAll();

        if (allReceipts == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        else if (allReceipts.isEmpty()) return new ResponseEntity<>(allReceipts, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(allReceipts, HttpStatus.OK);
    }

    @DeleteMapping("/adminPage/json-receipts/delete/{id}")
    public ResponseEntity<Receipt> deleteReceipt(@PathVariable Long id) {
        Optional<Receipt> receiptToDelete = receiptService.findById(id);

        if (!receiptToDelete.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        receiptService.deleteById(id);
        return new ResponseEntity<>(receiptToDelete.get(), HttpStatus.NO_CONTENT);
    }
}