package io.rigor.junkshopserver.customProperties;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
public class CustomPropertyController {
  private CustomPropertyService customPropertyService;

  public CustomPropertyController(CustomPropertyService customPropertyService) {
    this.customPropertyService = customPropertyService;
  }

  @GetMapping
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(customPropertyService.findAll(), HttpStatus.OK);
  }

  @GetMapping("/{property}")
  public ResponseEntity<?> getValue(@PathVariable String property) {
    Optional<CustomProperty> byProperty = customPropertyService.findByProperty(property);
    return new ResponseEntity<>(byProperty.orElse(new CustomProperty()), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody CustomProperty customProperty) {
    Optional<CustomProperty> byProperty = customPropertyService.findByProperty(customProperty.getProperty());
    if (byProperty.isPresent()){
      customProperty = byProperty.get();
    }
    return new ResponseEntity<>(customPropertyService.save(customProperty), HttpStatus.CREATED);
  }

}
