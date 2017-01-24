/*
 * 
 */
package com.example.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;

// TODO: Auto-generated Javadoc
/**
 * The Class DemoController.
 */
@RestController
@RequestMapping("/api")
public class DemoController {

	/**
	 * Instantiates a new demo controller.
	 */
	public DemoController() {
		// TODO Auto-generated constructor stub
	}

	/** The repository. */
	@Autowired
	private CustomerRepository repository;

	/**
	 * Gets the all customers.
	 *
	 * @return the all customers
	 */
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		List<Customer> custList = new ArrayList<>();
		custList = repository.findAll();
		return new ResponseEntity<List<Customer>>(custList, HttpStatus.OK);
	}

	/**
	 * Creates the customer.
	 *
	 * @param customer
	 *            the customer
	 * @return the response entity
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 */
	@PostMapping("/customer")
	public ResponseEntity<Customer> createCustomer(@RequestBody final Customer customer) throws URISyntaxException {
		final Customer cust = repository.save(customer);
		return ResponseEntity.created(new URI("/api/customer/" + cust.getId())).header("Customer " + cust.getId())
				.body(customer);
	}

	/**
	 * Update customer.
	 *
	 * @param customer
	 *            the customer
	 * @return the response entity
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 */
	@PutMapping("/customer")
	public ResponseEntity<Customer> updateCustomer(@RequestBody final Customer customer) throws URISyntaxException {
		repository.save(customer);
		return ResponseEntity.ok().body(customer);
	}

	/**
	 * Gets the customer by id.
	 *
	 * @param custId
	 *            the cust id
	 * @return the customer by id
	 */
	@GetMapping("/customer/{custId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable final String custId) {
		Customer customer = repository.findOne(custId);
		return Optional.ofNullable(customer).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Delete customer.
	 *
	 * @param custId
	 *            the cust id
	 */
	@DeleteMapping("/customer/{custId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable final String custId) {
		repository.delete(custId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
