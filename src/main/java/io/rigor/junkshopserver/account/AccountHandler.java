package io.rigor.junkshopserver.account;

import java.util.Optional;

public class AccountHandler implements AccountService {
  private AccountRepository accountRepository;

  public AccountHandler(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public void save(Account account) {
    accountRepository.save(account);
  }

  @Override
  public boolean check(String username, String password) {
    Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
    return account.isPresent();
  }
}
