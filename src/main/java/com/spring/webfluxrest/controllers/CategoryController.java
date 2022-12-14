package com.spring.webfluxrest.controllers;

import com.spring.webfluxrest.domain.Category;
import com.spring.webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {
    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/api/v1/categories")
    Flux<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    @GetMapping("/api/v1/categories/{id}")
    Mono<Category> getCategoryById(@PathVariable String id){
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/categories")
    Mono<Void> create(@RequestBody Publisher<Category> categoryStream){
        return categoryRepository.saveAll(categoryStream).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/v1/categories/{id}")
    Mono<Category> update(@PathVariable String id,@RequestBody Category category){
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/api/v1/categories/{id}")
    Mono<Category> updateOne(@PathVariable String id, @RequestBody Category category){
        return categoryRepository.findById(id).flatMap(foundCategory -> {
            if(!foundCategory.getDescription().equalsIgnoreCase(category.getDescription())){
                foundCategory.setDescription(category.getDescription());
                return categoryRepository.save(foundCategory);
            }
            return Mono.just(foundCategory);
        });
    }
}
