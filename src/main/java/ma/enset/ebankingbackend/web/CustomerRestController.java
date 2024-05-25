package ma.enset.ebankingbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.ebankingbackend.dtos.CustomerDto;
import ma.enset.ebankingbackend.entities.Costumer;
import ma.enset.ebankingbackend.exceptions.CustomerNotFoundExcep;
import ma.enset.ebankingbackend.services.BankAccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    public BankAccountService bankAccountService;


    @GetMapping(path = "/customers")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public List<CustomerDto> customers(){
        return bankAccountService.COSTUMER_LIST();
    }
    @GetMapping(path = "/customers/search")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public List<CustomerDto> customers(@RequestParam(name ="keyword",defaultValue = "")String keyword){
        return bankAccountService.searchCustomers("%"+keyword+"%");
    }
    @GetMapping(path = "/customers/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_USER')")
    public CustomerDto getCustomer(@PathVariable Long id) throws CustomerNotFoundExcep {
        return bankAccountService.getCustomer(id);
    }
    @PostMapping(path = "/customers")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public CustomerDto saveCustomer(@RequestBody CustomerDto customerDto){
        return bankAccountService.saveCostumer(customerDto);
    }
    @PutMapping(path = "/customers/{customerId}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public CustomerDto updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDto customerDto){
        customerDto.setId(customerId);
        return bankAccountService.updateCostumer(customerDto);
    }
    @DeleteMapping(path = "/customers/{id}")
    @PreAuthorize("hasAnyAuthority('SCOPE_ADMIN')")
    public void deleteCustomer(@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }
}
