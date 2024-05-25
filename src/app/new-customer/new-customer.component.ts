import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CustomerService} from "../services/customer.service";
import {Customer} from "../models/customer.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-customer',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit{

  newCustomerForm!:FormGroup
  constructor(private fb:FormBuilder,private customerService:CustomerService, private router:Router) {
  }
  ngOnInit() {
    this.newCustomerForm=this.fb.group({
      email: this.fb.control(null, [Validators.email, Validators.required]),
      name: this.fb.control(null, Validators.required)
    })
  }

  handleNewCustomer() {
  let customer:Customer=this.newCustomerForm.value
    this.customerService.saveCustomer(customer).subscribe({
      next: value => {
        alert("Customer has been successfully saved!")
        this.router.navigateByUrl("/customers")
      },
      error:err => {
        console.log(err)
      }
    });
  }
}
