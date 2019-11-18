package io.rigor.junkshopserver.material;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialHandler implements MaterialService {
  private MaterialRepository repository;

  public MaterialHandler(MaterialRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Material> findAll() {
    return repository.findAll();
  }

  @Override
  public List<Material> findAll(Sort sort) {
    return repository.findAll(sort);
  }

  @Override
  public Optional<Material> findById(Long id) {
    return findById(id);
  }

  @Override
  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  @Override
  public void delete(Material material) {
    repository.delete(material);
  }

  @Override
  public List<Material> saveAll(List<Material> materials) {
    return repository.saveAll(materials);
  }

  @Override
  public Material save(Material material) {
    return repository.save(material);
  }
}
