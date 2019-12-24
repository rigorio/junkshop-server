package io.rigor.junkshopserver.purchase;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface PurchaseRepository  extends CrudRepository<Purchase, String> {
  List<Purchase> findAllByDate(String date);
}
