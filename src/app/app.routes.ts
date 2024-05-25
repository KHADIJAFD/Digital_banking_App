import { Routes } from '@angular/router';
import {AccountsComponent} from "./accounts/accounts.component";
import path from "node:path";
import {CustomersComponent} from "./customers/customers.component";
import {NewCustomerComponent} from "./new-customer/new-customer.component";
import {LoginComponent} from "./login/login.component";
import {AdminTemplateComponent} from "./admin-template/admin-template.component";
import {AuthorizationGuard} from "./guard/authorization.guard";
import {authorization2Guard} from "./guards/authorization2.guard";

export const routes: Routes = [
  {path:"login",component:LoginComponent},
  {path:"",redirectTo:"/login",pathMatch:"full"},
  {path:"admin",component:AdminTemplateComponent ,canActivate:[AuthorizationGuard],children:[
      {path:"accounts",component:AccountsComponent},
      {path:"customers",component:CustomersComponent},
      {path:"new-customer",canActivate:[authorization2Guard],component:NewCustomerComponent}
    ]},

];
