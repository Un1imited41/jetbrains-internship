package com.github.un1imited41.internship.repositories;

import com.github.un1imited41.internship.entities.VariableWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariableRepository extends JpaRepository<VariableWrapper, Long> {
}
