package io.rigor.junkshopserver.expense;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DynamoExpenseHandler implements ExpenseService {
  private ExpenseRepository expenseRepository;

  public DynamoExpenseHandler(ExpenseRepository expenseRepository,
                              AmazonDynamoDB amazonDynamoDB) {
    this.expenseRepository = expenseRepository;
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(Expense.class);
    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(1L, 1L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public List<Expense> all(String accountId) {
    return expenseRepository.findAllByAccountId(accountId);
  }

  @Override
  public Optional<Expense> findById(String id) {
    return expenseRepository.findById(id);
  }

  @Override
  public List<Expense> findByDateAndAccountId(String date, String accountId) {
    return expenseRepository.findByDateAndAccountId(date, accountId);
  }

  @Override
  public Expense save(Expense expense) {
    return expenseRepository.save(expense);
  }

  @Override
  public List<Expense> saveAll(List<Expense> expenses) {
    return collectAsList(expenseRepository.saveAll(expenses));
  }

  @Override
  public void delete(Expense expense) {
    expenseRepository.delete(expense);
  }

  @Override
  public void deleteAll() {
    expenseRepository.deleteAll();
  }

  @Override
  public void deleteById(String id) {
    expenseRepository.deleteById(id);
  }

  @Override
  public void deleteAll(List<Expense> expenses) {
    expenseRepository.deleteAll(expenses);
  }

  private List<Expense> collectAsList(Iterable<Expense> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }
}
