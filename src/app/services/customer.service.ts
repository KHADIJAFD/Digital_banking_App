import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Customer} from "../models/customer.model";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  host:string="http://localhost:8085"
  constructor(private http:HttpClient) { }

  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.host+"/customers")
  }
  public searchCustomers(keyword:string):Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.host+"/customers/search?keyword="+keyword)  }
  saveCustomer(customer: Customer):Observable<Customer> {
    return this.http.post<Customer>(this.host+"/customers",customer)
  }
  deleteCustomer(id:number) {
    return this.http.delete(this.host+"/customers/"+id)
  }
}
