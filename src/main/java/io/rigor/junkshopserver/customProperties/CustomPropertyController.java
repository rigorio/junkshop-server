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

  @GetMapping("/{property}")
  public ResponseEntity<?> getValue(@PathVariable String property) {
    Optional<CustomProperty> byProperty = customPropertyService.findByProperty(property);
    CustomProperty noProperty = new CustomProperty("no property found", "no property found");
    return new ResponseEntity<>(byProperty.orElse(noProperty), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody CustomProperty customProperty) {
    return new ResponseEntity<>(customPropertyService.save(customProperty), HttpStatus.CREATED);
  }

}
