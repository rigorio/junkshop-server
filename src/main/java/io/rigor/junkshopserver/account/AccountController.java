package io.rigor.junkshopserver.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  private AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Account account) {
    return new ResponseEntity<>(accountService.check(account.getUsername(), account.getPassword()), HttpStatus.OK);
  }

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody Account account) {
    accountService.save(account);
    return new ResponseEntity<>("Success", HttpStatus.OK);
  }
}
