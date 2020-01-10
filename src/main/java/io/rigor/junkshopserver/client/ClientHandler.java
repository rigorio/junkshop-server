package io.rigor.junkshopserver.client;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientHandler implements ClientService {
  private ClientRepository clientRepository;

  public ClientHandler(ClientRepository clientRepository,
                       AmazonDynamoDB amazonDynamoDB) {
    this.clientRepository = clientRepository;
    DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = mapper.generateCreateTableRequest(Client.class);
    tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @Override
  public List<Client> all() {
    return clientRepository.findAll();
  }

  @Override
  public Client save(Client client) {
    return clientRepository.save(client);
  }

  @Override
  public Optional<Client> findById(String id) {
    return clientRepository.findById(id);
  }
}
