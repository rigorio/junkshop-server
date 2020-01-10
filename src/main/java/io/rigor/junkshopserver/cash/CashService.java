package io.rigor.junkshopserver.cash;

import io.rigor.junkshopserver.expense.Expense;
import io.rigor.junkshopserver.junk.Junk;
import io.rigor.junkshopserver.sale.Sale;

import java.util.List;

public interface CashService {
  List<Cash> allDailyCash(String accountId);

  Cash getToday(String accountId);

  void calibrateAll(String accountId);

  Cash updateCapital(Cash cash, String accountId);
}
