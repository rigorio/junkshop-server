package io.rigor.junkshopserver.junk.junklist;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rigor.junkshopserver.client.Client;
import io.rigor.junkshopserver.client.ClientService;
import io.rigor.junkshopserver.junk.Junk;
import io.rigor.junkshopserver.junk.JunkRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/junk/list")
public class JunkListController {

  private JunkListService junkListService;
  private ClientService clientService;
  private JunkRepository junkRepository;

  public JunkListController(JunkListService junkListService,
                            ClientService clientService, JunkRepository junkRepository) {
    this.junkListService = junkListService;
    this.clientService = clientService;
    this.junkRepository = junkRepository;
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String date,
                                  @RequestParam(required = false) String clientId,
                                  @RequestParam String accountId) throws JsonProcessingException {
    List<JunkList> all = new ArrayList<>();
    if (date != null) {
      all = junkListService.findByDate(date, accountId);
      return new ResponseEntity<>(all, HttpStatus.OK);
    } else if (clientId != null) {
      all = junkListService.findByClientId(clientId, accountId);
    } else if (accountId != null)
      all = junkListService.all(accountId);
    return new ResponseEntity<>(all, HttpStatus.OK);
  }

/*  @GetMapping("des")
  public ResponseEntity<?> nan(@RequestParam String accountId) {
    List<Junk> collect = junkRepository.findAll().stream().filter(junk -> junk.getDate() == null)
        .collect(Collectors.toList());
    junkRepository.deleteAll(collect);
    return new ResponseEntity<>(collect, HttpStatus.OK);
  }*/

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable(required = false) String id,
                               @RequestParam String accountId) {
    if (id != null)
      return new ResponseEntity<>(junkListService.findById(id), HttpStatus.OK);
    return new ResponseEntity<>(junkListService.all(accountId), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody JunkList junkList,
                                @RequestParam String accountId) {
    if (junkList.getDate() == null)
      junkList.setDate(LocalDate.now().toString());
    JunkList purchase = junkListService.save(junkList, accountId);
    String clientId = purchase.getClientId();
    Optional<Client> byId = clientService.findById(clientId);
    if (byId.isPresent()) {
      Client client = byId.get();
      Double cashAdvance = Double.valueOf(client.getCashAdvance() != null ? client.getCashAdvance() : "0.0");
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
        junkListService.save(purchase, accountId);
        clientService.save(client);
      }
    }
    return new ResponseEntity<>(purchase, HttpStatus.CREATED);
  }


  @DeleteMapping
  public void delete(@RequestBody List<JunkList> junkLists) {
    junkListService.deleteAll(junkLists);
  }

}
