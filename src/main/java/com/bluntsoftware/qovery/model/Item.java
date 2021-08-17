package com.bluntsoftware.qovery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
	@Id
	String id;
	String name;
	String description;
}
