package mk.ukim.finki.imdbclone.web.controllers;

import lombok.RequiredArgsConstructor;
import mk.ukim.finki.imdbclone.model.dto.CreatePersonDto;
import mk.ukim.finki.imdbclone.model.dto.DisplayPersonDto;
import mk.ukim.finki.imdbclone.service.application.PersonApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PersonController {

    private final PersonApplicationService personApplicationService;

    @GetMapping
    public ResponseEntity<List<DisplayPersonDto>> getAllPersons() {
        return ResponseEntity.ok(personApplicationService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisplayPersonDto> getPersonById(@PathVariable Long id) {
        Optional<DisplayPersonDto> person = personApplicationService.findById(id);
        return person.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<DisplayPersonDto>> searchPersonsByName(@RequestParam String name) {
        return ResponseEntity.ok(personApplicationService.search(name));
    }

    @PostMapping("/add")
    public ResponseEntity<DisplayPersonDto> createPerson(@RequestBody CreatePersonDto personDto) {
        return personApplicationService.save(personDto)
                .map(person -> ResponseEntity.status(HttpStatus.CREATED).body(person))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<DisplayPersonDto> updatePerson(
            @PathVariable Long id,
            @RequestBody CreatePersonDto personDto) {
        return personApplicationService.update(id, personDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}