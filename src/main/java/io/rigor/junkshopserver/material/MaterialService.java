package io.rigor.junkshopserver.material;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface MaterialService {
  List<Material> findAll();

  List<Material> findAll(Sort sort);

  Optional<Material> findById(Long id);

  void deleteById(Long id);

  void delete(Material material);

  List<Material> saveAll(List<Material> materials);

  Material save(Material material);
}
