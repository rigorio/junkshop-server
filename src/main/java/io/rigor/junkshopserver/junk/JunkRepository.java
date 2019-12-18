package io.rigor.junkshopserver.junk;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JunkRepository extends JpaRepository<Junk, Long> {
  List<Junk> findAllByDate(String date);
}
