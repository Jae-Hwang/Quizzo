import { Component, OnInit, OnDestroy } from '@angular/core';
import { AppUser } from 'src/app/models/app.user.model';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit, OnDestroy {

  currentUser: AppUser;
  userSubscription: Subscription;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    // every time the subject publishes new content 
    // it will invoke the subscriber method
    this.userSubscription = this.authService.$currentUser.subscribe(user => {
      this.currentUser = user;
      console.log(`Current User: ${user}`);
    });
    console.log(this.currentUser);
  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }

}
