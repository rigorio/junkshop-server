package io.rigor.junkshopserver.customProperties;

import java.util.List;
import java.util.Optional;

public interface CustomPropertyService {
  List<CustomProperty> findAll(String accountId);

  Optional<CustomProperty> findByPropertyAndAccountID(String property, String accountId);

  Optional<CustomProperty> findById(String id);

  void deleteById(String id);

  void delete(CustomProperty CustomProperty);

  CustomProperty save(CustomProperty customProperty);

  List<CustomProperty> saveAll(List<CustomProperty> CustomPropertys);
}
