package io.rigor.junkshopserver.account;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface AccountRepository extends CrudRepository<Account, String> {
  Optional<Account> findByUsernameAndPassword(String username, String password);
  List<Account> findAll();
}
