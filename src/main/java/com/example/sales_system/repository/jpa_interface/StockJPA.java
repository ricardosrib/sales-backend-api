package com.example.sales_system.repository.jpa_interface;

import com.example.sales_system.repository.jpa_entities.StockItemEntity;
import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;

public interface StockJPA extends ListCrudRepository<StockItemEntity, Long> {
  Optional<StockItemEntity> findByProductId(long id);
}
