package io.rigor.junkshopserver.material;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface MaterialRepository extends CrudRepository<Material, String> {
  Optional<Material> findAllByMaterial(String material);

  List<Material> findAllByAccountId(String accountId);

  Optional<Material> findByMaterialAndAccountId(String material, String accountId);
}
