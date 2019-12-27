package io.rigor.junkshopserver.customProperties;

import java.util.List;
import java.util.Optional;

public interface CustomPropertyService {
  List<CustomProperty> findAll();

  Optional<CustomProperty> findByProperty(String property);

  Optional<CustomProperty> findById(String id);

  void deleteById(String id);

  void delete(CustomProperty CustomProperty);

  CustomProperty save(CustomProperty customProperty);

  List<CustomProperty> saveAll(List<CustomProperty> CustomPropertys);
}
