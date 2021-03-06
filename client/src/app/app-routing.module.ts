import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AuthGuard } from './auth/auth.guard';
import { UserComponent } from './user/user.component';
import { RootComponent } from './root/root.component';
import { ShortenComponent } from './shorten/shorten.component';
import { EliteRouteComponent } from './elite-route/elite-route.component';
import { AdminComponent } from './admin/admin.component';
import { UserAdminComponent } from './admin/user-admin/user-admin.component';
import { CreateUserComponent } from './admin/create-user/create-user.component';
import { NetcodeComponent } from './netcode/netcode.component';
import { YtdlComponent } from './ytdl/ytdl.component';
import { YtdlListComponent } from './ytdl/ytdl-list/ytdl-list.component';
import { YtdlDetailsComponent } from './ytdl/ytdl-details/ytdl-details.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: '', component: RootComponent, canActivate: [AuthGuard], children: [
      { path: '', component: HomeComponent },
      { path: 'home', component: HomeComponent },
      { path: 'user', component: UserComponent },
      { path: 'shorten', component: ShortenComponent },
      { path: 'elite-route', component: EliteRouteComponent },
      {
        path: 'admin', component: AdminComponent, children: [
          { path: 'user/:id', component: UserAdminComponent },
          { path: 'createUser', component: CreateUserComponent }
        ]
      },
      { path: 'netcode', component: NetcodeComponent },
      {
        path: 'ytdl', component: YtdlComponent, children: [
          { path: 'list', component: YtdlListComponent },
          { path: 'job/:id', component: YtdlDetailsComponent }
        ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
