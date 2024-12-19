package com.auction.bid.service;

import com.auction.bid.model.Item;
import com.auction.bid.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }



    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public Optional<Item> getItemByIds(Long id) {
        return itemRepository.findById(id);
    }
    public void updateItem(Long id, Item updatedItem) {
        Optional<Item> existingItemOpt = getItemById(id);
        if (existingItemOpt.isPresent()) {
            Item existingItem = existingItemOpt.get();
            if (updatedItem.getName() != null && !updatedItem.getName().isBlank()) {
                existingItem.setName(updatedItem.getName());
            }
            if (updatedItem.getPrice() != null && updatedItem.getPrice() > 0) {
                existingItem.setPrice(updatedItem.getPrice());
            }
            itemRepository.save(existingItem);
        } else {
            throw new IllegalArgumentException("Item with ID " + id + " not found.");
        }
    }


    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public List<Item> searchItems(String search) {
        return itemRepository.findByNameContainingIgnoreCase(search);
    }
}
