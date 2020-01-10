package io.rigor.junkshopserver.junk;

import java.util.List;
import java.util.Optional;

public interface JunkService {
  List<Junk> findAll(String accountId);

  Optional<Junk> findById(String id);

  List<Junk> findByDate(String date, String accountId);

  void deleteById(String id);

  void delete(Junk junk);

  List<Junk> saveAll(List<Junk> junks);

  Junk save(Junk junk);
}
