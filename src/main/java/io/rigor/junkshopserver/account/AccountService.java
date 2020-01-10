package io.rigor.junkshopserver.account;

public interface AccountService {
  void save(Account account);

  boolean check(String username, String password);
}
