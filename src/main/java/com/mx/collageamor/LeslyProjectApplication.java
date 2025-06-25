package com.mx.collageamor;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
//Forzar redeploy completo

@SpringBootApplication(scanBasePackages = "com.mx.collageamor")
//Trigger redeploy
public class LeslyProjectApplication {
 public static void main(String[] args) {
     SpringApplication.run(LeslyProjectApplication.class, args);
 }
}
