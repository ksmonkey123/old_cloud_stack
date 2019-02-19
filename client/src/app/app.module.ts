import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AuthService } from './auth/auth.service';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { TokenInterceptor } from './auth/token.interceptor';
import { ContentTypeInterceptor } from './shared/contenttype.interceptor';
import { AuthGuard } from './auth/auth.guard';
import { UserService } from './user/user.service';
import { UserComponent } from './user/user.component';
import { RootComponent } from './root/root.component';
import { ShortenComponent } from './shorten/shorten.component';
import { ShortenService } from './shorten/shorten.service';
import { EliteRouteComponent } from './elite-route/elite-route.component';
import { EliteRouteService } from './elite-route/elite-route.service';
import { AdminComponent } from './admin/admin.component';
import { AdminService } from './admin/admin.service';
import { UserAdminComponent } from './admin/user-admin/user-admin.component';
import { CreateUserComponent } from './admin/create-user/create-user.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    UserComponent,
    RootComponent,
    ShortenComponent,
    EliteRouteComponent,
    AdminComponent,
    UserAdminComponent,
    CreateUserComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule.forRoot()
  ],
  providers: [
    AuthService,
    UserService,
    ShortenService,
    EliteRouteService,
    AdminService,
    AuthGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide:HTTP_INTERCEPTORS,
      useClass: ContentTypeInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
