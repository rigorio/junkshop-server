package io.rigor.junkshopserver.purchase;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseHandler implements PurchaseService<Purchase> {
  private PurchaseRepository purchaseRepository;

  public PurchaseHandler(PurchaseRepository purchaseRepository) {
    this.purchaseRepository = purchaseRepository;
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
    return purchaseRepository.save(purchase);
  }
}
