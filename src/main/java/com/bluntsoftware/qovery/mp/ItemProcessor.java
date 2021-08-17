package com.bluntsoftware.qovery.mp;

import com.bluntsoftware.qovery.model.Item;
import com.bluntsoftware.qovery.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;


@Slf4j
@Component
public class ItemProcessor {
  private final ObjectMapper mapper;
  private final ItemService service;

  public ItemProcessor(ItemService service) {
    this.service = service;
    this.mapper = new ObjectMapper();
  }

  @Bean
  public Function<Map<String,Object>, Item> processItem() {
    return map -> {
      log.info("processing: {}", map);
      return service.save(mapper.convertValue(map,Item.class));
    };
  }

}

