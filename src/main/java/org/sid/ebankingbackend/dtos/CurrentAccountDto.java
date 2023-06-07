package org.sid.ebankingbackend.dtos;

import lombok.Data;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;
@Data
public class CurrentAccountDto extends BankAccountDto {

private  String id;
private CustomerDto customerDto;
private String type;
private Date createdAt;
private  double balance;
private  double overDraft;;
private AccountStatus status;

}

