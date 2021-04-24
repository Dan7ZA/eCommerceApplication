package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);


    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOrder() throws Exception {
        User mockUser = createMockUser();
        UserOrder mockOrder = createUserOrder(mockUser);

        when(userRepo.findByUsername(mockUser.getUsername())).thenReturn(mockUser);
        when(orderRepo.save(mockOrder)).thenReturn(mockOrder);

        final ResponseEntity<UserOrder> responseEntity = orderController.submit(mockUser.getUsername());

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        UserOrder userOrder = responseEntity.getBody();
        assertNotNull(userOrder);
        assertEquals(mockOrder.getUser(),userOrder.getUser());
        assertEquals(mockOrder.getItems(), userOrder.getItems());
        assertEquals(mockOrder.getTotal(), userOrder.getTotal());
    }

    @Test
    public void getOrdersForUser() throws Exception {

        User mockUser = createMockUser();
        List<UserOrder> mockOrderList = new ArrayList<>();
        mockOrderList.add(createUserOrder(mockUser));
        mockOrderList.add(createUserOrder(mockUser));
        mockOrderList.add(createUserOrder(mockUser));

        when(userRepo.findByUsername(mockUser.getUsername())).thenReturn(mockUser);
        when(orderRepo.findByUser(mockUser)).thenReturn(mockOrderList);

        final ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(mockUser.getUsername());

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        List<UserOrder> userOrders= responseEntity.getBody();
        assertNotNull(userOrders);
        assertEquals(mockOrderList.size(), userOrders.size());
        for (int i=0; i<2; i++ ) {
            assertEquals(mockOrderList.get(i).getTotal(), userOrders.get(i).getTotal());
        }
        for (int i=0; i<2; i++ ) {
            assertEquals(mockOrderList.get(i).getItems(), userOrders.get(i).getItems());
        }
    }

    //Helper methods

    private User createMockUser() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("DanMockUser");
        mockUser.setCart(createMockCart(mockUser));

        return mockUser;
    }

    private Cart createMockCart(User user){
        Cart mockCart = new Cart();

        mockCart.setId(1L);
        mockCart.setItems(createMockItemList());
        mockCart.setUser(user);
        mockCart.setTotal(new BigDecimal("4.98"));

        return mockCart;
    }

    private List<Item> createMockItemList(){
        Item itemMock1 = createMockItem(1L, "Round Widget", new BigDecimal("2.99"),"A widget that is round");
        Item itemMock2 = createMockItem(2L, "Square Widget", new BigDecimal("1.99"), "A widget that is square");

        List<Item> mockItemList = new ArrayList<>();
        mockItemList.add(itemMock1);
        mockItemList.add(itemMock2);

        return mockItemList;
    }

    private Item createMockItem(Long id, String name, BigDecimal price, String description){
        Item itemMock = new Item();
        itemMock.setId(id);
        itemMock.setName(name);
        itemMock.setPrice(price);
        itemMock.setDescription(description);
        return itemMock;
    }

    private UserOrder createUserOrder(User mockUser) {
        UserOrder mockOrder = new UserOrder();
        mockOrder.setId(1L);
        mockOrder.setUser(mockUser);
        mockOrder.setItems(mockUser.getCart().getItems());
        mockOrder.setTotal(mockUser.getCart().getTotal());

        return mockOrder;
    }

}
