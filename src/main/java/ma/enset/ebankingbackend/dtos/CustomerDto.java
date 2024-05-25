package ma.enset.ebankingbackend.dtos;

import lombok.Data;

@Data
public class CustomerDto {
    private String name;
    private String email;
    private Long id;
}
