package io.rigor.junkshopserver.account;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountHandler implements AccountService {
  private AccountRepository accountRepository;

  public AccountHandler(AccountRepository accountRepository,
                        AmazonDynamoDB amazonDynamoDB) {
    this.accountRepository = accountRepository;
    DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = mapper.generateCreateTableRequest(Account.class);
    tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public void save(Account account) {
    accountRepository.save(account);
  }

  @Override
  public Optional<Account> check(String username, String password) {
    Optional<Account> account = accountRepository.findByUsernameAndPassword(username, password);
    return account;
  }

  @Override
  public List<Account> all() {
    return accountRepository.findAll();
  }

}
