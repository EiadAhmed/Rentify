package com.io.rentify.cms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaticPageService {
    @Autowired
    private StaticPageRepository repository;

    public StaticPage saveOrUpdate(StaticPage page) {
        return repository.save(page);
    }

    public Optional<StaticPage> getPageById(Long id) {
        return repository.findById(id);
    }

    public List<StaticPage> getAllPages() {
        return repository.findAll();
    }

    public void deletePage(Long id) {
        repository.deleteById(id);
    }
}
