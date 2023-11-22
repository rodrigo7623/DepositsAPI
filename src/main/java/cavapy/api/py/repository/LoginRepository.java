package cavapy.api.py.repository;

import cavapy.api.py.entity.Login;
import org.springframework.data.repository.CrudRepository;

public interface LoginRepository extends CrudRepository<Login, Integer> {
}
