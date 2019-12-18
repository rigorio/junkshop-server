package io.rigor.junkshopserver.purchase;

import io.rigor.junkshopserver.purchase.PurchaseItem.PurchaseItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    return purchaseRepository.findAll();
  }

  @Override
  public List<Purchase> findAll(Sort sort) {
    return purchaseRepository.findAll(sort);
  }

  @Override
  public Optional<Purchase> findById(Long id) {
    return purchaseRepository.findById(id);
  }

  @Override
  public List<Purchase> findByDate(String date) {
    return purchaseRepository.findAllByDate(date);
  }

  @Override
  public void deleteById(Long id) {
    purchaseRepository.deleteById(id);
  }

  @Override
  public void delete(Purchase purchase) {
    purchaseRepository.delete(purchase);
  }

  @Override
  public List<Purchase> saveAll(List<Purchase> t) {
    return purchaseRepository.saveAll(t);
  }

  @Override
  public Purchase save(Purchase purchase) {
    purchaseItemRepository.saveAll(purchase.getPurchaseItems());
    return purchaseRepository.save(purchase);
  }
}
