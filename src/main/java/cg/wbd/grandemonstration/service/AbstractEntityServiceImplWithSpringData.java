package cg.wbd.grandemonstration.service;

import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractEntityServiceImplWithSpringData<E, I extends Serializable> {
    protected abstract CrudRepository<E, I> repository();

    public List<E> findAll() {
        return streamAll().collect(Collectors.toList());
    }

    public Optional<E> findOne(I id) {
        return repository().findById(id);
    }

    public E save(E E) {
        return repository().save(E);
    }

    public List<E> save(List<E> Es) {
        Iterable<E> updatedEs = repository().saveAll(Es);
        return streamAll(updatedEs).collect(Collectors.toList());
    }

    public boolean exists(I id) {
        return repository().existsById(id);
    }

    public List<E> findAll(List<I> ids) {
        Iterable<E> Es = repository().findAllById(ids);
        return streamAll(Es).collect(Collectors.toList());
    }

    public long count() {
        return repository().count();
    }

    public void delete(I id) {
        repository().deleteById(id);
    }

    public void delete(E E) {
        repository().delete(E);
    }

    public void delete(List<E> Es) {
        repository().deleteAll(Es);
    }

    public void deleteAll() {
        repository().deleteAll();
    }

    protected Stream<E> streamAll() {
        return StreamSupport.stream(repository().findAll().spliterator(), false);
    }

    protected Stream<E> streamAll(Iterable<E> Es) {
        return StreamSupport.stream(Es.spliterator(), false);
    }
}
