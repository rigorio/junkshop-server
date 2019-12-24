package io.rigor.junkshopserver.junk;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JunkHandler implements JunkService {
  private JunkRepository junkRepository;

  public JunkHandler(JunkRepository junkRepository) {
    this.junkRepository = junkRepository;
  }

  @Override
  public List<Junk> findAll() {
    return collectAsList(junkRepository.findAll());
  }


  @Override
  public Optional<Junk> findById(String id) {
    return junkRepository.findById(id);
  }

  @Override
  public List<Junk> findByDate(String date) {
    return junkRepository.findAllByDate(date);
  }

  @Override
  public void deleteById(String id) {
    junkRepository.deleteById(id);
  }

  @Override
  public void delete(Junk junk) {
    junkRepository.delete(junk);
  }

  @Override
  public List<Junk> saveAll(List<Junk> junks) {
    return collectAsList(junkRepository.saveAll(junks));
  }

  @Override
  public Junk save(Junk junk) {
    return junkRepository.save(junk);
  }

  private List<Junk> collectAsList(Iterable<Junk> all) {
    return StreamSupport
        .stream(all.spliterator(), false)
        .collect(Collectors.toList());
  }
}
