package org.sid.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDto {
private String accountId;
private double balance;
private int currentPage;
private int totalPage;
private int pageSize;
private List<AccountOperationDto> accountOperationDto;
}
