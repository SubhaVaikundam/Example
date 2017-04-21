package app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String>{

}
