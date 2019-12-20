package io.rigor.junkshopserver.sales;

import io.rigor.junkshopserver.purchase.Purchase;
import io.rigor.junkshopserver.purchase.PurchaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class SalesHandler implements SalesService {
  private PurchaseService<Purchase> purchaseService;

  public SalesHandler(PurchaseService<Purchase> purchaseService) {
    this.purchaseService = purchaseService;
  }

  @Override
  public List<SalesEntity> viewByMonth() {
    List<Purchase> purchases = purchaseService.findAll();
    List<SalesEntity> sales = new ArrayList<>();
    for (Purchase purchase : purchases) {
      LocalDate date = LocalDate.parse(purchase.getDate());
      String span = date.getMonth() + " " + date.getYear();
      Optional<SalesEntity> any = sales.stream()
          .filter(sale -> sale.getSpan().equals(span))
          .findAny();
      SalesEntity sale = new SalesEntity();
      if (any.isPresent()) {
        sale = any.get();
        Double totalSale = Double.valueOf(sale.getSales());
        sale.setSales("" + (totalSale + Double.valueOf(purchase.getTotalPrice())));
      } else {
        sale.setSpan(span);
        sale.setSales(purchase.getTotalPrice());
        sales.add(sale);
      }
    }
    return sales;
  }
}
