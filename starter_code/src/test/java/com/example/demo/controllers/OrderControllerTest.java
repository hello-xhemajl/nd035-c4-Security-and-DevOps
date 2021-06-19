package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void can_get_orders_for_user(){
        User user = new User();
        user.setUsername("username");
        when(userRepository.findByUsername("username"))
                .thenReturn(user);

        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setUser(user);
        when(orderRepository.findByUser(user))
                .thenReturn(Collections.singletonList(userOrder));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        List<UserOrder> fetchedUserOrders = response.getBody();
        assertThat(fetchedUserOrders.size(), is(1));
    }

    @Test
    public void can_submit_order() {
        User user = new User();
        user.setUsername("username");
        when(userRepository.findByUsername("username"))
                .thenReturn(user);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(Collections.emptyList());
        cart.setTotal(BigDecimal.ZERO);

        user.setCart(cart);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        UserOrder userOrder = response.getBody();
        assertThat(userOrder.getUser(), is(user));
    }

    @Test
    public void can_not_submit_order_for_non_existant_username() {
        when(userRepository.findByUsername("nonExistingUserName"))
                .thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("nonExistingUserName");

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
