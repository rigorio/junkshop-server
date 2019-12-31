package io.rigor.junkshopserver.junk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  public JunkController(JunkService junkService,
                        MaterialService materialService) {
    this.junkService = junkService;
    this.materialService = materialService;
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
      ObjectMapper mapper = new ObjectMapper();
      String s = mapper.writeValueAsString(body);
      List<Junk> junks = mapper.readValue(s, new TypeReference<List<Junk>>() {});
      return new ResponseEntity<>(junkService.saveAll(junks), HttpStatus.CREATED);
    }
    Junk junk = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(body), new TypeReference<Junk>() {});
    String materialName = junk.getMaterial();
    Optional<Material> byName = materialService.findByName(materialName);
    if (byName.isPresent()) {
      String weight = junk.getWeight();
      materialService.addWeight(byName.get(), weight);
    }
    return new ResponseEntity<>(junkService.save(junk), HttpStatus.CREATED);
  }

  @GetMapping("calibrate")
  public ResponseEntity<?> calibrate() {
    List<Junk> junks = junkService.findAll();
    List<Material> materials = materialService.findAll();
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
