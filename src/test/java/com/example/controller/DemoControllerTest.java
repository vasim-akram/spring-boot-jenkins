package com.example.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.test.util.TestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: Auto-generated Javadoc
/**
 * The Class DemoControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoControllerTest {

	/** The Constant DEFAULT_NAME. */
	private static final String DEFAULT_NAME = "TEST";

	/** The Constant UPDATED_NAME. */
	private static final String UPDATED_NAME = "test";

	/** The customer repository. */
	@Inject
	private CustomerRepository repository;

	/** The http message converter. */
	@Inject
	private MappingJackson2HttpMessageConverter httpMessageConverter;

	/** The mapper. */
	@Inject
	private ObjectMapper mapper;

	/** The rest customer mock mvc. */
	private MockMvc restCustomerMockMvc;

	/** The customer. */
	private Customer customer;

	/** The demo controller. */
	private DemoController demoController;

	/**
	 * Setup.
	 */
	@PostConstruct
	public void setup() {
		MockitoAnnotations.initMocks(this);
		demoController = new DemoController();
		restCustomerMockMvc = MockMvcBuilders.standaloneSetup(demoController).setMessageConverters(httpMessageConverter)
				.build();
		ReflectionTestUtils.setField(demoController, "repository", repository);

	}

	/**
	 * Inits the test.
	 */
	@Before
	public void initTest() {
		customer = new Customer();
		customer.setName(DEFAULT_NAME);
	}

	/**
	 * Creates the customer.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void createCustomer() throws Exception {
		int databaseSizeBeforeCreate = repository.findAll().size();

		// Create the BankAccount

		restCustomerMockMvc.perform(post("/api/customer").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(mapper.writeValueAsBytes(customer))).andExpect(status().isCreated());

		// Validate the BankAccount in the database
		List<Customer> customerList = repository.findAll();
		assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
		Customer testCustomer = customerList.get(customerList.size() - 1);
		assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
	}

	/**
	 * Gets the all customer.
	 *
	 * @return the all customer
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getAllCustomer() throws Exception {
		// Initialize the database
		repository.save(customer);

		// Get the customer
		restCustomerMockMvc.perform(get("/api/customers")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().toString())))
				.andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
		// .andDo(print());

	}

	/**
	 * Gets the customer.
	 *
	 * @return the customer
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getCustomer() throws Exception {
		// Initialize the database
		repository.save(customer);

		// Get the bankAccount
		restCustomerMockMvc.perform(get("/api/customer/{id}", customer.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id").value(customer.getId().toString()))
				.andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
		// .andDo(print());
	}

	/**
	 * Update customer.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void updateCustomer() throws Exception {
		// Initialize the database
		repository.save(customer);
		int databaseSizeBeforeUpdate = repository.findAll().size();

		// Update the bankAccount
		Customer updatedCustomer = repository.findOne(customer.getId());
		updatedCustomer.setName(UPDATED_NAME);

		restCustomerMockMvc.perform(put("/api/customer").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(mapper.writeValueAsBytes(updatedCustomer))).andExpect(status().isOk());

		// Validate the BankAccount in the database
		List<Customer> customerList = repository.findAll();
		assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
		Customer testCustomer = customerList.get(customerList.size() - 1);
		assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
	}

	/**
	 * Delete customer.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void deleteCustomer() throws Exception {
		// Initialize the database
		repository.save(customer);

		int databaseSizeBeforeDelete = repository.findAll().size();

		// Get the blog
		restCustomerMockMvc.perform(delete("/api/customer/{id}", customer.getId()).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());//.andDo(print());

		// Validate the database is empty
		List<Customer> customers = repository.findAll();
		assertThat(customers).hasSize(databaseSizeBeforeDelete - 1);
	}

}
