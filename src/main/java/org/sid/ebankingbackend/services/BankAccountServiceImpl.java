package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.Exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.Exceptions.BankAccountNotFounfException;
import org.sid.ebankingbackend.Exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.mappers.BankAcountMapperImpl;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements  BankAccountService{
private BankAcountMapperImpl mapper;
private CustomerRepository customerRepository;
private BankAccountRepository bankAccountRepository;
private AccountOperationRepository accountOperationRepository;
//----------------////////  saveCustomer/////////////////////---------------------------
@Override
public CustomerDto saveCustomer(CustomerDto customerDto) {
	log.info("saving customer");
	Customer customer=mapper.fromCustomerDto(customerDto);
	customerRepository.save(customer);
	return mapper.fromCustomer(customer);
}
//----------------//////// saveCurrentBankAccount/////////////////////---------------------------
@Override
public CurrentAccountDto saveCurrentBankAccount(double initialBalance, double overDraft, Long customer_id) throws CustomerNotFoundException {
	Customer customer =customerRepository.findById(customer_id).orElse(null);
	if(customer==null) throw new CustomerNotFoundException("Customer not found");
	CurrentAccount currentAccount=new CurrentAccount();
	currentAccount.setId(UUID.randomUUID().toString());
	currentAccount.setCreatedAt(new Date());
	currentAccount.setBalance(initialBalance);
	currentAccount.setCustomer(customer);
	currentAccount.setOverDraft(overDraft);
	CurrentAccount savedBankAccount=bankAccountRepository.save(currentAccount);
	return mapper.fromCurrent(savedBankAccount) ;
}
//----------------////////  saveSavingBankAccount/////////////////////---------------------------
@Override
public SavingAccountDto saveSavingBankAccount(double initialBalance, double interestRate, Long customer_id) throws CustomerNotFoundException {
	Customer customer=customerRepository.findById(customer_id).orElse(null);
	if(customer==null) throw new CustomerNotFoundException("Customer not found");
	SavingAccount savingAccount=new  SavingAccount();
	savingAccount.setId(UUID.randomUUID().toString());
	savingAccount.setCreatedAt(new Date());
	savingAccount.setBalance(initialBalance);
	savingAccount.setCustomer(customer);
	savingAccount.setInterestRate(interestRate);
	SavingAccount savedBankAccount=bankAccountRepository.save(savingAccount);
	return mapper.fromSaving(savedBankAccount);
}
//----------------//////// CustomerDto/////////////////////---------------------------
@Override
public List<CustomerDto> ListCustomer() {
	
	List<Customer> customers=customerRepository.findAll();
	List<CustomerDto> customerDtos=customers.stream().map(customer -> mapper.fromCustomer(customer)).collect(Collectors.toList());
	/*List<CustomerDto> customerDtos=new ArrayList<>();
	for(Customer customer:customers){
		CustomerDto  customerDto=mapper.fromCustomer(customer);
		customerDtos.add(customerDto);
	}*/
	return customerDtos;
	
}
//----------------//////// getCustomerById/////////////////////---------------------------
@Override
public CustomerDto getCustomerFormId(Long id_customer) throws CustomerNotFoundException {
	Customer customer=customerRepository.findById(id_customer).orElseThrow(()-> new CustomerNotFoundException("Customer not found"));
	return mapper.fromCustomer(customer);

	
}
//----------------//////// getbankAccount/////////////////////---------------------------
@Override
public BankAccountDto getBankAccount(String accountId) throws BankAccountNotFounfException {
	BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFounfException("BankAccount not found"));
	if(bankAccount instanceof  SavingAccount){
		SavingAccount savingAccount=(SavingAccount) bankAccount;
		return  mapper.fromSaving(savingAccount);
	}
	else {
		CurrentAccount currentAccount=(CurrentAccount) bankAccount;
		return  mapper.fromCurrent(currentAccount);
	}
	
}
//----------------//////// debit/////////////////////---------------------------
@Override
public void debit(String accountId, double amount, String description) throws BankAccountNotFounfException, BalanceNotSufficientException {
	BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFounfException("BankAccount not found"));
if(bankAccount.getBalance()<amount)
	throw  new BalanceNotSufficientException("Balance not sufficient");
	AccountOperation accountOperation=new AccountOperation();
	accountOperation.setType(OperationType.DEBIT);
	accountOperation.setAmount(amount);
	accountOperation.setDescription(description);
	accountOperation.setOperationDate(new Date());
	accountOperation.setBankAccount(bankAccount);
	accountOperationRepository.save(accountOperation);
	bankAccount.setBalance(bankAccount.getBalance()-amount);
	bankAccountRepository.save(bankAccount);
}
//----------------//////// credit/////////////////////---------------------------
@Override
public void credit(String accountId, double amount, String description) throws BankAccountNotFounfException {
	BankAccount bankAccount=bankAccountRepository.findById(accountId).orElseThrow(()->new BankAccountNotFounfException("BankAccount not found"));
	AccountOperation accountOperation=new AccountOperation();
	accountOperation.setBankAccount(bankAccount);
	accountOperation.setOperationDate(new Date());
	accountOperation.setType(OperationType.CREDIT);
	accountOperation.setAmount(amount);
	accountOperation.setDescription(description);
	accountOperationRepository.save(accountOperation);
	bankAccount.setBalance(bankAccount.getBalance()+amount);
	bankAccountRepository.save(bankAccount);
}
//----------------//////// transfer/////////////////////---------------------------
@Override
public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFounfException, BalanceNotSufficientException {
debit(accountIdSource,amount,"Transfer to"+accountIdDestination);
credit(accountIdDestination,amount,"transfer from"+accountIdSource);
}
//----------------//////// consult/////////////////////---------------------------
@Override
public List<BankAccountDto> bankAccountList(){
	List<BankAccount> bankAcounts=bankAccountRepository.findAll();
	List<BankAccountDto> bankAcountDto=bankAcounts.stream().map(account->{
		if(account instanceof  SavingAccount){
			SavingAccount savingAccount=(SavingAccount) account;
		return mapper.fromSaving(savingAccount);
		}
		else{
			CurrentAccount currentAccount=(CurrentAccount) account;
			return mapper.fromCurrent(currentAccount);
		}
	}).collect(Collectors.toList());
	return  bankAcountDto;
}
//----------------//////// update/////////////////////---------------------------
@Override
public CustomerDto 	updateCustomer(CustomerDto customerDto) {
	log.info("saving customer");
	Customer customer=mapper.fromCustomerDto(customerDto);
	customerRepository.save(customer);
	return mapper.fromCustomer(customer);
}
@Override
public  void deleteCustomer(Long customer_id){
	customerRepository.deleteById(customer_id);
}
@Override
public  List<AccountOperationDto>  accountHistory(String id) {
	List<AccountOperation>  accountOperation=accountOperationRepository.findByBankAccountId(id);
	return accountOperation.stream().map(op-> mapper.fromAccountOperation(op)).collect(Collectors.toList());
	
}
@Override
public AccountHistoryDto getAccountHistory(String id, int page, int size) throws BankAccountNotFounfException {
	BankAccount bankAccount=bankAccountRepository.findById(id).orElse(null);
	if(bankAccount==null) throw new BankAccountNotFounfException("Account not Found");
	Page<AccountOperation> accountOperations=accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(id, PageRequest.of(page,size));
	List<AccountOperationDto> accountOperationDtos=accountOperations.getContent().stream().map(account-> mapper.fromAccountOperation(account)).collect(Collectors.toList());
	AccountHistoryDto accountHistoryDto=new AccountHistoryDto();
	accountHistoryDto.setAccountOperationDto(accountOperationDtos);
	accountHistoryDto.setAccountId(bankAccount.getId());
	accountHistoryDto.setBalance(bankAccount.getBalance());
	accountHistoryDto.setCurrentPage(page);
	accountHistoryDto.setPageSize(size);
	accountHistoryDto.setTotalPage(accountOperations.getTotalPages());
	return accountHistoryDto;
}

@Override
public List<CustomerDto> searchCustomer(String keyword) {
	List<Customer> customers=customerRepository.searchCustomer(keyword);
	List<CustomerDto> collect=customers.stream().map(cus->
		mapper.fromCustomer(cus)).collect(Collectors.toList());
	return collect;
	
}
}
