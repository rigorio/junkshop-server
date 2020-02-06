package io.rigor.junkshopserver.material;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DynamoMaterialHandler implements MaterialService {
  private MaterialRepository repository;

  public DynamoMaterialHandler(MaterialRepository repository,
                               AmazonDynamoDB amazonDynamoDB) {
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = dynamoDBMapper
        .generateCreateTableRequest(Material.class);

    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(1L, 1L));

    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
    this.repository = repository;
  }

  @Override
  public List<Material> findAll(String accountId) {
    return repository.findAllByAccountId(accountId);
//    return collectAsList(repository.findAll());
  }

  @Override
  public Optional<Material> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public void deleteById(String id) {
    repository.deleteById(id);
  }

  @Override
  public void delete(Material material) {
    repository.delete(material);
  }

  @Override
  public List<Material> saveAll(List<Material> materials) {
    for (Material material : materials) {
      if (material == null)
        return new ArrayList<>();
      if (material.getWeight() == null)
        material.setWeight("0.0");
    }
    return collectAsList(repository.saveAll(materials));
  }

  @Override
  public Material save(Material material) {
    if (material.getWeight() == null)
      material.setWeight("0.0");
    return repository.save(material);
  }

  @Override
  public Material addWeight(Material m, String weight) {
    Double currentWeight = Double.valueOf(m.getWeight() == null ? "0.0" : m.getWeight());
    Double additionalWeight = Double.valueOf(weight);
    double newWeight = currentWeight + additionalWeight;
    m.setWeight("" + newWeight);
    return save(m);
  }

  @Override
  public Optional<Material> findByName(String materialName, String accountId) {
    return repository.findByMaterialAndAccountId(materialName, accountId);
//    return repository.findAllByMaterial(materialName);
//    return allByMaterial.orElse(new Material());
  }

  private <T> List<T> collectAsList(Iterable<T> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }
}
