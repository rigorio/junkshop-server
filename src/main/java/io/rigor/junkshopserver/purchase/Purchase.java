package io.rigor.junkshopserver.purchase;

import io.rigor.junkshopserver.purchase.PurchaseItem.PurchaseItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @OneToMany
  private List<PurchaseItem> purchaseItems;
  private String totalPrice;
  private String date;
}
