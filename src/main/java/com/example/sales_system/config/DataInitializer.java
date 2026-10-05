package com.example.sales_system.config;

import com.example.sales_system.repository.jpa_entities.CustomerEntity;
import com.example.sales_system.repository.jpa_entities.ProductEntity;
import com.example.sales_system.repository.jpa_entities.StockItemEntity;
import com.example.sales_system.repository.jpa_interface.CustomerJPA;
import com.example.sales_system.repository.jpa_interface.ProductJPA;
import com.example.sales_system.repository.jpa_interface.StockJPA;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  private final ProductJPA productJPA;
  private final StockJPA stockJPA;
  private final CustomerJPA customerJPA;

  public DataInitializer(ProductJPA productJPA, StockJPA stockJPA, CustomerJPA customerJPA) {
    this.productJPA = productJPA;
    this.stockJPA = stockJPA;
    this.customerJPA = customerJPA;
  }

  @Override
  public void run(String... args) throws Exception {
    // Seed products if none exist
    if (productJPA.count() == 0) {
      var p1 = productJPA.save(new ProductEntity(0L, "Laptop", 1200.0));
      var p2 = productJPA.save(new ProductEntity(0L, "Mouse", 25.0));

      // Seed stock items
      var s1 = new StockItemEntity(p1.getId(), p1, 10, 1, 100);
      var s2 = new StockItemEntity(p2.getId(), p2, 50, 5, 200);
      stockJPA.save(s1);
      stockJPA.save(s2);
    }

    // Seed a customer if none exist
    if (customerJPA.count() == 0) {
      var c1 = new CustomerEntity(1L, "Acme Corporation", "USA", "CA");
      customerJPA.save(c1);
    }
  }
}
