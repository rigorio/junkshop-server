package io.rigor.junkshopserver.junk;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface JunkRepository extends CrudRepository<Junk, String> {
  List<Junk> findAllByDate(String date);
  List<Junk> findAll();
}
