package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.model.Customer;

/**
 * The Interface CustomerRepository.
 */
public interface CustomerRepository extends MongoRepository<Customer, String>{

}
