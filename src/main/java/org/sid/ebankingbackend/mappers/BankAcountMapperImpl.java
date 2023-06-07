package org.sid.ebankingbackend.mappers;

import org.sid.ebankingbackend.dtos.AccountOperationDto;
import org.sid.ebankingbackend.dtos.CurrentAccountDto;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAcountMapperImpl {
public CustomerDto fromCustomer(Customer customer){
	CustomerDto customerDto=new CustomerDto();
	BeanUtils.copyProperties(customer,customerDto);
	return customerDto;
}
public Customer fromCustomerDto(CustomerDto customerDto){
	Customer customer=new Customer();
	BeanUtils.copyProperties(customerDto,customer);
	return customer;
}
public SavingAccountDto fromSaving(SavingAccount savingAccount){
	SavingAccountDto savingAccountDto=new SavingAccountDto();
	BeanUtils.copyProperties(savingAccount,savingAccountDto);
	savingAccountDto.setCustomerDto(fromCustomer(savingAccount.getCustomer()));
	savingAccountDto.setType(savingAccount.getClass().getSimpleName());
	return savingAccountDto;
}
public  SavingAccount fromSavingDto(SavingAccountDto savingAccountDto){
	SavingAccount savingAccount=new SavingAccount();
	BeanUtils.copyProperties(savingAccountDto,savingAccount);
	savingAccount.setCustomer(fromCustomerDto(savingAccountDto.getCustomerDto()));
	return savingAccount;
}
public CurrentAccountDto fromCurrent(CurrentAccount currentAccount){
	CurrentAccountDto currentAccountDto=new CurrentAccountDto();
	BeanUtils.copyProperties(currentAccount,currentAccountDto);
	currentAccountDto.setCustomerDto(fromCustomer(currentAccount.getCustomer()));
	currentAccountDto.setType( currentAccount.getClass().getSimpleName());
	return currentAccountDto;
}
public  CurrentAccount fromCurrentDto(CurrentAccountDto currentAccountDto){
	 CurrentAccount currentAccount=new CurrentAccount();
	BeanUtils.copyProperties(currentAccountDto,currentAccount);
	currentAccount.setCustomer(fromCustomerDto(currentAccountDto.getCustomerDto()));
	return currentAccount;
}
public AccountOperationDto fromAccountOperation(AccountOperation accountOperation){
	AccountOperationDto accountOperationDto=new AccountOperationDto();
	BeanUtils.copyProperties(accountOperation,accountOperationDto);
	return  accountOperationDto;
}
public  AccountOperation fromAccountOperationDto(AccountOperationDto accountOperationDto){
	AccountOperation accountOperation=new AccountOperation();
	BeanUtils.copyProperties(accountOperationDto,accountOperation);
	return  accountOperation;
}
}
