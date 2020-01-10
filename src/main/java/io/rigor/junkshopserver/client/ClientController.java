package io.rigor.junkshopserver.client;

import io.rigor.junkshopserver.cash.Cash;
import io.rigor.junkshopserver.cash.CashService;
import io.rigor.junkshopserver.junk.junklist.JunkList;
import io.rigor.junkshopserver.junk.junklist.JunkListService;
import io.rigor.junkshopserver.sale.Sale;
import io.rigor.junkshopserver.sale.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
  private ClientService clientService;
  private CashService cashService;
  private SaleService<Sale> saleService;
  private JunkListService junkListService;

  public ClientController(ClientService clientService,
                          CashService cashService,
                          SaleService<Sale> saleService,
                          JunkListService junkListService) {
    this.clientService = clientService;
    this.cashService = cashService;
    this.saleService = saleService;
    this.junkListService = junkListService;
  }

  @GetMapping
  public ResponseEntity<?> all(@RequestParam String accountId) {
    return new ResponseEntity<>(clientService.all(accountId), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> findById(@PathVariable String id) {
    return new ResponseEntity<>(clientService.findById(id), HttpStatus.OK);
  }

  @GetMapping("/sp/{id}")
  public ResponseEntity<?> getSalesAndPurchases(@PathVariable String id) {
    List<Sale> sales = saleService.findByClientId(id);
    List<JunkList> purchases = junkListService.findByClientId(id);
    Client client = clientService.findById(id).get();
    ClientResponse response = ClientResponse.builder()
        .sales(sales)
        .purchases(purchases)
        .client(client)
        .build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/loan")
  public ResponseEntity<?> loan(@RequestParam String clientId,
                                @RequestParam String amount,
                                @RequestParam String accountId) {
    Optional<Client> byId = clientService.findById(clientId);
    if (byId.isPresent()) {
      Client client = byId.get();
      if (Double.valueOf(client.getCashAdvance()) > 0)
        amount = "" + (Double.valueOf(amount) + Double.valueOf(client.getCashAdvance()));
      client.setCashAdvance(amount);
      clientService.save(client);
      Cash cashToday = cashService.getToday(accountId);
      Double capital = Double.valueOf(cashToday.getCapital());
      Double loanAmount = Double.valueOf(amount);
      double capitalLeft = capital - loanAmount;
      cashToday.setCapital("" + capitalLeft);
      cashService.updateCapital(cashToday, accountId);
      return new ResponseEntity<>(client, HttpStatus.OK);
    }
    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/pay")
  public ResponseEntity<?> payLoan(@RequestParam String clientId,
                                   @RequestParam String amount,
                                   @RequestParam String accountId) {
    Optional<Client> byId = clientService.findById(clientId);
    if (byId.isPresent()) {
      Client client = byId.get();
      Double cashAdvance = Double.valueOf(client.getCashAdvance());
      client.setCashAdvance("" + (cashAdvance - Double.valueOf(amount)));
      clientService.save(client);
      Cash cashToday = cashService.getToday(accountId);
      Double capital = Double.valueOf(cashToday.getCapital());
      Double loanAmount = Double.valueOf(amount);
      double capitalLeft = capital + loanAmount;
      cashToday.setCapital("" + capitalLeft);
      cashService.updateCapital(cashToday, accountId);
      return new ResponseEntity<>(client, HttpStatus.OK);
    }
    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Client client) {
    if (client.getCashAdvance() == null)
      client.setCashAdvance("0.0");
    client.setContact(client.getContact() == null ? "" : client.getContact());
    client.setAddress(client.getAddress() != null ? client.getAddress() : "");
    return new ResponseEntity<>(clientService.save(client), HttpStatus.CREATED);
  }


}
