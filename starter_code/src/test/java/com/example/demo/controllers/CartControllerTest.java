package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
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

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
    }

    @Test
    public void addTocart() throws Exception {
        User mockUser = createMockUser();
        Item mockItem = createMockItem(1L, "Round Widget", new BigDecimal("2.99"),"A widget that is round");
        ModifyCartRequest request = createModifyCartRequest(mockUser, mockItem);

        when(userRepo.findByUsername(request.getUsername())).thenReturn(mockUser);
        when(itemRepo.findById(request.getItemId())).thenReturn(java.util.Optional.of(mockItem));

        final ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cart = responseEntity.getBody();
        assertNotNull(cart);
        assertEquals(mockUser, cart.getUser());

        List<Item> expectedItems = mockUser.getCart().getItems();
        expectedItems.add(mockItem);
        assertEquals(expectedItems, cart.getItems());

        BigDecimal expectedTotal = mockUser.getCart().getTotal();
        expectedTotal.add(mockItem.getPrice());
        assertEquals(expectedTotal, cart.getTotal());

    }

    @Test
    public void removeFromcart() throws Exception {

        User mockUser = createMockUser();
        Item mockItem = createMockItem(1L, "Round Widget", new BigDecimal("2.99"),"A widget that is round");
        ModifyCartRequest request = createModifyCartRequest(mockUser, mockItem);

        when(userRepo.findByUsername(request.getUsername())).thenReturn(mockUser);
        when(itemRepo.findById(request.getItemId())).thenReturn(java.util.Optional.of(mockItem));

        final ResponseEntity<Cart> responseEntity = cartController.addTocart(request);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cart = responseEntity.getBody();
        assertNotNull(cart);
        assertEquals(mockUser, cart.getUser());

        List<Item> expectedItems = mockUser.getCart().getItems();
        expectedItems.remove(mockItem);
        assertEquals(expectedItems, cart.getItems());

        BigDecimal expectedTotal = mockUser.getCart().getTotal();
        expectedTotal.subtract(mockItem.getPrice());
        assertEquals(expectedTotal, cart.getTotal());

    }

    //Helper methods

    private ModifyCartRequest createModifyCartRequest(User mockUser, Item mockItem){
        ModifyCartRequest modifyCartRequestMock = new ModifyCartRequest();
        modifyCartRequestMock.setUsername(mockUser.getUsername());
        modifyCartRequestMock.setItemId(mockItem.getId());
        modifyCartRequestMock.setQuantity(1);

        return modifyCartRequestMock;
    }

    private Cart createMockCart(User user){
        Cart mockCart = new Cart();

        mockCart.setId(1L);
        mockCart.setItems(createMockItemList());
        mockCart.setUser(user);
        mockCart.setTotal(new BigDecimal("4.98"));

        return mockCart;
    }

    private Item createMockItem(Long id, String name, BigDecimal price, String description){
        Item itemMock = new Item();
        itemMock.setId(id);
        itemMock.setName(name);
        itemMock.setPrice(price);
        itemMock.setDescription(description);
        return itemMock;
    }

    private List<Item> createMockItemList(){
        Item itemMock1 = createMockItem(1L, "Round Widget", new BigDecimal("2.99"),"A widget that is round");
        Item itemMock2 = createMockItem(2L, "Square Widget", new BigDecimal("1.99"), "A widget that is square");

        List<Item> mockItemList = new ArrayList<>();
        mockItemList.add(itemMock1);
        mockItemList.add(itemMock2);

        return mockItemList;
    }

    private User createMockUser() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("DanMockUser");
        mockUser.setCart(createMockCart(mockUser));

        return mockUser;
    }

}


