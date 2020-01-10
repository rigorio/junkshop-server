package io.rigor.junkshopserver.cash;

import io.rigor.junkshopserver.expense.Expense;
import io.rigor.junkshopserver.junk.Junk;
import io.rigor.junkshopserver.sale.Sale;

import java.util.List;

public interface CashService {
  List<Cash> allDailyCash();

  Cash getToday();

  void addSales(Sale sale);

  void addPurchases(Junk junk);

  void addExpense(Expense expense);

  void deleteExpense(Expense expense);

  Cash updateCapital(Cash cash);
}
