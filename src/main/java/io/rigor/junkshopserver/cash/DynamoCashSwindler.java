package io.rigor.junkshopserver.cash;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.rigor.junkshopserver.expense.Expense;
import io.rigor.junkshopserver.expense.ExpenseService;
import io.rigor.junkshopserver.junk.Junk;
import io.rigor.junkshopserver.junk.JunkService;
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

  public DynamoCashSwindler(CashRepository cashRepository,
                            AmazonDynamoDB amazonDynamoDB,
                            JunkService junkService,
                            SaleService<Sale> saleService, ExpenseService expenseService) {
    this.cashRepository = cashRepository;
    this.junkService = junkService;
    this.saleService = saleService;
    this.expenseService = expenseService;
    DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = mapper.generateCreateTableRequest(Cash.class);
    tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public List<Cash> allDailyCash() {
    return cashRepository.findAll();
  }

  @Override
  public void addSales(Sale sale) {
 /*   String totalPrice = sale.getTotalPrice();
    String date = sale.getDate();
    Optional<Cash> byDate = cashRepository.findByDate(date);
    if (byDate.isPresent()) {
      Cash cash = byDate.get();
      cash.setSales("" + (toDouble(cash.getSales()) + toDouble(totalPrice)));
      Double cashOnHand = getCashOnHand(cash);
      cash.setCashOnHand("" + cashOnHand);
      cashRepository.save(cash);
    } else {
      Cash cash = createCashItem(date);
      cash.setSales(totalPrice);
      cash.setCashOnHand("" + getCashOnHand(cash));
      cashRepository.save(cash);
    }*/
    calibrateAll();
  }

  @Override
  public void addPurchases(Junk junk) {
/*    String totalPrice = junk.getTotalPrice();
    String date = junk.getDate();
    Optional<Cash> byDate = cashRepository.findByDate(date);
    if (byDate.isPresent()) {
      Cash cash = byDate.get();
      cash.setPurchases("" + (toDouble(cash.getPurchases()) + toDouble(totalPrice)));
      Double cashOnHand = getCashOnHand(cash);
      cash.setCashOnHand("" + cashOnHand);
      cashRepository.save(cash);
    } else {
      Cash cash = createCashItem(date);
      cash.setPurchases(totalPrice);
      cash.setCashOnHand("" + getCashOnHand(cash));
      cashRepository.save(cash);
    }*/
    calibrateAll();
  }

  public void calibrateAll() {
    List<Cash> cashList = allDailyCash();
    List<String> dates = cashList.stream().map(Cash::getDate).collect(Collectors.toList());
    List<Cash> updatedKaching = dates.stream()
        .map(date -> {
          Optional<Cash> junkByDate = cashRepository.findByDate(date);

          List<Junk> junk = junkService.findByDate(date);
          double purchasesSum = junk.stream().mapToDouble(j -> Double.valueOf(j.getTotalPrice())).sum();

          List<Sale> sales = saleService.findByDate(date);
          double salesSum = sales.stream().mapToDouble(s -> Double.valueOf(s.getTotalPrice())).sum();

          List<Expense> expenses = expenseService.findByDate(date);
          double expenseSum = expenses.stream().mapToDouble(e -> Double.valueOf(e.getAmount())).sum();


          Cash cash = junkByDate.orElseGet(Cash::new);
          cash.setSales("" + salesSum);
          cash.setPurchases("" + purchasesSum);
          cash.setExpenses("" + expenseSum);
          cash.setCashOnHand("" + getCashOnHand(cash));
          return cash;
        })
        .collect(Collectors.toList());
    cashRepository.saveAll(updatedKaching);
  }

  /**
   * TODO ok great you also delete expenses so how about that this is very tiresome
   */
  @Override
  public void addExpense(Expense expense) {
  /*  String amount = expense.getAmount();
    String date = expense.getDate();
    Optional<Cash> byDate = cashRepository.findByDate(date);
    if (byDate.isPresent()) {
      Cash cash = byDate.get();
      cash.setExpenses("" + (toDouble(cash.getExpenses()) + toDouble(amount)));
      cash.setCashOnHand("" + getCashOnHand(cash));
      cashRepository.save(cash);
    } else {
      Cash cash = createCashItem(date);
      cash.setExpenses(amount);
      cash.setCashOnHand("" + getCashOnHand(cash));
      cashRepository.save(cash);
    }*/
    calibrateAll();
  }

  @Override
  public void deleteExpense(Expense expense) {
  /*  String amount = expense.getAmount();
    String date = expense.getDate();
    Optional<Cash> byDate = cashRepository.findByDate(date);
    if (byDate.isPresent()) {
      Cash cash = byDate.get();
      cash.setExpenses("" + (toDouble(cash.getExpenses()) - toDouble(amount)));
      cash.setCashOnHand("" + getCashOnHand(cash));
      cashRepository.save(cash);
    } else {
      Cash cash = createCashItem(date);
      cash.setExpenses(amount);
      cash.setCashOnHand("" + getCashOnHand(cash));
      cashRepository.save(cash);
    }*/
    calibrateAll();
  }

  @Override
  public Cash updateCapital(Cash cash) {
    String capital = cash.getCapital();
    String date = cash.getDate();
    Optional<Cash> byDate = cashRepository.findByDate(date);
    if (byDate.isPresent()) {
      Cash c = byDate.get();
      c.setCapital(capital);
      c.setCashOnHand("" + (getCashOnHand(c)));
      Cash save = cashRepository.save(c);
      calibrateAll();
      return save;
    } else {
      cash.setCashOnHand("" + getCashOnHand(cash));
      Cash save = cashRepository.save(cash);
      calibrateAll();
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
