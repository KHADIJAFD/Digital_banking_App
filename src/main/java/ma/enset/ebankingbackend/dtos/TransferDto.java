package ma.enset.ebankingbackend.dtos;

import lombok.Data;

@Data
public class TransferDto {
    String accountIdSource;
    String accountIdDest;
    double amount;
}
