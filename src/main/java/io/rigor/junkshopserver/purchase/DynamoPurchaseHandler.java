package io.rigor.junkshopserver.purchase;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.rigor.junkshopserver.material.Material;
import io.rigor.junkshopserver.material.MaterialService;
import io.rigor.junkshopserver.purchase.PurchaseItem.PurchaseItem;
import io.rigor.junkshopserver.purchase.PurchaseItem.PurchaseItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DynamoPurchaseHandler implements PurchaseService<Purchase> {
  private PurchaseRepository purchaseRepository;
  private PurchaseItemRepository purchaseItemRepository;
  private MaterialService materialService;

  public DynamoPurchaseHandler(PurchaseRepository purchaseRepository,
                               PurchaseItemRepository purchaseItemRepository,
                               MaterialService materialService,
                               AmazonDynamoDB amazonDynamoDB) {
    this.purchaseRepository = purchaseRepository;
    this.purchaseItemRepository = purchaseItemRepository;
    this.materialService = materialService;
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = dynamoDBMapper
        .generateCreateTableRequest(Purchase.class);

    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(4L, 4L));

    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
    tableRequest = null;

    tableRequest = dynamoDBMapper.generateCreateTableRequest(PurchaseItem.class);
    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(4L, 4L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public List<Purchase> findAll() {
    return collectAsList(purchaseRepository.findAll());
  }

  @Override
  public Optional<Purchase> findById(String id) {
    return purchaseRepository.findById(id);
  }

  @Override
  public List<Purchase> findByDate(String date) {
    return purchaseRepository.findAllByDate(date);
  }

  @Override
  public void deleteById(String id) {
    purchaseRepository.deleteById(id);
  }

  @Override
  public void delete(Purchase purchase) {
    purchaseRepository.delete(purchase);
  }

  @Override
  public List<Purchase> saveAll(List<Purchase> t) {
    return collectAsList(purchaseRepository.saveAll(t));
  }

  @Override
  public Purchase save(Purchase purchase) {
    List<PurchaseItem> purchaseItems = purchase.getPurchaseItems();
    purchaseItemRepository.saveAll(purchaseItems);
    List<Material> materials = purchaseItems
        .stream()
        .map(purchaseItem -> {
          String materialName = purchaseItem.getMaterial();
          String weight = purchaseItem.getWeight();
          Material material = materialService.findByName(materialName);
          Double currentWeight = Double.valueOf(material.getWeight());
          Double takenWeight = Double.valueOf(weight);
          material.setWeight("" + (currentWeight - takenWeight));
          return material;
        })
        .collect(Collectors.toList());
    materialService.saveAll(materials);
    return purchaseRepository.save(purchase);
  }

  private <T> List<T> collectAsList(Iterable<T> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }
}
