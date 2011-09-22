package me.tedyoung.blog;

import java.util.List;

public interface EntityService<T> {

	public abstract List<T> find();

	public abstract T load(long id);

	public abstract void save(T article);

	public abstract void remove(T article);

}