package ma.enset.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDto {
    private String accountId;
    private double amount;
    private int totalePages;
    private  int pageSize;
    private int currentPage;
    private List<AccountOperationDTO> accountOperationDTOS;

}
