import {Component, OnInit} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit{
  constructor(public authService:AuthService,private router:Router){}
  ngOnInit() {
  }

  handleLogout() {
    this.authService.logout();
    this.router.navigateByUrl("/login")
  }
}
