import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule, provideHttpClient} from "@angular/common/http";
import {HttpAppInterceptor} from "./interceptors/app-http.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideClientHydration(),HttpClientModule,provideHttpClient(),{provide:HTTP_INTERCEPTORS,useClass:HttpAppInterceptor,multi:true}]
};
