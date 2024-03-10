package com.sentryc.controller.dto;

import java.util.List;

public record SellerFilter(String searchByName, List<String> producerIds, List<String> marketplaceIds) {
}
