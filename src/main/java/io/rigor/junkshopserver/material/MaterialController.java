package io.rigor.junkshopserver.material;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rigor.junkshopserver.client.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
  private MaterialService materialService;
  private ClientService clientService;

  public MaterialController(MaterialService materialService,
                            ClientService clientService) {
    this.materialService = materialService;
    this.clientService = clientService;
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam String accountId) {
    return new ResponseEntity<>(materialService.findAll(accountId), HttpStatus.OK);
  }

  @GetMapping("/page")
  public ResponseEntity<?> getClientAndMaterials(@RequestParam String accountId) {
    Map<String, Object> map = new HashMap<>();
    map.put("clients", clientService.all(accountId));
    map.put("materials", materialService.findAll(accountId));
    return new ResponseEntity<>(map, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody Object body) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    if (body instanceof List) {
      String s = mapper.writeValueAsString(body);
      List<Material> materials = mapper.readValue(s, new TypeReference<List<Material>>() {});
      return new ResponseEntity<>(materialService.saveAll(materials), HttpStatus.CREATED);
    }
    Material junk = mapper.readValue(mapper.writeValueAsString(body), new TypeReference<Material>() {});
    Material save = materialService.save(junk);
    return new ResponseEntity<>(materialService.findAll(save.getAccountId()), HttpStatus.CREATED);
  }

  @DeleteMapping
  public ResponseEntity<?> delete(@RequestBody Material material) {
    materialService.delete(material);
    return new ResponseEntity<>(materialService.findAll(material.getAccountId()), HttpStatus.OK);
  }

  @PostMapping("/weight")
  public ResponseEntity<?> addWeight(@RequestBody Material material, @RequestParam String weight) {
    return new ResponseEntity<>(materialService.addWeight(material, weight), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<?> edit(@RequestBody Material material) {
    return new ResponseEntity<>(materialService.save(material), HttpStatus.CREATED);
  }


}
