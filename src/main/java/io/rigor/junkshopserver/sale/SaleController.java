package io.rigor.junkshopserver.sale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rigor.junkshopserver.cash.CashService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale")
public class SaleController {

  private SaleService<Sale> saleService;
  private CashService cashService;

  public SaleController(SaleService<Sale> saleService,
                        CashService cashService) {
    this.saleService = saleService;
    this.cashService = cashService;
  }

  @GetMapping()
  public ResponseEntity<?> getAllNoFilter(@RequestParam(required = false) String date) {
    if (date != null)
      return new ResponseEntity<>(saleService.findByDate(date), HttpStatus.OK);
    return new ResponseEntity<>(saleService.findAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getAll(@PathVariable(required = false) String id) {
    if (id != null)
      return new ResponseEntity<>(saleService.findById(id), HttpStatus.OK);
    return new ResponseEntity<>(saleService.findAll(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Object body) throws JsonProcessingException {
    if (body instanceof List) {
      ObjectMapper mapper = new ObjectMapper();
      String s = mapper.writeValueAsString(body);
      List<Sale> sales = mapper.readValue(s, new TypeReference<List<Sale>>() {});
      List<Sale> newSales = saleService.saveAll(sales);
      newSales.forEach(cashService::addSales);
      return new ResponseEntity<>(newSales, HttpStatus.CREATED);
    }
    Sale sale = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(body), new TypeReference<Sale>() {});
    Sale newSale = saleService.save(sale);
    cashService.addSales(newSale);
    return new ResponseEntity<>(newSale, HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    saleService.deleteById(id);
    return new ResponseEntity<>(saleService.findAll(), HttpStatus.OK);
  }
}
