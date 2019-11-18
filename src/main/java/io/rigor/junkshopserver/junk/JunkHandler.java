package io.rigor.junkshopserver.junk;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JunkHandler implements JunkService {
  private JunkRepository junkRepository;

  public JunkHandler(JunkRepository junkRepository) {
    this.junkRepository = junkRepository;
  }

  @Override
  public List<Junk> findAll() {
    return junkRepository.findAll();
  }

  @Override
  public List<Junk> findAll(Sort sort) {
    return junkRepository.findAll(sort);
  }

  @Override
  public Optional<Junk> findById(Long id) {
    return junkRepository.findById(id);
  }

  @Override
  public void deleteById(Long id) {
    junkRepository.deleteById(id);
  }

  @Override
  public void delete(Junk junk) {
    junkRepository.delete(junk);
  }

  @Override
  public List<Junk> saveAll(List<Junk> junks) {
    return junkRepository.saveAll(junks);
  }

  @Override
  public Junk save(Junk junk) {
    return junkRepository.save(junk);
  }
}
