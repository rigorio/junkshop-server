package io.rigor.junkshopserver.purchase.PurchaseItem;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface PurchaseItemRepository extends CrudRepository<PurchaseItem, String> {
}
