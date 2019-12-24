package io.rigor.junkshopserver.material;

import java.util.List;
import java.util.Optional;

public interface MaterialService {
  List<Material> findAll();

  Optional<Material> findById(Long id);

  void deleteById(String id);

  void delete(Material material);

  List<Material> saveAll(List<Material> materials);

  Material save(Material material);
}
