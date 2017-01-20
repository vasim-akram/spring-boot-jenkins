package com.example.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;

@RestController
@RequestMapping("/api")
public class DemoController {

    @Inject
	private CustomerRepository repository;
	
    @GetMapping("/customers")
	public List<Customer> getAllCustomers() {
		List<Customer> custList = repository.findAll();
		return custList;
	}

	@PostMapping("/customer")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws URISyntaxException {
		Customer cust = repository.save(customer);
		return ResponseEntity.created(new URI("/api/customer/" + cust.getId()))
				.header("Customer " + cust.getId()).body(customer);
	}

	@PutMapping("/customer")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) throws URISyntaxException{
		repository.save(customer);
		return ResponseEntity.ok().body(customer);
	}
	
	@GetMapping("/customer/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
		Customer customer = repository.findOne(id);
		return Optional.ofNullable(customer).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}
