package org.sid.ebankingbackend.dtos;

import lombok.Data;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.enums.AccountStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.Date;
@Data
public class SavingAccountDto extends BankAccountDto{

private  String id;
private CustomerDto customerDto;
private String type;
private Date createdAt;
private  double balance;
private  double interestRate;
private AccountStatus status;

}
