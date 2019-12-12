package io.rigor.junkshopserver.purchase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

  private PurchaseService<Purchase> purchaseService;

  public PurchaseController(PurchaseService<Purchase> purchaseService) {
    this.purchaseService = purchaseService;
  }

  @GetMapping()
  public ResponseEntity<?> getAllNoFilter(@RequestParam(required = false) String date) {
    if (date != null)
      return new ResponseEntity<>(purchaseService.findByDate(date), HttpStatus.OK);
    return new ResponseEntity<>(purchaseService.findAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getAll(@PathVariable(required = false) Long id) {
    if (id != null)
      return new ResponseEntity<>(purchaseService.findById(id), HttpStatus.OK);
    return new ResponseEntity<>(purchaseService.findAll(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Object body) throws JsonProcessingException {
    if (body instanceof List) {
      ObjectMapper mapper = new ObjectMapper();
      String s = mapper.writeValueAsString(body);
      System.out.println("eh");
      System.out.println(s);
      List<Purchase> junks = mapper.readValue(s, new TypeReference<List<Purchase>>() {});
      return new ResponseEntity<>(purchaseService.saveAll(junks), HttpStatus.CREATED);
    }
    Purchase junk = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(body), new TypeReference<Purchase>() {});
    return new ResponseEntity<>(purchaseService.save(junk), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    purchaseService.deleteById(id);
    return new ResponseEntity<>(purchaseService.findAll(), HttpStatus.OK);
  }
}
