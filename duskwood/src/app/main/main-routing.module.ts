import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './main.component';

const routes: Routes = [
  { path: '', component: MainComponent },
/*   { path: 'dashboard', loadChildren: 'app/main/main.module#MainModule' },
  { path: 'signin', loadChildren: 'app/sign-in/sign-in.module#SignInModule' }, */
];
 
@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class MainRoutingModule { }
