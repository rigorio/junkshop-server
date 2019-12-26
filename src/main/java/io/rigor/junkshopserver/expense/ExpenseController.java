package io.rigor.junkshopserver.expense;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
  public ExpenseService expenseService;

  public ExpenseController(ExpenseService expenseService) {
    this.expenseService = expenseService;
  }


  @GetMapping()
  public ResponseEntity<?> getAllNoFilter(@RequestParam(required = false) String date) {
    if (date != null)
      return new ResponseEntity<>(expenseService.findByDate(date), HttpStatus.OK);
    return new ResponseEntity<>(expenseService.all(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getAll(@PathVariable(required = false) String id) {
    if (id != null)
      return new ResponseEntity<>(expenseService.findById(id), HttpStatus.OK);
    return new ResponseEntity<>(expenseService.all(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Object body) throws JsonProcessingException {
    if (body instanceof List) {
      ObjectMapper mapper = new ObjectMapper();
      String s = mapper.writeValueAsString(body);
      List<Expense> expenses = mapper.readValue(s, new TypeReference<List<Expense>>() {});
      return new ResponseEntity<>(expenseService.saveAll(expenses), HttpStatus.CREATED);
    }
    Expense expense = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(body), new TypeReference<Expense>() {});
    return new ResponseEntity<>(expenseService.save(expense), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteById(@PathVariable String id) {
    expenseService.deleteById(id);
    return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
  }

  @DeleteMapping
  public ResponseEntity<?> delete(@RequestBody Expense expense) {
    expenseService.delete(expense);
    return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
  }
}
