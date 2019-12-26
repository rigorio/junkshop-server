package io.rigor.junkshopserver.material;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {
  private MaterialService materialService;

  public MaterialController(MaterialService materialService) {
    this.materialService = materialService;
  }

  @GetMapping
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(materialService.findAll(), HttpStatus.OK);
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
    return new ResponseEntity<>(materialService.save(junk), HttpStatus.CREATED);
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
