package io.rigor.junkshopserver.purchase;

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
public class PurchaseHandler implements PurchaseService<Purchase> {
  private PurchaseRepository purchaseRepository;
  private PurchaseItemRepository purchaseItemRepository;
  private MaterialService materialService;

  public PurchaseHandler(PurchaseRepository purchaseRepository,
                         PurchaseItemRepository purchaseItemRepository,
                         MaterialService materialService) {
    this.purchaseRepository = purchaseRepository;
    this.purchaseItemRepository = purchaseItemRepository;
    this.materialService = materialService;
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
