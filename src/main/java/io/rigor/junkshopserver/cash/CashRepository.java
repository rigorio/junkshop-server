package io.rigor.junkshopserver.cash;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface CashRepository extends CrudRepository<Cash, String> {
  List<Cash> findAll();
  Optional<Cash> findByDate(String date);
}
