package io.rigor.junkshopserver.junk.junklist;

import java.util.List;
import java.util.Optional;

public interface JunkListService {
  List<JunkList> all();

  Optional<JunkList> findById(String id);

  List<JunkList> findByDate(String date);

  List<JunkList> saveAll(List<JunkList> list);

  JunkList save(JunkList junkList);
}