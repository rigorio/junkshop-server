package io.rigor.junkshopserver.account;

import io.rigor.junkshopserver.cash.Cash;
import io.rigor.junkshopserver.cash.CashRepository;
import io.rigor.junkshopserver.client.Client;
import io.rigor.junkshopserver.client.ClientRepository;
import io.rigor.junkshopserver.customProperties.CustomProperty;
import io.rigor.junkshopserver.customProperties.CustomPropertyRepository;
import io.rigor.junkshopserver.expense.Expense;
import io.rigor.junkshopserver.expense.ExpenseRepository;
import io.rigor.junkshopserver.junk.junklist.JunkList;
import io.rigor.junkshopserver.junk.junklist.JunkListRepository;
import io.rigor.junkshopserver.material.Material;
import io.rigor.junkshopserver.material.MaterialRepository;
import io.rigor.junkshopserver.sale.Sale;
import io.rigor.junkshopserver.sale.SaleRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/c")
public class CController {
  private AccountService accountService;
  private CashRepository cashRepository;
  private ClientRepository clientRepository;
  private CustomPropertyRepository customPropertyRepository;
  private ExpenseRepository expenseRepository;
  private JunkListRepository junkListRepository;
  private MaterialRepository materialRepository;
  private SaleRepository saleRepository;

  public CController(AccountService accountService,
                     CashRepository cashRepository,
                     ClientRepository clientRepository,
                     CustomPropertyRepository customPropertyRepository,
                     ExpenseRepository expenseRepository,
                     JunkListRepository junkListRepository,
                     MaterialRepository materialRepository,
                     SaleRepository saleRepository) {
    this.accountService = accountService;
    this.cashRepository = cashRepository;
    this.clientRepository = clientRepository;
    this.customPropertyRepository = customPropertyRepository;
    this.expenseRepository = expenseRepository;
    this.junkListRepository = junkListRepository;
    this.materialRepository = materialRepository;
    this.saleRepository = saleRepository;
  }

  @GetMapping
  public void kwan(@RequestParam String secretKey) {
    String id = accountService.check("admin", "pass123").get().getId();
    List<Cash> allCash = cashRepository.findAll();
    allCash.forEach(cash -> cash.setAccountId(id));
    cashRepository.saveAll(allCash);

    List<Client> clients = clientRepository.findAll();
    clients.forEach(o -> o.setAccountId(id));
    clientRepository.saveAll(clients);

    List<CustomProperty> customProperties = customPropertyRepository.findAll();
    customProperties.forEach(o -> o.setAccountId(id));
    customPropertyRepository.saveAll(customProperties);

    List<Expense> all = expenseRepository.findAll();
    all.forEach(o -> o.setAccountId(id));
    expenseRepository.saveAll(all);

    List<JunkList> all1 = junkListRepository.findAll();
    all1.forEach(o -> o.setAccountId(id));
    junkListRepository.saveAll(all1);

    List<Material> all2 = materialRepository.findAll();
    all2.forEach(o -> o.setAccountId(id));
    materialRepository.saveAll(all2);

    List<Sale> all3 = saleRepository.findAll();
    all3.forEach(o -> o.setAccountId(id));
    saleRepository.saveAll(all3);

  }
}
