package io.rigor.junkshopserver.salessummary;

import io.rigor.junkshopserver.sale.Sale;
import io.rigor.junkshopserver.sale.SaleService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalesHandler implements SalesService {
  private SaleService<Sale> saleService;

  public SalesHandler(SaleService<Sale> saleService) {
    this.saleService = saleService;
  }

  @Override
  public List<SaleSummary> viewByMonth(String accountId) {
    List<Sale> purchases = saleService.findAll(accountId);
    List<SaleSummary> sales = new ArrayList<>();
    for (Sale purchase : purchases) {
      LocalDate date = LocalDate.parse(purchase.getDate());
      String span = date.getMonth() + " " + date.getYear();
      Optional<SaleSummary> any = sales.stream()
          .filter(sale -> sale.getSpan().equals(span))
          .findAny();
      SaleSummary sale = new SaleSummary();
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
