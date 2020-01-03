package io.rigor.junkshopserver.junk.junklist;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/junk/list")
public class JunkListController {

  private JunkListService junkListService;

  public JunkListController(JunkListService junkListService) {
    this.junkListService = junkListService;
  }

  @GetMapping
  public ResponseEntity<?> getAll(@RequestParam(required = false) String date) {
    if (date!=null)
      return new ResponseEntity<>(junkListService.findByDate(date), HttpStatus.OK);
    return new ResponseEntity<>(junkListService.all(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> get(@PathVariable(required = false) String id) {
    if (id!= null)
      return new ResponseEntity<>(junkListService.findById(id) ,HttpStatus.OK);
    return new ResponseEntity<>(junkListService.all(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<?> save(@RequestBody JunkList junkList) {
    if (junkList.getDate() == null)
      junkList.setDate(LocalDate.now().toString());
    return new ResponseEntity<>(junkListService.save(junkList), HttpStatus.CREATED);
  }
}
