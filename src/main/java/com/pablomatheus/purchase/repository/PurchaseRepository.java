package com.pablomatheus.purchase.repository;

import com.pablomatheus.purchase.entity.PurchaseEntity;
import org.springframework.data.repository.CrudRepository;

public interface PurchaseRepository extends CrudRepository<PurchaseEntity, Long> {
}
