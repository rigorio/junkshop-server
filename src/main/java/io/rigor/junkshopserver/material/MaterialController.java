package io.rigor.junkshopserver.material;

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
  public ResponseEntity<?> saveBatch(@RequestBody List<Material> materials) {
    return new ResponseEntity<>(materialService.saveAll(materials), HttpStatus.CREATED);
  }
}
