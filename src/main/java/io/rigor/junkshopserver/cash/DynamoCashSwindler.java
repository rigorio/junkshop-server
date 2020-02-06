package io.rigor.junkshopserver.cash;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.rigor.junkshopserver.expense.Expense;
import io.rigor.junkshopserver.expense.ExpenseService;
import io.rigor.junkshopserver.junk.JunkService;
import io.rigor.junkshopserver.junk.junklist.JunkList;
import io.rigor.junkshopserver.junk.junklist.JunkListRepository;
import io.rigor.junkshopserver.sale.Sale;
import io.rigor.junkshopserver.sale.SaleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.rigor.junkshopserver.Utils.roundToTwo;

@Service
public class DynamoCashSwindler implements CashService {
  private CashRepository cashRepository;
  private JunkService junkService;
  private SaleService<Sale> saleService;
  private ExpenseService expenseService;

  private JunkListRepository junkListRepository;

  public DynamoCashSwindler(CashRepository cashRepository,
                            AmazonDynamoDB amazonDynamoDB,
                            JunkService junkService,
                            SaleService<Sale> saleService,
                            ExpenseService expenseService,
                            JunkListRepository junkListRepository) {
    this.cashRepository = cashRepository;
    this.junkService = junkService;
    this.saleService = saleService;
    this.expenseService = expenseService;
    this.junkListRepository = junkListRepository;
    DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = mapper.generateCreateTableRequest(Cash.class);
    tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public List<Cash> allDailyCash(String accountId) {
    return cashRepository.findAllByAccountId(accountId);
  }

  @Override
  public Optional<Cash> findByDateAcc(String date, String accountId) {
    return cashRepository.findByDateAndAccountId(date, accountId);
  }

  @Override
  public Cash getToday(String accountId) {
    List<Cash> cashList = allDailyCash(accountId);
    Optional<Cash> any = cashList.stream().filter(c -> c.getDate().equals(LocalDate.now().toString())).findAny();
    Cash cash = any.orElseGet(Cash::new);
    return updateCapital(cash, accountId);
  }

  @Override
  public Cash calibrate(String date, String accountId) {
    Optional<Cash> byDateAndAccountId = cashRepository.findByDateAndAccountId(date, accountId);

    Cash cash = byDateAndAccountId.orElse(new Cash());
    List<JunkList> byDate = junkListRepository.findAllByDateAndAccountId(date, accountId);
    double totalPurchases = byDate.stream().mapToDouble(j -> {

      return Double.valueOf(j.getTotalPrice());
    }).sum();

    List<Sale> sales = saleService.findByDate(date, accountId);
    double totalSales = sales.stream().mapToDouble(s -> Double.valueOf(s.getTotalPrice())).sum();

    List<Expense> expenses = expenseService.findByDateAndAccountId(date, accountId);
    double totalExpenses = expenses.stream().mapToDouble(e -> Double.valueOf(e != null && e.getAmount() != null ? e.getAmount() : "0.0")).sum();

    cash.setSales(roundToTwo(totalSales));
    cash.setPurchases(roundToTwo(totalPurchases));
    cash.setExpenses(roundToTwo(totalExpenses));
    cash.setCashOnHand(roundToTwo(getCashOnHand(cash)));
    return cashRepository.save(cash);
  }

  @Override
  public void calibrateAll(String accountId) {
    List<Cash> cashList = allDailyCash(accountId);
    List<String> dates = cashList.stream().map(Cash::getDate).collect(Collectors.toList());
    List<Cash> updatedKaching = dates.stream()
        .map(date -> {
          Optional<Cash> junkByDate = cashRepository.findByDateAndAccountId(date, accountId);

          List<JunkList> byDate = junkListRepository.findAllByDateAndAccountId(date, accountId);
          double totalPurchases = byDate.stream().mapToDouble(j -> {

            return Double.valueOf(j.getTotalPrice());
          }).sum();

          List<Sale> sales = saleService.findByDate(date, accountId);
          double totalSales = sales.stream().mapToDouble(s -> Double.valueOf(s.getTotalPrice())).sum();

          List<Expense> expenses = expenseService.findByDateAndAccountId(date, accountId);
          double totalExpenses = expenses.stream().mapToDouble(e -> Double.valueOf(e != null && e.getAmount() != null ? e.getAmount() : "0.0")).sum();


          Cash cash = junkByDate.orElseGet(Cash::new);
          cash.setSales(roundToTwo(totalSales));
          cash.setPurchases(roundToTwo(totalPurchases));
          cash.setExpenses(roundToTwo(totalExpenses));
          cash.setCashOnHand(roundToTwo(getCashOnHand(cash)));
          return cash;
        })
        .collect(Collectors.toList());
    cashRepository.saveAll(updatedKaching);
  }

  @Override
  public Cash updateCapital(Cash cash, String accountId) {
    String capital = cash.getCapital();
    String date = cash.getDate();
    Optional<Cash> byDate = cashRepository.findByDateAndAccountId(date, accountId);
    if (byDate.isPresent()) {
      Cash c = byDate.get();
      c.setCapital(capital);
      c.setCashOnHand("" + (getCashOnHand(c)));
      cashRepository.save(c);
      return calibrate(date, accountId);
    } else {
      cash.setCashOnHand("" + getCashOnHand(cash));
      cashRepository.save(cash);
      return calibrate(date, accountId);
    }
  }

  @Override
  public Cash what(Cash cash) {
    return cashRepository.save(cash);
  }

  private Double getCashOnHand(Cash cash) {
    Double capital = toDouble(cash.getCapital());
    Double sales = toDouble(cash.getSales());
    Double purchases = toDouble(cash.getPurchases());
    Double expenses = toDouble(cash.getExpenses());
    double assets = capital + sales;
    double liability = purchases + expenses;
    return assets - liability;
  }

  private Double toDouble(String string) {
    return Double.valueOf(string != null && string.length() > 0 ? string : "0.0");
  }

  private Cash createCashItem(String date) {
    Cash cash = new Cash();
    cash.setDate(date);
    LocalDate dateBefore = LocalDate.parse(date).minusDays(1L);
    Optional<Cash> yesterdayCash = cashRepository.findByDate(dateBefore.toString());
    if (yesterdayCash.isPresent()) {
      Cash yesterday = yesterdayCash.get();
      String capital = yesterday.getCashOnHand();
      cash.setCapital(capital);
    }
    return cash;
  }
}
