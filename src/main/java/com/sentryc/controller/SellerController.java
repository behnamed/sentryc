package com.sentryc.controller;

import com.sentryc.controller.dto.PageInput;
import com.sentryc.controller.dto.SellerFilter;
import com.sentryc.controller.dto.SellerPageableResponse;
import com.sentryc.controller.dto.SellerSortBy;
import com.sentryc.service.SellerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     * Sellers API
     * returns list of sellers based on input filter and sortBy
     * */
    @QueryMapping
    public SellerPageableResponse sellers(@Argument SellerFilter filter,
                                         @Argument PageInput page,
                                         @Argument SellerSortBy sortBy) {

        return sellerService.getSellers(filter, page, sortBy);
    }

}
