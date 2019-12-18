package io.rigor.junkshopserver.purchase.PurchaseItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseItem {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String material;
  private String type;
  private String price;
  private String weight;
}
