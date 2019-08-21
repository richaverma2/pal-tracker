package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private final TimeEntryRepository repository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository repository,MeterRegistry meterRegistry) {
        this.repository = repository;

        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry response = repository.create(timeEntry);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());
        return new ResponseEntity<TimeEntry>(response,HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry obj = repository.find(id);
        if(obj != null) {
            actionCounter.increment();
            return new ResponseEntity<TimeEntry>(obj, HttpStatus.OK);
        }
        else
            return new ResponseEntity<TimeEntry>(obj,HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        List<TimeEntry> response = repository.list();
        return new ResponseEntity<List<TimeEntry>>(response,HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry expected) {
        TimeEntry response = repository.update(id, expected);
        if(response != null){
            actionCounter.increment();
            return new ResponseEntity<TimeEntry>(response,HttpStatus.OK);
        }

        else
            return new ResponseEntity<TimeEntry>(response,HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        repository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
