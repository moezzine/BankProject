package org.sid.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.Exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {

private BankAccountService bankAccountService;
@GetMapping("/customer")
public List<CustomerDto> listcustomers(){
	return bankAccountService.ListCustomer();
}

@GetMapping("/customer/search")
public List<CustomerDto> searchCustomer(@RequestParam(name="keyword", defaultValue = "") String keyword){
	return bankAccountService.searchCustomer("%"+keyword+"%");
}
@GetMapping("/customer/{id}")
public CustomerDto getCustomerFromId(@PathVariable(name = "id") Long id_customer) throws CustomerNotFoundException {
	return bankAccountService.getCustomerFormId(id_customer);
}
@PostMapping("/customer")
public CustomerDto saveCustomer(@RequestBody CustomerDto customerDto){
	return bankAccountService.saveCustomer(customerDto);
}
@PutMapping("/customer/{customer_id}")
public CustomerDto updateCustmer(@PathVariable Long customer_id,@RequestBody CustomerDto customerDto){
	customerDto.setId(customer_id);
	return bankAccountService.updateCustomer(customerDto);
}
@DeleteMapping("/customer/{customer_id}")
public void deleteCustomer(@PathVariable Long customer_id){
	bankAccountService.deleteCustomer(customer_id);
}
}
