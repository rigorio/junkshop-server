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

@Service
public class DynamoCashSwindler implements CashService {
  private CashRepository cashRepository;
  private JunkService junkService;
  private SaleService<Sale> saleService;
  private ExpenseService expenseService;

  private JunkListRepository repository;

  public DynamoCashSwindler(CashRepository cashRepository,
                            AmazonDynamoDB amazonDynamoDB,
                            JunkService junkService,
                            SaleService<Sale> saleService,
                            ExpenseService expenseService,
                            JunkListRepository repository) {
    this.cashRepository = cashRepository;
    this.junkService = junkService;
    this.saleService = saleService;
    this.expenseService = expenseService;
    this.repository = repository;
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
  public Cash getToday(String accountId) {
    List<Cash> cashList = allDailyCash(accountId);
    Optional<Cash> any = cashList.stream().filter(c -> c.getDate().equals(LocalDate.now().toString())).findAny();
    Cash cash = any.orElseGet(Cash::new);
    return updateCapital(cash, accountId);
  }

  @Override
  public void calibrateAll(String accountId) {
    List<Cash> cashList = allDailyCash(accountId);
    List<String> dates = cashList.stream().map(Cash::getDate).collect(Collectors.toList());
    List<Cash> updatedKaching = dates.stream()
        .map(date -> {
          Optional<Cash> junkByDate = cashRepository.findByDate(date);

          List<JunkList> byDate = repository.findAllByDate(date);
          double totalSum = byDate.stream().mapToDouble(j -> Double.valueOf(j.getTotalPrice())).sum();

          List<Sale> sales = saleService.findByDate(date);
          double salesSum = sales.stream().mapToDouble(s -> Double.valueOf(s.getTotalPrice())).sum();

          List<Expense> expenses = expenseService.findByDate(date);
          double expenseSum = expenses.stream().mapToDouble(e -> Double.valueOf(e.getAmount())).sum();


          Cash cash = junkByDate.orElseGet(Cash::new);
          cash.setSales("" + salesSum);
          cash.setPurchases("" + totalSum);
          cash.setExpenses("" + expenseSum);
          cash.setCashOnHand("" + getCashOnHand(cash));
          return cash;
        })
        .collect(Collectors.toList());
    cashRepository.saveAll(updatedKaching);
  }

  @Override
  public Cash updateCapital(Cash cash, String accountId) {
    String capital = cash.getCapital();
    String date = cash.getDate();
    Optional<Cash> byDate = cashRepository.findByDate(date);
    if (byDate.isPresent()) {
      Cash c = byDate.get();
      c.setCapital(capital);
      c.setCashOnHand("" + (getCashOnHand(c)));
      Cash save = cashRepository.save(c);
      calibrateAll(accountId);
      return save;
    } else {
      cash.setCashOnHand("" + getCashOnHand(cash));
      Cash save = cashRepository.save(cash);
      calibrateAll(accountId);
      return save;
    }
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
