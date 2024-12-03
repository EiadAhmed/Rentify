package com.io.rentify.cms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/static-pages")
public class StaticPageController {
    @Autowired
    private StaticPageService service;

    @PostMapping
    public ResponseEntity<StaticPage> createOrUpdatePage(@RequestBody StaticPage page) {
        return ResponseEntity.ok(service.saveOrUpdate(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaticPage> getPage(@PathVariable Long id) {
        return service.getPageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<StaticPage>> getAllPages() {
        return ResponseEntity.ok(service.getAllPages());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePage(@PathVariable Long id) {
        service.deletePage(id);
        return ResponseEntity.noContent().build();
    }
}
