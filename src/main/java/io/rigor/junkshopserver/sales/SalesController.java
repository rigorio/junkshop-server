package io.rigor.junkshopserver.sales;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {
  private SalesService salesService;

  public SalesController(SalesService salesService) {
    this.salesService = salesService;
  }

  @GetMapping("/month")
  public ResponseEntity<?> getByMonth(@RequestParam(required = false) String year) {
    List<SalesEntity> salesEntities = salesService.viewByMonth();
    return new ResponseEntity<>(salesEntities, HttpStatus.OK);
  }
}
