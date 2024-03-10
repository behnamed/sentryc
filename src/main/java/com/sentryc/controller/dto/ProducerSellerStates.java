package com.sentryc.controller.dto;

import com.sentryc.data.entities.enums.SellerState;

public record ProducerSellerStates(String producerId,
                                   String producerName,
                                   SellerState sellerState,
                                   String sellerId) {}
