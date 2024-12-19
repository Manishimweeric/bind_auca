package com.auction.bid.controller;

import com.auction.bid.model.Client;
import com.auction.bid.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    // Signup API
    @PostMapping("/signup")
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        Client savedClient = clientService.registerClient(client);
        return ResponseEntity.ok(savedClient);
    }

    @GetMapping("/client")
    public ResponseEntity<List<Client>> registerClient() {
        List<Client> savedClient = clientService.listofclient();
        return ResponseEntity.ok(savedClient);
    }



    @PostMapping("/login")
    public ResponseEntity<?> loginClient(@RequestBody Client loginClient) {
        Client authenticatedClient = clientService.login(loginClient.getEmail(), loginClient.getPassword());

        if (authenticatedClient != null) {
            // Return the authenticated client details with a 200 OK status
            return new ResponseEntity<>(authenticatedClient, HttpStatus.OK);
        } else {
            // Return a 401 Unauthorized status with an error message
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }
    }

}
