package com.hashedin.virtualproperty.application.service;

import com.hashedin.virtualproperty.application.entities.Property;
import com.hashedin.virtualproperty.application.exceptions.CustomException;
import com.hashedin.virtualproperty.application.repository.PropertyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyServiceTest {
    @Mock
    private PropertyRepo propertyRepo;
    @InjectMocks
    private PropertyService propertyService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }


//    @Mock
//    private PropertyRepo propertyRepo;
//    @InjectMocks
//    private PropertyService propertyService;
//
//    @BeforeEach
//    void setUp(){
//        MockitoAnnotations.initMocks(this);
//    }
//

//    @Test
//    void testProperty(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Property p2 = new Property(2,"lucknow","UP","gomti nagar","flat","Sell","xyza@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        List<Property> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        Mockito.when(propertyRepo.getProperties()).thenReturn(list);
//        assertEquals(list , propertyService.getProperty());
//    }
//
//    @Test
//    void gettingErrorNoPropertyFoundFromGetProperty(){
//        List<Property> list= new ArrayList<>();
//        Mockito.when(propertyRepo.getProperties()).thenReturn(list);
//        assertThrows(CustomException.class, () -> propertyService.getProperty());
//    }
//
//    @Test
//    void testCreateProperty(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Mockito.when(propertyRepo.save(p1)).thenReturn(p1);
//        assertEquals(p1 , propertyService.createProperty(p1));
//    }
//
//    @Test
//    void testPropertyByCityName(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Property p2 = new Property(2,"lucknow","UP","gomti nagar","flat","Sell","xyza@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        List<Property> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        Mockito.when(propertyRepo.getPropertiesByCity("lucknow")).thenReturn(list);
//        assertEquals(list , propertyService.getPropertyByCityName("lucknow"));
//
//    }
//
//    @Test
//    void testPropertyByCityNameWhenCityIsNotAvailable(){
//        List<Property> list = new ArrayList<>();
//        Mockito.when(propertyRepo.getPropertiesByCity("lucknow")).thenReturn(list);
//        assertThrows(CustomException.class , () -> propertyService.getPropertyByCityName("lucknow"));
//
//    }
//
//    @Test
//    void testPropertyByCityandType(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Property p2 = new Property(2,"lucknow","UP","gomti nagar","flat","Sell","xyza@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        List<Property> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        Mockito.when(propertyRepo.getPropertiesByCityAndType("lucknow" , "flat")).thenReturn(list);
//        assertEquals(list , propertyService.getPropertyByCityAndType("lucknow" , "flat"));
//
//    }
//
//    @Test
//    void testPropertyByCityAndTypeWhenCityAndTypeIsNotAvailable(){
//        List<Property> list = new ArrayList<>();
//        Mockito.when(propertyRepo.getPropertiesByCityAndType("lucknow" , "flat")).thenReturn(list);
//        assertThrows(CustomException.class , () -> propertyService.getPropertyByCityAndType("lucknow" , "flat"));
//
//    }
//
//    @Test
//    void testPropertyByStateAndType(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Property p2 = new Property(2,"lucknow","UP","gomti nagar","flat","Sell","xyza@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        List<Property> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        Mockito.when(propertyRepo.getPropertiesByStateAndType("UP" , "flat")).thenReturn(list);
//        assertEquals(list , propertyService.getPropertyByStateAndType("UP" , "flat"));
//
//    }
//
//    @Test
//    void testPropertyByStateAndTypeWhenStateAndTypeIsNotAvailable(){
//        List<Property> list = new ArrayList<>();
//        Mockito.when(propertyRepo.getPropertiesByStateAndType("UP" , "flat")).thenReturn(list);
//        assertThrows(CustomException.class , () -> propertyService.getPropertyByStateAndType("UP" , "flat"));
//
//    }
//
//
//    @Test
//    void testPropertyByAddress(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Property p2 = new Property(2,"lucknow","UP","gomti nagar","flat","Sell","xyza@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        List<Property> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        Mockito.when(propertyRepo.getPropertiesByAddress("gomti nagar" , "lucknow" , "UP")).thenReturn(list);
//        assertEquals(list , propertyService.getPropertyByAddress("gomti nagar" , "lucknow" , "UP"));
//
//
//    }
//
//    @Test
//    void testPropertyByAddressWhenAddressIsNotAvailable(){
//        List<Property> list = new ArrayList<>();
//        Mockito.when(propertyRepo.getPropertiesByAddress("gomti nagar" , "lucknow" , "UP")).thenReturn(list);
//        assertThrows(CustomException.class , () -> propertyService.getPropertyByAddress("gomti nagar" , "lucknow" , "UP"));
//
//    }
//
//    @Test
//    void testPropertyByState(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Property p2 = new Property(2,"lucknow","UP","gomti nagar","flat","Sell","xyza@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        List<Property> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        Mockito.when(propertyRepo.getPropertiesByState("UP")).thenReturn(list);
//        assertEquals(list , propertyService.getPropertyByState("UP"));
//
//
//    }
//
//    @Test
//    void testPropertyByStateWhenStateIsNotAvailable(){
//        List<Property> list = new ArrayList<>();
//        Mockito.when(propertyRepo.getPropertiesByState("UP")).thenReturn(list);
//        assertThrows(CustomException.class , () -> propertyService.getPropertyByState("UP"));
//
//    }
//
//
//    @Test
//    void testPropertyById(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Mockito.when(propertyRepo.getPropertiesById(1)).thenReturn(p1);
//        assertEquals(p1 , propertyService.getPropertyById(1));
//    }
//
//    @Test
//    void testPropertyByIdWhenIdIsNotAvailable(){
//        Mockito.when(propertyRepo.getPropertiesById(1)).thenReturn(null);
//        assertThrows(CustomException.class , () -> propertyService.getPropertyById(1));
//
//    }
//
//    @Test
//    void testPropertyByBudget(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Property p2 = new Property(2,"lucknow","UP","gomti nagar","flat","Sell","xyza@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        List<Property> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        Mockito.when(propertyRepo.getPropertiesByBudget(2000000 , 4000000)).thenReturn(list);
//        assertEquals(list , propertyService.getPropertyByBudget(2000000,4000000));
//
//
//    }
//
//
//    @Test
//    void testPropertyByBudgetWhenThereIsNoHouseOfThatBudget(){
//        List<Property> list = new ArrayList<>();
//        Mockito.when(propertyRepo.getPropertiesByBudget(2000000 , 4000000)).thenReturn(list);
//        assertThrows(CustomException.class , () -> propertyService.getPropertyByBudget(2000000 , 4000000));
//
//    }
//
//
//    @Test
//    void testPropertyByMaxPrice(){
//        Property p1 = new Property(1,"lucknow","UP","gomti nagar","flat","Sell","xyz@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        Property p2 = new Property(2,"lucknow","UP","gomti nagar","flat","Sell","xyza@gmail.com","its a awesome house",1500,2,2015, 3000000, 1, 2, 2, 226010);
//        List<Property> list = new ArrayList<>();
//        list.add(p1);
//        list.add(p2);
//        Mockito.when(propertyRepo.getPropertiesByMaxPrice(3000000)).thenReturn(list);
//        assertEquals(list , propertyService.getPropertyByMaxPrice(3000000));
//
//
//    }
//
//
//    @Test
//    void testPropertyByMaxPriceWhenThereIsNoHouseOfThatPrice(){
//        List<Property> list = new ArrayList<>();
//        Mockito.when(propertyRepo.getPropertiesByMaxPrice(3000000)).thenReturn(list);
//        assertThrows(CustomException.class , () -> propertyService.getPropertyByMaxPrice(3000000));
//
//    }

}