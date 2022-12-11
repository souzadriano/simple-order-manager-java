package com.souzadriano.som.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Mapper<OBJECT, ENTITY> {

	public abstract OBJECT toObject(ENTITY entity);

	public abstract ENTITY toEntity(OBJECT object);

	public List<OBJECT> toObject(Collection<ENTITY> entities) {
		return entities.stream().map(e -> toObject(e)).collect(Collectors.toList());
	}

	public List<ENTITY> toEntity(Collection<OBJECT> objects) {
		return objects.stream().map(e -> toEntity(e)).collect(Collectors.toList());
	}
}
