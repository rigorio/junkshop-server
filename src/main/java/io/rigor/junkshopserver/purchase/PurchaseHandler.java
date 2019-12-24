package io.rigor.junkshopserver.purchase;

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

  public PurchaseHandler(PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository) {
    this.purchaseRepository = purchaseRepository;
    this.purchaseItemRepository = purchaseItemRepository;
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
    purchaseItemRepository.saveAll(purchase.getPurchaseItems());
    return purchaseRepository.save(purchase);
  }

  private <T>List<T> collectAsList(Iterable<T> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }
}
