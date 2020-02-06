package io.rigor.junkshopserver.expense;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rigor.junkshopserver.cash.Cash;
import io.rigor.junkshopserver.cash.CashService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
  public ExpenseService expenseService;
  private CashService cashService;
  private ObjectMapper mapper;

  public ExpenseController(ExpenseService expenseService,
                           CashService cashService) {
    this.expenseService = expenseService;
    this.cashService = cashService;
    mapper = new ObjectMapper();
  }


  @GetMapping()
  public ResponseEntity<?> getAllNoFilter(@RequestParam(required = false) String date,
                                          @RequestParam String accountId) {
    if (date != null)
      return new ResponseEntity<>(expenseService.findByDateAndAccountId(date, accountId), HttpStatus.OK);
    return new ResponseEntity<>(expenseService.all(accountId), HttpStatus.OK);
  }

  @GetMapping("/daily")
  public ResponseEntity<?> getForPage(@RequestParam String accountId) {
//    if (date != null)
//      return new ResponseEntity<>(expenseService.findByDateAndAccountId(date, accountId), HttpStatus.OK);
    Map<String, Object> map = new HashMap<>();
    List<Expense> fuk = expenseService.all(accountId);
    Cash today = cashService.getToday(accountId);
    map.put("expenses", fuk);
    map.put("cash", today);
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @PostMapping("/daily")
  public ResponseEntity<?> postpage(@RequestParam String accountId,
                                    @RequestBody Expense expense) {
//    if (date != null)
//      return new ResponseEntity<>(expenseService.findByDateAndAccountId(date, accountId), HttpStatus.OK);
    long start = System.currentTimeMillis();
    Map<String, Object> map = new HashMap<>();
    expenseService.save(expense);
    Cash today = cashService.calibrate(LocalDate.now().toString(), accountId);
    List<Expense> nametara = expenseService.all(accountId);
//    Cash today = cashService.getToday(accountId);
    map.put("expenses", nametara);
    map.put("cash", today);
    System.out.println("s " + (System.currentTimeMillis() - start));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @DeleteMapping("/daily")
  public ResponseEntity<?> deleteasj(@RequestBody Object body,
                                     @RequestParam String accountId) throws JsonProcessingException {
    if (body instanceof List) {
      List<Expense> expenses = mapper.readValue(mapper.writeValueAsString(body), new TypeReference<List<Expense>>() {});
      expenseService.deleteAll(expenses);
    } else {
      Expense expense = mapper.readValue(mapper.writeValueAsString(body), new TypeReference<Expense>() {});
      Optional<Expense> byId = expenseService.findById(expense.getId());
      if (byId.isPresent()) {
        expense = byId.get();
        expenseService.delete(expense);
//      cashService.deleteExpense(expense);
      }
    }
    Cash today = cashService.calibrate(LocalDate.now().toString(), accountId);
    Map<String, Object> map = new HashMap<>();
    List<Expense> fuk = expenseService.all(accountId);
//    Cash today = cashService.getToday(accountId);
    map.put("expenses", fuk);
    map.put("cash", today);
    return new ResponseEntity<>(map, HttpStatus.ACCEPTED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getAll(@PathVariable(required = false) String id,
                                  @RequestParam String accountId) {
    if (id != null)
      return new ResponseEntity<>(expenseService.findById(id), HttpStatus.OK);
    return new ResponseEntity<>(expenseService.all(accountId), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Object body,
                                @RequestParam String accountId) throws JsonProcessingException {
    if (body instanceof List) {
      ObjectMapper mapper = new ObjectMapper();
      String s = mapper.writeValueAsString(body);
      List<Expense> expenses = mapper.readValue(s, new TypeReference<List<Expense>>() {});
      return new ResponseEntity<>(expenseService.saveAll(expenses), HttpStatus.CREATED);
    }

    Expense expense = mapper.readValue(mapper.writeValueAsString(body), new TypeReference<Expense>() {});
//    cashService.addExpense(expense);
//    cashService.calibrateAll(accountId);
    cashService.calibrate(expense.getDate(), accountId);
    return new ResponseEntity<>(expenseService.save(expense), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable String id,
                                      @RequestParam String accountId) {
    Optional<Expense> byId = expenseService.findById(id);
    if (byId.isPresent()) {
//      cashService.deleteExpense(byId.get());
//      cashService.calibrateAll(accountId);
      cashService.calibrate(byId.get().getDate(), accountId);
      expenseService.deleteById(id);
    }
    return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
  }

  @DeleteMapping
  public ResponseEntity<?> delete(@RequestBody Object body,
                                  @RequestParam String accountId) throws JsonProcessingException {
    if (body instanceof List) {
      List<Expense> expenses = mapper.readValue(mapper.writeValueAsString(body), new TypeReference<List<Expense>>() {});
      expenseService.deleteAll(expenses);
//      cashService.calibrateAll(accountId);
      expenses.forEach(expense -> cashService.calibrate(expense.getDate(), accountId));
      return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }
    Expense expense = mapper.readValue(mapper.writeValueAsString(body), new TypeReference<Expense>() {});
    Optional<Expense> byId = expenseService.findById(expense.getId());
    if (byId.isPresent()) {
      expense = byId.get();
      expenseService.delete(expense);
//      cashService.deleteExpense(expense);
      cashService.calibrate(expense.getDate(), accountId);
//      cashService.calibrateAll(accountId);
    }
    return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
  }
}
