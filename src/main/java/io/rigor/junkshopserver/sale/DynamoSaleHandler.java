package io.rigor.junkshopserver.sale;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import io.rigor.junkshopserver.material.Material;
import io.rigor.junkshopserver.material.MaterialService;
import io.rigor.junkshopserver.sale.saleitem.SaleItem;
import io.rigor.junkshopserver.sale.saleitem.SaleItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DynamoSaleHandler implements SaleService<Sale> {
  private SaleRepository saleRepository;
  private SaleItemRepository saleItemRepository;
  private MaterialService materialService;

  public DynamoSaleHandler(SaleRepository saleRepository,
                           SaleItemRepository saleItemRepository,
                           MaterialService materialService,
                           AmazonDynamoDB amazonDynamoDB) {
    this.saleRepository = saleRepository;
    this.saleItemRepository = saleItemRepository;
    this.materialService = materialService;
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = dynamoDBMapper
        .generateCreateTableRequest(Sale.class);

    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(4L, 4L));

    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
    tableRequest = null;

    tableRequest = dynamoDBMapper.generateCreateTableRequest(SaleItem.class);
    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(4L, 4L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public List<Sale> findAll() {
    return collectAsList(saleRepository.findAll());
  }

  @Override
  public Optional<Sale> findById(String id) {
    return saleRepository.findById(id);
  }

  @Override
  public List<Sale> findByDate(String date) {
    return saleRepository.findAllByDate(date);
  }

  @Override
  public void deleteById(String id) {
    saleRepository.deleteById(id);
  }

  @Override
  public void delete(Sale sale) {
    saleRepository.delete(sale);
  }

  @Override
  public List<Sale> saveAll(List<Sale> t) {
    return collectAsList(saleRepository.saveAll(t));
  }

  @Override
  public Sale save(Sale sale) {
    List<SaleItem> saleItems = sale.getSaleItems();
    saleItemRepository.saveAll(saleItems);
    List<Material> materials = saleItems
        .stream()
        .map(saleItem -> {
          String materialName = saleItem.getMaterial();
          String weight = saleItem.getWeight();
          Material material = materialService.findByName(materialName);
          Double currentWeight = Double.valueOf(material.getWeight());
          Double takenWeight = Double.valueOf(weight);
          material.setWeight("" + (currentWeight - takenWeight));
          return material;
        })
        .collect(Collectors.toList());
    materialService.saveAll(materials);
    return saleRepository.save(sale);
  }

  private <T> List<T> collectAsList(Iterable<T> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }
}
