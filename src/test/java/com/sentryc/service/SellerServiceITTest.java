package com.sentryc.service;

import com.sentryc.controller.SellerController;
import com.sentryc.controller.dto.*;
import com.sentryc.data.entities.MarketplaceEntity;
import com.sentryc.data.entities.ProducerEntity;
import com.sentryc.data.entities.SellerEntity;
import com.sentryc.data.entities.SellerInfoEntity;
import com.sentryc.data.entities.enums.SellerState;
import com.sentryc.data.repository.MarketplaceRepository;
import com.sentryc.data.repository.ProducerRepository;
import com.sentryc.data.repository.SellerInfoRepository;
import com.sentryc.data.repository.SellerRepository;
import com.sentryc.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@Slf4j
@SpringBootTest
class SellerServiceITTest {

    @Autowired
    public SellerRepository sellerRepository;

    @Autowired
    public MarketplaceRepository marketplaceRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private SellerController sellerController;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @BeforeEach
    public void init() {

        var marketplace = new MarketplaceEntity();
        marketplace.setId("amazon.ae");
        marketplace.setDescription("test data marketplace");

        var sellerInfo1 = new SellerInfoEntity();
        sellerInfo1.setName("sellerInfo1");
        sellerInfo1.setCountry("US");
        sellerInfo1.setUrl("sellerInfo1.com");
        sellerInfo1.setExternalId("yn");
        sellerInfo1.setMarketplace(marketplace);

        var sellerInfo2 = new SellerInfoEntity();
        sellerInfo2.setName("sellerInfo2");
        sellerInfo2.setCountry("US");
        sellerInfo2.setUrl("sellerInfo2.com");
        sellerInfo2.setExternalId("vb");
        sellerInfo2.setMarketplace(marketplace);

        var producer1 = new ProducerEntity();
        producer1.setName("adidas");
        producer1.setCreatedAt(Timestamp.from(Instant.now()));

        var producer2 = new ProducerEntity();
        producer2.setName("Nike");
        producer2.setCreatedAt(Timestamp.from(Instant.now()));

        var seller1 = new SellerEntity();
        seller1.setId(UUID.randomUUID());
        seller1.setSellerInfo(sellerInfo1);
        seller1.setProducer(producer1);
        seller1.setState(SellerState.BLACKLISTED);

        var seller2 = new SellerEntity();
        seller2.setId(UUID.randomUUID());
        seller2.setSellerInfo(sellerInfo1);
        seller2.setProducer(producer2);
        seller2.setState(SellerState.WHITELISTED);

        var seller3 = new SellerEntity();
        seller3.setId(UUID.randomUUID());
        seller3.setSellerInfo(sellerInfo2);
        seller3.setProducer(producer2);
        seller3.setState(SellerState.BLACKLISTED);

        marketplaceRepository.save(marketplace);
        sellerInfoRepository.saveAll(List.of(sellerInfo1,sellerInfo2));
        producerRepository.saveAll(List.of(producer1, producer2));
        sellerRepository.saveAll(List.of(seller1, seller2, seller3));

    }
    @AfterEach
    void clean() {
        marketplaceRepository.deleteAll();
        sellerInfoRepository.deleteAll();
        producerRepository.deleteAll();
        sellerRepository.deleteAll();
    }

    @Test
    void searchByNameFilterTest() {
        String sellerName = "sellerInfo1";
        var sellerFilter = new SellerFilter(sellerName, null, null);
        var page = new PageInput(0,1);
        var sort = SellerSortBy.NAME_DESC;
        SellerPageableResponse sellerPageableResponse = sellerController.sellers(sellerFilter, page, sort);

        assertEquals(sellerName, sellerPageableResponse.data().get(0).sellerName());
    }
    @Test
    void searchByMarketplaceIdFilterTest() {
        String marketPlaceId = "amazon.ae";
        var searchFilter = new SellerFilter(null, null, List.of(marketPlaceId));
        var page = new PageInput(0,1);
        SellerPageableResponse sellerPageableResponse = sellerController.sellers(searchFilter, page, null);

        assertEquals(marketPlaceId, sellerPageableResponse.data().get(0).marketplaceId());
    }
    @Test
    void searchByProducerIdFilterTest() {
        ProducerEntity producerEntity = producerRepository.findAll().get(0);
        List<String> stringProducerIds = List.of(producerEntity.getId().toString());
        var searchFilter = new SellerFilter(null, stringProducerIds, null);
        var page = new PageInput(0,2);
        SellerPageableResponse sellerPageableResponse = sellerController.sellers(searchFilter, page, null);
        List<List<String>> result = sellerPageableResponse.data().stream().map(
                response -> response.producerSellerStates().stream().map(ProducerSellerStates::producerId).toList()
        ).toList();

        assertEquals(stringProducerIds, result.get(0));
    }

}