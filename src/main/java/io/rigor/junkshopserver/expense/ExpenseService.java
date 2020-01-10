package io.rigor.junkshopserver.expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {
  List<Expense> all(String accountId);

  Optional<Expense> findById(String id);

  List<Expense> findByDateAndAccountId(String date, String accountId);

  Expense save(Expense expense);

  List<Expense> saveAll(List<Expense> expenses);

  void delete(Expense expense);

  void deleteAll();

  void deleteById(String id);

  void deleteAll(List<Expense> expenses);
}
