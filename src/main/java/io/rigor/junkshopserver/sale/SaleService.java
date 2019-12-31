package io.rigor.junkshopserver.sale;

import java.util.List;
import java.util.Optional;

public interface SaleService<T> {
  List<T> findAll();

  Optional<T> findById(String id);

  List<T> findByDate(String date);

  void deleteById(String id);

  void delete(T t);

  List<T> saveAll(List<T> t);

  T save(T t);
}
