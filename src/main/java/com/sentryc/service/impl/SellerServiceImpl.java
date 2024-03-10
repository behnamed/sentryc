package com.sentryc.service.impl;

import com.sentryc.controller.dto.*;
import com.sentryc.data.entities.MarketplaceEntity;
import com.sentryc.data.entities.ProducerEntity;
import com.sentryc.data.entities.SellerEntity;
import com.sentryc.data.entities.SellerInfoEntity;
import com.sentryc.data.repository.SellerRepository;
import com.sentryc.service.SellerService;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;

    public SellerServiceImpl(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Override
    public SellerPageableResponse getSellers(SellerFilter sellerFilter, PageInput pageInput, SellerSortBy sellerSortBy) {
        return this.findByFilter(sellerFilter, pageInput, sellerSortBy);
    }

    private SellerPageableResponse findByFilter(SellerFilter sellerFilter, PageInput pageInput, SellerSortBy sellerSortBy) {
        Specification<SellerEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<SellerEntity, SellerInfoEntity> sellerInfoEntityJoin = root.join("sellerInfo");
            Join<SellerInfoEntity, MarketplaceEntity> marketplaceEntityJoin = sellerInfoEntityJoin.join("marketplace");
            Join<SellerEntity, ProducerEntity> producerEntityJoin = root.join("producer");

            if (sellerFilter.searchByName() != null) {
                predicates.add(cb.equal(sellerInfoEntityJoin.get("name"), sellerFilter.searchByName()));
            }

            if (!CollectionUtils.isEmpty(sellerFilter.marketplaceIds())) {
                CriteriaBuilder.In<String> inClause = cb.in(marketplaceEntityJoin.get("id"));
                for (String marketplaceId : sellerFilter.marketplaceIds()) {
                    inClause.value(marketplaceId);
                }
                predicates.add(inClause);
            }

            if (!CollectionUtils.isEmpty(sellerFilter.producerIds())) {
                CriteriaBuilder.In<UUID> inClause = cb.in(producerEntityJoin.get("id"));
                for (String producerId : sellerFilter.producerIds()) {
                    inClause.value(UUID.fromString(producerId));
                }
                predicates.add(inClause);
            }

            if (sellerSortBy != null) {
                Order order = switch (sellerSortBy) {
                    case NAME_ASC -> cb.asc(sellerInfoEntityJoin.get("name"));
                    case NAME_DESC -> cb.desc(sellerInfoEntityJoin.get("name"));
                    case MARKETPLACE_ID_ASC -> cb.asc(marketplaceEntityJoin.get("id"));
                    case MARKETPLACE_ID_DESC -> cb.desc(marketplaceEntityJoin.get("id"));
                    case SELLER_INFO_EXTERNAL_ID_ASC -> cb.asc(sellerInfoEntityJoin.get("externalId"));
                    case SELLER_INFO_EXTERNAL_ID_DESC -> cb.desc(sellerInfoEntityJoin.get("externalId"));
                };
                query.orderBy(order);
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        var sellerPageableResponse = new SellerPageableResponse(sellerRepository.findAll(spec,
                        PageRequest.of(pageInput.page(), pageInput.size()))
                .stream().collect(groupingBy(SellerEntity::getSellerInfo))
                        .entrySet().stream().map((entry) -> this.convertToSeller(entry.getKey(),entry.getValue()))
                .toList());

        log.info("search result with filter {} is : {}", sellerFilter.toString(), sellerPageableResponse);
        return sellerPageableResponse;
    }

    private Seller convertToSeller(SellerInfoEntity sellerInfo, List<SellerEntity> sellerEntityList) {
        return new Seller(sellerInfo.getName(), sellerInfo.getExternalId(),
                this.convertToProducerSellerStates(sellerEntityList), sellerInfo.getMarketplace().getId());
    }

    private List<ProducerSellerStates> convertToProducerSellerStates(List<SellerEntity> sellerEntityList) {
        List<ProducerSellerStates> producerSellerStateList = new ArrayList<>();
        for (SellerEntity sellerEntity : sellerEntityList) {
            var producer = sellerEntity.getProducer();
            var producerSellerState = new ProducerSellerStates(producer.getId().toString(), producer.getName(),
                    sellerEntity.getState(), sellerEntity.getId().toString());
            producerSellerStateList.add(producerSellerState);
        }
        return producerSellerStateList;
    }


}
