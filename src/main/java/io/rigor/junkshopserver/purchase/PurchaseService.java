package io.rigor.junkshopserver.purchase;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PurchaseService<T> {
  List<T> findAll();
  List<T> findAll(Sort sort);

  Optional<T> findById(Long id);

  List<T> findByDate(String date);

  void deleteById(Long id);

  void delete(T t);

  List<T> saveAll(List<T> t);

  T save(T t);
}
