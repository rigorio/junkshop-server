package io.rigor.junkshopserver.cash;

import java.util.List;
import java.util.Optional;

/**
 * @see DynamoCashSwindler
 */
public interface CashService {
  List<Cash> allDailyCash(String accountId);

  Optional<Cash> findByDateAcc(String date, String accountId);

  Cash getToday(String accountId);

  Cash calibrate(String date, String accountId);

  void calibrateAll(String accountId);

  Cash updateCapital(Cash cash, String accountId);

  Cash what(Cash cash);
}
