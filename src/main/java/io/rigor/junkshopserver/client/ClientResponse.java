package io.rigor.junkshopserver.client;

import io.rigor.junkshopserver.junk.junklist.JunkList;
import io.rigor.junkshopserver.sale.Sale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
  private Client client;
  private List<Sale> sales;
  private List<JunkList> purchases;
}
