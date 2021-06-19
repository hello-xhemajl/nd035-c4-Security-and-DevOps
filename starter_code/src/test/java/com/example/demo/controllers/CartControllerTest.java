package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {


    private CartController cartController;

    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
    }

    @Test
    public void can_add_to_cart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setUsername("Alice");
        request.setQuantity(4);

        Cart cart = new Cart();
        User user = new User();

        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        user.setCart(cart);
        user.setId(100L);
        user.setUsername(request.getUsername());

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("broken glasses");
        item.setDescription("a pair of broken glasses");
        item.setPrice(new BigDecimal(120.00));

        when(itemRepository.findById(request.getItemId()))
                .thenReturn(Optional.of(item));

        when(cartRepository.save(cart)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getItems().size(), is(request.getQuantity()));
    }

    @Test
    public void can_remove_from_cart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setUsername("Alice");
        request.setQuantity(4);

        Cart cart = new Cart();
        User user = new User();

        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        user.setCart(cart);
        user.setId(100L);
        user.setUsername(request.getUsername());

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("broken glasses");
        item.setDescription("a pair of broken glasses");
        item.setPrice(new BigDecimal(120.00));

        when(itemRepository.findById(request.getItemId()))
                .thenReturn(Optional.of(item));

        when(cartRepository.save(cart)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getItems().size(), is(0));
    }

    @Test
    public void can_not_add_to_cart_for_non_existant_user() {
        when(userRepository.findByUsername("nonExistantUsername"))
                .thenReturn(null);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("nonExistantUsername");
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void can_not_add_to_cart_for_non_existant_item() {
        Long nonExistingItemId = 1L;
        when(userRepository.findByUsername("username"))
                .thenReturn(new User());

        when(itemRepository.findById(nonExistingItemId))
                .thenReturn(Optional.empty());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(nonExistingItemId);


        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }


}
