package io.rigor.junkshopserver.junk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Junk {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String material;
  private String price;
  private String weight;
}
