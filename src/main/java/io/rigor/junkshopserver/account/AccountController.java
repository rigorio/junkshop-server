package io.rigor.junkshopserver.account;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    List<Account> accounts = accountService.all();
    accounts.forEach(account -> account.setPassword(""));
    return new ResponseEntity<>(accounts, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Account account) {
    Optional<Account> check = accountService.check(account.getUsername(), account.getPassword());
    if (check.isPresent()) {
      Map<String, String> map = new HashMap<>();
      Account acc = check.get();
      map.put("id", acc.getId());
      map.put("role", acc.getRole());
      return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
    }
    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/create")
  public ResponseEntity<?> create(@RequestBody Account account) {
    return new ResponseEntity<>(accountService.save(account), HttpStatus.OK);
  }
}
