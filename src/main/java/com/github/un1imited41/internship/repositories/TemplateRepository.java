package com.github.un1imited41.internship.repositories;

import com.github.un1imited41.internship.entities.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, String> {
}
