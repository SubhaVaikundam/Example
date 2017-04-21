package app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.model.Order;

public interface OrderRepository extends MongoRepository<Order, String>{

}
