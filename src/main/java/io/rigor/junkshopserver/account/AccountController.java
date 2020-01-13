package io.rigor.junkshopserver.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  private AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping
  public ResponseEntity<?> all() {
    List<String> ids = accountService.all()
        .stream()
        .map(Account::getId)
        .collect(Collectors.toList());
    return new ResponseEntity<>(ids, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Account account) {
    Optional<Account> check = accountService.check(account.getUsername(), account.getPassword());
    if (check.isPresent())
      return new ResponseEntity<>(check.get().getId(), HttpStatus.ACCEPTED);
    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody Account account) {
    accountService.save(account);
    return new ResponseEntity<>("Success", HttpStatus.OK);
  }
}
