package com.sentryc.controller.dto;

import lombok.*;

import java.util.List;

public record Seller(String sellerName,
                     String externalId,
                     List<ProducerSellerStates> producerSellerStates,
                     String marketplaceId) {}
