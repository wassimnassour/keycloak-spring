package com.security.keycloak_app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/")
class TestController {
      
   @GetMapping("/get")
   @PreAuthorize("hasRole('ROLE_REALME_ROLES_SUPER_ADMIN1')")
   public ResponseEntity<String> getMethodName() {
       return new ResponseEntity<String>("This is Get param THIS_ISMS_DEPAREMENT_ROLE", HttpStatus.OK);
   }
        

   @GetMapping("/get2")
   @PreAuthorize("hasRole('ROLE_REALME_ROLES_SUPER_ADMIN2')")
   public ResponseEntity<String> getMethodName2() {
       return new ResponseEntity<String>("This is Get param THIS_ISMS_DEPAREMENT_ROLE", HttpStatus.OK);
   }
}