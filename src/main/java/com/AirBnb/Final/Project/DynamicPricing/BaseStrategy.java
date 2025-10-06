package com.AirBnb.Final.Project.DynamicPricing;

import com.AirBnb.Final.Project.Entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BaseStrategy {

    public BigDecimal basicPrice(Inventory inventory){
        return inventory.getBasePrice();
    }

}
