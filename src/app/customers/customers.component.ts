import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AsyncPipe, JsonPipe, NgForOf, NgIf} from "@angular/common";
import {CustomerService} from "../services/customer.service";
import {Customer} from "../models/customer.model";
import {catchError, map, Observable, throwError} from "rxjs";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    JsonPipe,
    AsyncPipe,
    ReactiveFormsModule
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit{
  customers!:Observable<Array<Customer>>;
  errorMessage!:String;
  searchFormGroup!:FormGroup
  constructor(private customerService:CustomerService, public  fb:FormBuilder) {
  }
  ngOnInit() {
    this.searchFormGroup=this.fb.group({
      keyword:this.fb.control("")
    });

    this.handleSearchCustomers()
  }

  handleSearchCustomers() {
    let kw=this.searchFormGroup?.value.keyword
    this.customers=this.customerService.searchCustomers(kw).pipe(
      catchError (err => {
        this.errorMessage=err.message;
        return throwError(err);
      }));
  }

  handleDeleteCustomer(c:Customer) {
    let conf=confirm("Are you sure?")
    if (!conf) return
    this.customerService.deleteCustomer(c.id).subscribe({
      next: value =>{
        this.customers=this.customers.pipe(
           map( data=>{
             let index=data.indexOf(c)
             data.slice(index,1)
             return data
           })
          )
      },
      error: err => {
        console.log(err)
      }
    })
  }
}
