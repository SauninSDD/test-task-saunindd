package ru.sovkom.backend.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkom.backend.entities.Client;
import ru.sovkom.backend.repositories.ClientRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final CartService cartService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, CartService cartService) {
        this.clientRepository = clientRepository;
        this.cartService = cartService;
    }

    @Override
    public long signUp(Client client) {

        return clientRepository.save(client).getId();
    }

    @Override
    public Optional<Client> getClientById(long clientId) {

        return clientRepository.findById(clientId);
    }

    @Override
    public boolean checkClientExistence(long clientId) {

        return clientRepository.existsById(clientId);
    }

    @Override
    @Transactional
    public boolean deleteClientById(long clientId) {
        if (checkClientExistence(clientId)) {
//            cartService.deleteCart(clientId);
            clientRepository.deleteById(clientId);

            return true;
        }
        return false;
    }

    @Override
    public Optional<Client> getClientByEmail(String email) {

        return clientRepository.findUserByEmail(email);
    }

    @Override
    public boolean updateClientInfo(long clientId, String name, Date dateOfBirth, String number) {
        Optional<Client> optionalUser = clientRepository.findById(clientId);

        if (optionalUser.isPresent()) {
            Client client = optionalUser.get();

            client.setUsername(name);
            client.setNumber(number);

            clientRepository.save(client);

            return true;
        } else {
            throw new EntityNotFoundException("Клиент с id " + clientId + " не найден");
        }
    }

    @Override
    public List<Client> getUsersByUsername(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return clientRepository.findAll();
        } else {
            return clientRepository.findUsersByUsername(stringFilter);
        }
    }



}
