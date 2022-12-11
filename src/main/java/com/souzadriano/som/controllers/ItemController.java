package com.souzadriano.som.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.souzadriano.som.entities.Item;
import com.souzadriano.som.services.ItemService;

@RestController
public class ItemController {

	private ItemService itemService;

	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@GetMapping("/items")
	public List<Item> findAll() {
		return itemService.findAll();
	}

	@PostMapping("/items")
	public Item create(@Valid @RequestBody Item item) {
		return itemService.create(item);
	}

	@GetMapping("/items/{itemId}")
	public Item findOne(@PathVariable Long itemId) {
		return itemService.findOne(itemId);
	}

	@PutMapping("/items/{itemId}")
	public Item update(@PathVariable Long itemId, @Valid @RequestBody Item item) {
		return itemService.update(itemId, item);
	}

	@DeleteMapping("/items/{itemId}")
	public void delete(@PathVariable Long itemId) {
		itemService.delete(itemId);
	}

}
