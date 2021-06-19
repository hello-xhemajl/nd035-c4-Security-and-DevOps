package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void can_find_items() {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void can_find_item() {
        Item item = new Item();
        item.setId(1L);
        item.setName("broken glasses");
        item.setDescription("A pair of broken glasses");
        item.setPrice(new BigDecimal(120.99));

        when(itemRepository.findById(item.getId()))
                .thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(item.getId());

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Item fetchedItem = response.getBody();
        assertThat(fetchedItem.getId(), is(item.getId()));
        assertThat(fetchedItem.getName(), is("broken glasses"));
    }

    @Test
    public void can_find_items_by_name() {
        Item item = new Item();
        item.setId(1L);
        item.setName("broken glasses");
        item.setDescription("A pair of broken glasses");
        item.setPrice(new BigDecimal(120.99));

        when(itemRepository.findByName(item.getName()))
                .thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> itemsByNameResponse = itemController.getItemsByName(item.getName());

        assertThat(itemsByNameResponse.getStatusCode(), is(HttpStatus.OK));
        List<Item> foundItems = itemsByNameResponse.getBody();
        assertThat(foundItems.size(), is(1));
    }

    @Test
    public void not_found_when_no_item_with_name(){
        when(itemRepository.findByName("stone"))
                .thenReturn(Collections.emptyList());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("stone");

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }


}
