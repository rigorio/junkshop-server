package io.rigor.junkshopserver.account;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, String> {
  Optional<Account> findByUsernameAndPassword(String username, String password);
}
