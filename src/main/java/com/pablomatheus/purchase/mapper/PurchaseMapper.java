package com.pablomatheus.purchase.mapper;

import com.pablomatheus.purchase.dto.PurchaseDto;
import com.pablomatheus.purchase.entity.PurchaseEntity;
import com.pablomatheus.purchase.request.PurchaseRequest;
import com.pablomatheus.purchase.response.PurchaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

    @Mapping(target = "originalAmount.value", source = "amount")
    @Mapping(target = "originalAmount.currency", source = "currency")
    @Mapping(target = "originalAmount.country", source = "country")
    PurchaseDto toDto(PurchaseRequest purchaseRequest);

    @Mapping(target = "originalAmount.value", source = "amount")
    @Mapping(target = "originalAmount.currency", source = "currency")
    @Mapping(target = "originalAmount.country", source = "country")
    PurchaseDto toDto(PurchaseEntity purchaseEntity);

    PurchaseResponse toResponse(PurchaseDto purchaseDto);

    @Mapping(target = "amount", source = "originalAmount.value")
    @Mapping(target = "currency", source = "originalAmount.currency")
    @Mapping(target = "country", source = "originalAmount.country")
    PurchaseEntity toEntity(PurchaseDto purchaseDto);

}
