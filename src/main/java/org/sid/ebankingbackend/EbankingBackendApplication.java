package org.sid.ebankingbackend;

import jdk.swing.interop.SwingInterOpUtils;
import org.sid.ebankingbackend.Exceptions.BalanceNotSufficientException;
import org.sid.ebankingbackend.Exceptions.BankAccountNotFounfException;
import org.sid.ebankingbackend.Exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.dtos.BankAccountDto;
import org.sid.ebankingbackend.dtos.CurrentAccountDto;
import org.sid.ebankingbackend.dtos.CustomerDto;
import org.sid.ebankingbackend.dtos.SavingAccountDto;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.AccountStatus;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbankingBackendApplication.class, args);
	}
	
	@Bean
			CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
		return  args -> {
			Stream.of("Mohamed","Sana","Mourad").forEach(name->{
				CustomerDto customerDto=new CustomerDto();
				customerDto.setName(name);
				customerDto.setEmail(name+"@gmail.com");
				bankAccountService.saveCustomer(customerDto);
			});
			bankAccountService.ListCustomer().forEach(customer->{
				try {
					bankAccountService.saveCurrentBankAccount(Math.random()*100,Math.random()*20,customer.getId());
					bankAccountService.saveSavingBankAccount(Math.random()*1000,Math.random()*30, customer.getId());
			
				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}
			});
			List<BankAccountDto> bankAcounts=bankAccountService.bankAccountList();
			for(BankAccountDto bankAccount:bankAcounts){
				for(int i=0;i<10;i++){
					String accountId;
					if(bankAccount instanceof SavingAccountDto){
						accountId=((SavingAccountDto) bankAccount).getId();
					}else{
						accountId=((CurrentAccountDto) bankAccount).getId();
					}
					bankAccountService.credit(accountId, Math.random()*20,"Credit");
					bankAccountService.debit(accountId, Math.random()*20,"Debit");
				}}
		};
	}
	
	//@Bean
	CommandLineRunner start(CustomerRepository customerRepository,
							BankAccountRepository bankAccountRepository,
							AccountOperationRepository accountOperationRepository
							){return args ->{
								Stream.of("Hassan","Yacine", "Aicha").forEach(name->{
									Customer customer=new Customer();
									customer.setName(name);
									customer.setEmail(name+"@gmail.com");
									customerRepository.save(customer);
								});
								customerRepository.findAll().forEach(cust->{
									CurrentAccount currentAccount=new CurrentAccount();
									currentAccount.setBalance(Math.random()*800);
									currentAccount.setCreatedAt(new Date());
									currentAccount.setCustomer(cust);
									currentAccount.setOverDraft(5000);
									currentAccount.setStatus(AccountStatus.CREATED);
									currentAccount.setId(UUID.randomUUID().toString());
									bankAccountRepository.save(currentAccount);
									
									SavingAccount savingAccount=new SavingAccount();
									savingAccount.setBalance(Math.random()*800);
									savingAccount.setId(UUID.randomUUID().toString());
									savingAccount.setCreatedAt(new Date());
									savingAccount.setCustomer(cust);
									savingAccount.setInterestRate(5.5);
									savingAccount.setStatus(AccountStatus.CREATED);
									bankAccountRepository.save(savingAccount);
								});
								bankAccountRepository.findAll().forEach(acc->{
									for(int i=0;i<10;i++){
										AccountOperation accountOperation=new AccountOperation();
										accountOperation.setAmount(Math.random()*100);
										accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
										accountOperation.setOperationDate(new Date());
										accountOperation.setBankAccount(acc);
										accountOperationRepository.save(accountOperation);
									}
								});
	};
	}

}
