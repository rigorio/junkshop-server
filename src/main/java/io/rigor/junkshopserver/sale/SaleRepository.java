package io.rigor.junkshopserver.sale;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface SaleRepository extends CrudRepository<Sale, String> {
  List<Sale> findAllByDate(String date);

  List<Sale> findAllByDateAndAccountId(String date, String accountId);

  List<Sale> findAllByAccountId(String accountId);

  List<Sale> findAllByClientId(String clientId);

  List<Sale> findAllByClientIdAndAccountId(String clientId, String accountId);

  List<Sale> findAll();
}
