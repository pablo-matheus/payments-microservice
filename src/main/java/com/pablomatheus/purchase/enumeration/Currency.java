package com.pablomatheus.purchase.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Currency {

    USD("United States", "Dollar");

    private final String name;
    private final String country;

}
