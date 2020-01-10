package io.rigor.junkshopserver.client;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ClientRepository extends CrudRepository<Client, String> {
  List<Client> findAll();

  List<Client> findAllByAccountId(String accountId);



}
