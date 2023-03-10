package com.github.chuettenrauch.mixifyapi.mixtape.controller;

import com.github.chuettenrauch.mixifyapi.mixtape.model.Mixtape;
import com.github.chuettenrauch.mixifyapi.mixtape.service.MixtapeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mixtapes")
@RequiredArgsConstructor
public class MixtapeController {

    private final MixtapeService mixtapeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mixtape create(@Valid @RequestBody Mixtape mixtape) {
        return this.mixtapeService.save(mixtape);
    }

    @GetMapping
    public List<Mixtape> getAll() {
        return this.mixtapeService.findAllForAuthenticatedUser();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@mixtapePermissionService.canView(#id)")
    public Mixtape get(@PathVariable String id) {
        return this.mixtapeService.findByIdForAuthenticatedUser(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@mixtapePermissionService.canEdit(#id)")
    public Mixtape update(@PathVariable String id, @Valid @RequestBody Mixtape mixtape) {
        return this.mixtapeService.updateByIdForAuthenticatedUser(id, mixtape);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@mixtapePermissionService.canDelete(#id)")
    public void delete(@PathVariable String id) {
        this.mixtapeService.deleteByIdForAuthenticatedUser(id);
    }

}
