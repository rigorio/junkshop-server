package io.rigor.junkshopserver.junk.junklist;

import io.rigor.junkshopserver.client.Client;
import io.rigor.junkshopserver.client.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/junk/list")
public class JunkListController {

  private JunkListService junkListService;
  private ClientService clientService;

  public JunkListController(JunkListService junkListService,
                            ClientService clientService) {
    this.junkListService = junkListService;
    this.clientService = clientService;
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String date,
                                  @RequestParam(required = false) String clientId) {
    if (date != null)
      return new ResponseEntity<>(junkListService.findByDate(date), HttpStatus.OK);
    if (clientId != null)
      return new ResponseEntity<>(junkListService.findByClientId(clientId), HttpStatus.OK);
    return new ResponseEntity<>(junkListService.all(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable(required = false) String id) {
    if (id != null)
      return new ResponseEntity<>(junkListService.findById(id), HttpStatus.OK);
    return new ResponseEntity<>(junkListService.all(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody JunkList junkList) {
    if (junkList.getDate() == null)
      junkList.setDate(LocalDate.now().toString());
    JunkList purchase = junkListService.save(junkList);
    String clientId = purchase.getClientId();
    Optional<Client> byId = clientService.findById(clientId);
    if (byId.isPresent()) {
      Client client = byId.get();
      Double cashAdvance = Double.valueOf(client.getCashAdvance());
      Double purchasePrice = Double.valueOf(purchase.getTotalPrice());
      double whatthefuckamidoing = cashAdvance - purchasePrice;
      if (cashAdvance > 0) {
        Double totalPrice = Double.valueOf(purchase.getTotalPrice());
        double remainingLoan = cashAdvance - totalPrice;
        if (remainingLoan >= 0) {
          purchase.setTotalPrice("0.0");
        client.setCashAdvance("" + remainingLoan);
        } else {
          purchase.setTotalPrice("" + (0 - remainingLoan));
          client.setCashAdvance("0.0");
        }
        junkListService.save(purchase);
        clientService.save(client);
      }
    }
    return new ResponseEntity<>(purchase, HttpStatus.CREATED);
  }
}
