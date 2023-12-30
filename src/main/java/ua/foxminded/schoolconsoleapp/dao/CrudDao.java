package ua.foxminded.schoolconsoleapp.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<E> {

  void save(E entity);

  Optional<E> findById(Integer id);

  List<E> findAll();

  List<E> findAll(Integer page, Integer itemsPerPage);

  void update(E entity);

  boolean deleteById(Integer id);
}
