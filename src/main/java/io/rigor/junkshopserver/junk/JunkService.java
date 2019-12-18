package io.rigor.junkshopserver.junk;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface JunkService {
  List<Junk> findAll();

  List<Junk> findAll(Sort sort);

  Optional<Junk> findById(Long id);

  List<Junk> findByDate(String date);

  void deleteById(Long id);

  void delete(Junk junk);

  List<Junk> saveAll(List<Junk> junks);

  Junk save(Junk junk);
}
