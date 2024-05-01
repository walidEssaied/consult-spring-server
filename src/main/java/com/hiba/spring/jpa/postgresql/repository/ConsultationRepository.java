package com.hiba.spring.jpa.postgresql.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hiba.spring.jpa.postgresql.model.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
  List<Consultation> findByPublished(boolean published);

  List<Consultation> findByTitleContaining(String title);
}
