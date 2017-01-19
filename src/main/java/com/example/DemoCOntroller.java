package com.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.model.Customer;

@RestController
@RequestMapping("/api")
public class DemoCOntroller {

	Map<Long, Customer> custmap = new HashMap<>();

	@PostConstruct
	public void initIt() throws Exception {
		Customer cust1 = new Customer(1, "Vasim");
		Customer cust2 = new Customer(2, "Sadia");
		custmap.put(cust1.getId(), cust1);
		custmap.put(cust2.getId(), cust2);
	}

	@GetMapping("/customers")
	public List<Customer> getAllCustomers() {
		List<Customer> custList = new ArrayList<>();
		Set<Long> custKey = custmap.keySet();
		custKey.forEach(cust -> {
			Customer customer = custmap.get(cust);
			custList.add(customer);
		});
		return custList;
	}

	@PostMapping("/customer")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws URISyntaxException {
		Random random = new Random();
		customer.setId(random.nextLong());
		custmap.put(customer.getId(), customer);
		return ResponseEntity.created(new URI("/api/customer/" + customer.getId()))
				.header("Customer " + customer.getId()).body(customer);
	}

	@PutMapping("/customer")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) throws URISyntaxException{
		if (customer.getId() == null) {
			return createCustomer(customer);
		}
		custmap.put(customer.getId(), customer);
		return ResponseEntity.ok()
				.header("Customer " + customer.getId()).body(customer);
	}
	
	@GetMapping("/customer/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		Customer customer = custmap.get(id);
		return Optional.ofNullable(customer).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}
