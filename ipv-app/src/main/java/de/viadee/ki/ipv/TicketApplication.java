package de.viadee.ki.ipv;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class TicketApplication {

  private static final Logger logger = LoggerFactory.getLogger(TicketApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(TicketApplication.class, args);
  }

}
