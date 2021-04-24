package com.example.demo.controllers;


import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);


    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItems() throws Exception {
        List<Item> mockItemList = createMockItemList();
        when(itemRepo.findAll()).thenReturn(mockItemList);

        final ResponseEntity<List<Item>> responseEntity = itemController.getItems();

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        List<Item> items = responseEntity.getBody();
        assertNotNull(items);
        assertEquals(mockItemList.get(0).getName(), items.get(0).getName());
        assertEquals(mockItemList.get(1).getName(), items.get(1).getName());
        assertEquals(mockItemList.get(0).getPrice(), items.get(0).getPrice());
        assertEquals(mockItemList.get(1).getPrice(), items.get(1).getPrice());
        assertEquals(mockItemList.get(0).getDescription(), items.get(0).getDescription());
        assertEquals(mockItemList.get(1).getDescription(), items.get(1).getDescription());
    }


    @Test
    public void getItemById() throws Exception {
        Item mockItem = createMockItem();
        when(itemRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(mockItem));

        final ResponseEntity<Item> responseEntity = itemController.getItemById(1L);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Item item = responseEntity.getBody();
        assertNotNull(item);
        assertEquals(mockItem.getName(), item.getName());
        assertEquals(mockItem.getPrice(), item.getPrice());
        assertEquals(mockItem.getDescription(), item.getDescription());
    }

    @Test
    public void getItemsByName() throws Exception {
        List<Item> mockItemList = createMockItemList();
        when(itemRepo.findByName("Round Widget")).thenReturn(mockItemList);

        final ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("Round Widget");

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        List<Item> items = responseEntity.getBody();
        assertNotNull(items);
        assertEquals(mockItemList.get(0).getName(), items.get(0).getName());
        assertEquals(mockItemList.get(0).getPrice(), items.get(0).getPrice());
        assertEquals(mockItemList.get(0).getDescription(), items.get(0).getDescription());
    }


    //Helper method

    private Item createMockItem(){
        Item itemMock = mock(Item.class);

        itemMock.setId(1L);
        itemMock.setName("Round Widget");
        itemMock.setPrice(new BigDecimal("2.99"));
        itemMock.setDescription("A widget that is round");

        return itemMock;
    }


    private List<Item> createMockItemList(){
        Item itemMock1 = mock(Item.class);
        Item itemMock2 = mock(Item.class);

        itemMock1.setId(1L);
        itemMock1.setName("Round Widget");
        itemMock1.setPrice(new BigDecimal("2.99"));
        itemMock1.setDescription("A widget that is round");

        itemMock2.setId(2L);
        itemMock2.setName("Square Widget");
        itemMock2.setPrice(new BigDecimal("1.99"));
        itemMock2.setDescription("A widget that is square");

        List<Item> mockItemList = new ArrayList<>();
        mockItemList.add(itemMock1);
        mockItemList.add(itemMock2);

        return mockItemList;
    }
}
