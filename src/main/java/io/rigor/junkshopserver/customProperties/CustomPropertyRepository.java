package io.rigor.junkshopserver.customProperties;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface CustomPropertyRepository extends CrudRepository<CustomProperty, String> {
  List<CustomProperty> findAll();
  List<CustomProperty> findAllByAccountId(String accountId);
  Optional<CustomProperty> findByPropertyAndAccountId(String property, String accountId);
}
