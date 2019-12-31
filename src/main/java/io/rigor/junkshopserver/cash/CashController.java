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
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(cashService.allDailyCash(), HttpStatus.OK);
  }

  @GetMapping("/today")
  public ResponseEntity<?> today() {
    List<Cash> cashList = cashService.allDailyCash();
    Optional<Cash> any = cashList.stream().filter(c -> c.getDate().equals(LocalDate.now().toString())).findAny();
    Cash cash = any.orElseGet(Cash::new);
    Cash savedCash = cashService.updateCapital(cash);
    return new ResponseEntity<>(savedCash, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Cash cash) {
    return new ResponseEntity<>(cashService.updateCapital(cash), HttpStatus.OK);
  }
}
