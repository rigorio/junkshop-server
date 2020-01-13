package io.rigor.junkshopserver.account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
  void save(Account account);

  Optional<Account> check(String username, String password);

  List<Account> all();
}
