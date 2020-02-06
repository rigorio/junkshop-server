package io.rigor.junkshopserver.junk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rigor.junkshopserver.cash.CashService;
import io.rigor.junkshopserver.material.Material;
import io.rigor.junkshopserver.material.MaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/junk")
public class JunkController {

  private JunkService junkService;
  private MaterialService materialService;
  private CashService cashService;

  public JunkController(JunkService junkService,
                        MaterialService materialService,
                        CashService cashService) {
    this.junkService = junkService;
    this.materialService = materialService;
    this.cashService = cashService;
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
  public ResponseEntity<?> save(@RequestBody Object body, @RequestParam String accountId) throws JsonProcessingException {
    if (body instanceof List) {
      ObjectMapper mapper = new ObjectMapper();
      String s = mapper.writeValueAsString(body);
      List<Junk> junks = mapper.readValue(s, new TypeReference<List<Junk>>() {});
      List<Junk> savedJunks = junkService.saveAll(junks);
//      savedJunks.forEach(cashService::addPurchases);
//      cashService.calibrateAll(accountId);
      savedJunks.forEach(junk -> cashService.calibrate(junk.getDate(), accountId));
      return new ResponseEntity<>(savedJunks, HttpStatus.CREATED);
    }
    Junk junk = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(body), new TypeReference<Junk>() {});
    String materialName = junk.getMaterial();
    Optional<Material> byName = materialService.findByName(materialName, accountId);
    if (byName.isPresent()) {
      String weight = junk.getWeight();
      materialService.addWeight(byName.get(), weight);
    }
    Junk savedJunk = junkService.save(junk);
//    cashService.addPurchases(savedJunk);
//    cashService.calibrateAll(accountId);
    cashService.calibrate(savedJunk.getDate(), accountId);
    return new ResponseEntity<>(savedJunk, HttpStatus.CREATED);
  }

  @GetMapping("calibrate")
  public ResponseEntity<?> calibrate(@RequestParam String accountId) {
    List<Junk> junks = junkService.findAll();
    List<Material> materials = materialService.findAll(accountId);
    materials.forEach(material -> {
      double totalWeight = junks.stream()
          .filter(junk -> junk.getMaterial().equals(material.getMaterial()))
          .mapToDouble(value -> Double.valueOf(value.getWeight()))
          .sum();
      material.setWeight("" + totalWeight);
    });
    return new ResponseEntity<>(materialService.saveAll(materials), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    junkService.deleteById(id);
    return new ResponseEntity<>(junkService.findAll(), HttpStatus.OK);
  }


}
