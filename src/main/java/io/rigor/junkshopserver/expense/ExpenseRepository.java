package io.rigor.junkshopserver.expense;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ExpenseRepository extends CrudRepository<Expense, String> {
  List<Expense> findAll();
//  List<Expense> saveAll(List<Expense> expenses);
  List<Expense> findByDate(String date);
}
