package io.rigor.junkshopserver.cash;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Cash cash) {
    return new ResponseEntity<>(cashService.updateCapital(cash), HttpStatus.OK);
  }
}
