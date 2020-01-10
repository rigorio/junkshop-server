package io.rigor.junkshopserver.customProperties;

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
public class DynamoCustomPropertyHandler implements CustomPropertyService {
  private CustomPropertyRepository customPropertyRepository;

  public DynamoCustomPropertyHandler(CustomPropertyRepository customPropertyRepository,
                                     AmazonDynamoDB amazonDynamoDB) {
    this.customPropertyRepository = customPropertyRepository;
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(CustomProperty.class);
    tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public List<CustomProperty> findAll(String accountId) {
    return customPropertyRepository.findAllByAccountId(accountId);
  }

  @Override
  public Optional<CustomProperty> findByPropertyAndAccountID(String property, String accountId) {
    return customPropertyRepository.findByPropertyAndAccountId(property, accountId);
  }

  @Override
  public Optional<CustomProperty> findById(String id) {
    return customPropertyRepository.findById(id);
  }

  @Override
  public void deleteById(String id) {
    customPropertyRepository.deleteById(id);
  }

  @Override
  public void delete(CustomProperty CustomProperty) {
    customPropertyRepository.delete(CustomProperty);
  }

  @Override
  public CustomProperty save(CustomProperty customProperty) {
    return customPropertyRepository.save(customProperty);
  }

  @Override
  public List<CustomProperty> saveAll(List<CustomProperty> CustomPropertys) {
    return collectAsList(customPropertyRepository.saveAll(CustomPropertys));
  }

  private <T> List<T> collectAsList(Iterable<T> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }


}
