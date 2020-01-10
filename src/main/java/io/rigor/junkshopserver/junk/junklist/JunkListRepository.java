package io.rigor.junkshopserver.junk.junklist;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface JunkListRepository extends CrudRepository<JunkList, String> {
  List<JunkList> findAll();
  List<JunkList> findAllByDate(String date);
  List<JunkList> findAllByClientId(String clientId);
}
