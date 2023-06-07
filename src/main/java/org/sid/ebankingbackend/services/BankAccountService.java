package org.sid.ebankingbackend.services;

import org.sid.ebankingbackend.Exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.Exceptions.BankAccountNotFounfException;
import org.sid.ebankingbackend.Exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;

import java.util.List;

public interface BankAccountService {
	CustomerDto saveCustomer(CustomerDto customerDto);
CurrentAccountDto saveCurrentBankAccount(double initialBalance, double overDraft, Long customer_id) throws CustomerNotFoundException;
SavingAccountDto saveSavingBankAccount(double initialBalance, double interestRate, Long customer_id) throws CustomerNotFoundException;
List<CustomerDto> ListCustomer();

CustomerDto getCustomerFormId(Long id_customer) throws CustomerNotFoundException;

BankAccountDto getBankAccount(String accountId) throws BankAccountNotFounfException;
	void debit(String accountId,double amount,String description) throws BankAccountNotFounfException, BalanceNotSufficientException;
void credit(String accountId,double amount,String description) throws CustomerNotFoundException, BankAccountNotFounfException, BalanceNotSufficientException;
void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFounfException, BalanceNotSufficientException;

List<BankAccountDto> bankAccountList();

//----------------//////// update/////////////////////---------------------------
CustomerDto 	updateCustomer(CustomerDto customerDto);

void deleteCustomer(Long id);
List<AccountOperationDto>  accountHistory(String account_id) ;


AccountHistoryDto getAccountHistory(String id, int page, int size) throws BankAccountNotFounfException;

List<CustomerDto> searchCustomer(String keyword);
}
