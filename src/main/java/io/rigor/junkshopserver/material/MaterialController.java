package io.rigor.junkshopserver.material;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
  private MaterialService materialService;

  public MaterialController(MaterialService materialService, AmazonDynamoDB amazonDynamoDB) {
    this.materialService = materialService;
    DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    CreateTableRequest tableRequest = dynamoDBMapper
        .generateCreateTableRequest(Material.class);

    tableRequest.setProvisionedThroughput(
        new ProvisionedThroughput(4000L, 4000L));

    TableUtils.createTableIfNotExists(amazonDynamoDB, tableRequest);
  }

  @GetMapping
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(materialService.findAll(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> saveBatch(@RequestBody List<Material> materials) {
    return new ResponseEntity<>(materialService.saveAll(materials), HttpStatus.CREATED);
  }
}
