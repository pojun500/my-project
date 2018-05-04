import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SignInComponent } from './sign-in.component';
import { signInRoutingModule } from './sign-in-routing.module';

@NgModule({
  imports: [
    CommonModule,
    signInRoutingModule
  ],
  declarations: [SignInComponent]
})
export class SignInModule { }
