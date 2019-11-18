package io.rigor.junkshopserver.junk;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/junk")
public class JunkController {
  private JunkService junkService;

  public JunkController(JunkService junkService) {
    this.junkService = junkService;
  }

  @GetMapping
  public ResponseEntity<?> getAll() {
    return new ResponseEntity<>(junkService.findAll(), HttpStatus.OK);
  }
}
