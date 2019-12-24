package io.rigor.junkshopserver.material;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MaterialHandler implements MaterialService {
  private MaterialRepository repository;

  public MaterialHandler(MaterialRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<Material> findAll() {
    return collectAsList(repository.findAll());
  }

  @Override
  public Optional<Material> findById(Long id) {
    return findById(id);
  }

  @Override
  public void deleteById(String id) {
    repository.deleteById(id);
  }

  @Override
  public void delete(Material material) {
    repository.delete(material);
  }

  @Override
  public List<Material> saveAll(List<Material> materials) {
    return collectAsList(repository.saveAll(materials));
  }

  @Override
  public Material save(Material material) {
    return repository.save(material);
  }

  private <T>List<T> collectAsList(Iterable<T> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }
}
