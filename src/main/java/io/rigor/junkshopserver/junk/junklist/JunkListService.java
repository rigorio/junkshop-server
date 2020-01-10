package io.rigor.junkshopserver.junk.junklist;

import java.util.List;
import java.util.Optional;

public interface JunkListService {
  List<JunkList> all(String accountId);

  Optional<JunkList> findById(String id);

  List<JunkList> findByDate(String date, String accountId);

  List<JunkList> saveAll(List<JunkList> list);

  JunkList save(JunkList junkList, String accountId);

  List<JunkList> findByClientId(String clientId, String accountId);
}
