package org.sid.ebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Data  @NoArgsConstructor  @AllArgsConstructor
@Entity
public class Customer {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String name;
private String email;
@OneToMany (mappedBy="customer")
private List<BankAccount> bankAccounts;

}
