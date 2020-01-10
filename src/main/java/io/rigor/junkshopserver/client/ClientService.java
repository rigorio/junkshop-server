package io.rigor.junkshopserver.client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
  List<Client> all();

  Client save(Client client);

  Optional<Client> findById(String id);
}
