package io.rigor.junkshopserver.junk;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/junk")
public class JunkController {
  private JunkService junkService;

  public JunkController(AmazonDynamoDB amazonDynamoDB, JunkService junkService) {
    this.junkService = junkService;
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = dynamoDBMapper
        .generateCreateTableRequest(Junk.class);

    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(4000L, 4000L));

    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);

  }

  @GetMapping()
  public ResponseEntity<?> getAllNoFilter(@RequestParam(required = false) String date) {
    if (date != null)
      return new ResponseEntity<>(junkService.findByDate(date), HttpStatus.OK);
    return new ResponseEntity<>(junkService.findAll(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getAll(@PathVariable(required = false) String id) {
    if (id != null)
      return new ResponseEntity<>(junkService.findById(id), HttpStatus.OK);
    return new ResponseEntity<>(junkService.findAll(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Object body) throws JsonProcessingException {
    if (body instanceof List) {
      ObjectMapper  mapper = new ObjectMapper();
      String s = mapper.writeValueAsString(body);
      System.out.println("eh");
      System.out.println(s);
      List<Junk> junks = mapper.readValue(s, new TypeReference<List<Junk>>() {});
      return new ResponseEntity<>(junkService.saveAll(junks), HttpStatus.CREATED);
    }
    Junk junk = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(body), new TypeReference<Junk>() {});
    return new ResponseEntity<>(junkService.save(junk), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    junkService.deleteById(id);
    return new ResponseEntity<>(junkService.findAll(), HttpStatus.OK);
  }


}
