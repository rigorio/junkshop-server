package io.rigor.junkshopserver.junk.junklist;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.rigor.junkshopserver.cash.CashService;
import io.rigor.junkshopserver.junk.Junk;
import io.rigor.junkshopserver.junk.JunkService;
import io.rigor.junkshopserver.material.Material;
import io.rigor.junkshopserver.material.MaterialService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DynamoJunkListHandler implements JunkListService {

  private JunkService junkService;
  private JunkListRepository junkListRepository;
  private MaterialService materialService;
  private CashService cashService;

  public DynamoJunkListHandler(JunkListRepository junkListRepository,
                               MaterialService materialService,
                               AmazonDynamoDB amazonDynamoDB,
                               JunkService junkService,
                               CashService cashService) {
    this.junkListRepository = junkListRepository;
    this.materialService = materialService;
    this.junkService = junkService;
    this.cashService = cashService;
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(JunkList.class);
    tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public List<JunkList> all() {
    return junkListRepository.findAll();
  }

  @Override
  public Optional<JunkList> findById(String id) {
    return junkListRepository.findById(id);
  }

  @Override
  public List<JunkList> findByDate(String date) {
    return junkListRepository.findAllByDate(date);
  }

  @Override
  public List<JunkList> saveAll(List<JunkList> list) {
    return collectAsList(junkListRepository.saveAll(list));
  }

  @Override
  public JunkList save(JunkList junkList) {
    List<Junk> purchaseItems = junkList.getPurchaseItems();
    junkService.saveAll(purchaseItems);
    purchaseItems.forEach(junk -> {
      junk.setDate(LocalDate.now().toString());
      String materialName = junk.getMaterial();
      Optional<Material> byName = materialService.findByName(materialName);
      if (byName.isPresent()) {
        String weight = junk.getWeight();
        materialService.addWeight(byName.get(), weight);
      } else {
        Material newMaterial = Material.builder()
            .material(materialName)
            .standardPrice(junk.getPrice())
            .weight(junk.getWeight())
            .build();
        materialService.save(newMaterial);
      }
      Junk savedJunk = junkService.save(junk);
      cashService.addPurchases(savedJunk);
    });
    return junkListRepository.save(junkList);
  }

  private <T> List<T> collectAsList(Iterable<T> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }
}
