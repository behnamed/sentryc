package com.sentryc.service;

import com.sentryc.controller.dto.PageInput;
import com.sentryc.controller.dto.SellerFilter;
import com.sentryc.controller.dto.SellerPageableResponse;
import com.sentryc.controller.dto.SellerSortBy;
import com.sentryc.data.entities.SellerEntity;
import org.springframework.data.domain.Page;

public interface SellerService {
    SellerPageableResponse getSellers(SellerFilter sellerFilter, PageInput pageInput, SellerSortBy sellerSortBy);

}
