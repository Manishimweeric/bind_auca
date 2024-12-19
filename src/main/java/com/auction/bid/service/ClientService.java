package com.auction.bid.service;

import com.auction.bid.model.Client;
import com.auction.bid.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Method to register a new client
    public Client registerClient(Client client) {
        // Add password encoding or other security measures here
        return clientRepository.save(client);
    }

    // Method to get a list of all clients
    public List<Client> listofclient() {
        return clientRepository.findAll();
    }

    // Method to login a client by email and password
    public Client login(String email, String password) {
        Optional<Client> clientOpt = clientRepository.findByEmail(email);

        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();

            // Direct password comparison (for learning, not secure for production)
            if (password.equals(client.getPassword())) {
                return client; // Return the authenticated client
            }
        }

        return null; // Return null if authentication fails
    }

    // Method to get a client by ID

    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }
}
