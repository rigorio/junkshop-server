package io.rigor.junkshopserver.cash;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cash")
public class CashController {
  private CashService cashService;

  public CashController(CashService cashService) {
    this.cashService = cashService;
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam String accountId) {
    return new ResponseEntity<>(cashService.allDailyCash(accountId), HttpStatus.OK);
  }

  @GetMapping("/today")
  public ResponseEntity<?> today(@RequestParam String accountId) {
//    Cash savedCash = getToday(accountId);
    Optional<Cash> byDateAcc = cashService.findByDateAcc(LocalDate.now().toString(), accountId);
    return byDateAcc.<ResponseEntity<?>>map(cash -> new ResponseEntity<>(cash, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(cashService.what(new Cash()), HttpStatus.OK));
  }

  @GetMapping("/calibrate")
  public void calibrate(@RequestParam String accountId) {
    cashService.calibrateAll(accountId);
  }

  private Cash getToday(String accountId) {

    List<Cash> cashList = cashService.allDailyCash(accountId);
    Optional<Cash> any = cashList
        .stream()
        .filter(c -> c.getDate().equals(LocalDate.now().toString()))
        .findAny();
    Cash cash = any.orElseGet(() -> new Cash(accountId)); // todo what

    return cashService.updateCapital(cash, accountId);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Cash cash, @RequestParam String accountId) {
    return new ResponseEntity<>(cashService.updateCapital(cash, accountId), HttpStatus.OK);
  }
}
