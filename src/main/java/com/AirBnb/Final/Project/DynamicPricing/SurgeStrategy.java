package com.AirBnb.Final.Project.DynamicPricing;


import com.AirBnb.Final.Project.Entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SurgeStrategy {

    private final BaseStrategy baseStrategy;

    public BigDecimal SurgePrice(Inventory inventory){
        return baseStrategy.basicPrice(inventory).multiply(inventory.getSurgeFactor());
    }

}
