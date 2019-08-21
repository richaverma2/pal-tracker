package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final TimeEntryRepository repository;
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        repository = timeEntryRepository;
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry response = repository.create(timeEntry);
        return new ResponseEntity<TimeEntry>(response,HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry obj = repository.find(id);
        if(obj != null)
            return new ResponseEntity<TimeEntry>(obj,HttpStatus.OK);
        else
            return new ResponseEntity<TimeEntry>(obj,HttpStatus.NOT_FOUND);
    }
    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {

        List<TimeEntry> response = repository.list();

        return new ResponseEntity<List<TimeEntry>>(response,HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry expected) {
        TimeEntry response = repository.update(id, expected);
        if(response != null){
            return new ResponseEntity<TimeEntry>(response,HttpStatus.OK);

        }
        else
            return new ResponseEntity<TimeEntry>(response,HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        repository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
