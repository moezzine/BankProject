package org.sid.ebankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.enums.AccountStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
@Data @NoArgsConstructor
@Entity
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4,discriminatorType = DiscriminatorType.STRING)
public  class BankAccount {
@Id
private  String id;
private  double balance;
private Date createdAt;
@Enumerated(EnumType.STRING)
private AccountStatus status;
@ManyToOne
private Customer customer;
@OneToMany(mappedBy = "bankAccount",fetch=FetchType.EAGER)
private List<AccountOperation> accountOperations;
}
