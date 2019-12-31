package io.rigor.junkshopserver.sale.saleitem;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface SaleItemRepository extends CrudRepository<SaleItem, String> {
}
