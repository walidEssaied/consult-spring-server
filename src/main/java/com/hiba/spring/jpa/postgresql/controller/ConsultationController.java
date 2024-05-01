package com.hiba.spring.jpa.postgresql.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.hiba.spring.jpa.postgresql.repository.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hiba.spring.jpa.postgresql.model.Consultation;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ConsultationController {

	@Autowired
	ConsultationRepository consultationRepository;

	@GetMapping("/consultations")
	public ResponseEntity<List<Consultation>> getAllConsultations(@RequestParam(required = false) String title) {
		try {
			List<Consultation> consultations = new ArrayList<Consultation>();

			if (title == null)
				consultationRepository.findAll().forEach(consultations::add);
			else
				consultationRepository.findByTitleContaining(title).forEach(consultations::add);

			if (consultations.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(consultations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/consultations/{id}")
	public ResponseEntity<Consultation> getConsultationById(@PathVariable("id") long id) {
		Optional<Consultation> consultationData = consultationRepository.findById(id);

		if (consultationData.isPresent()) {
			return new ResponseEntity<>(consultationData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/consultations")
	public ResponseEntity<Consultation> createConsultation(@RequestBody Consultation consultation) {
		try {
			Consultation _consultation = consultationRepository
					.save(new Consultation(consultation.getTitle(), consultation.getDescription(), false));
			return new ResponseEntity<>(_consultation, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/consultations/{id}")
	public ResponseEntity<Consultation> updateConsultation(@PathVariable("id") long id, @RequestBody Consultation consultation) {
		Optional<Consultation> consultationData = consultationRepository.findById(id);

		if (consultationData.isPresent()) {
			Consultation _consultationData = consultationData.get();
			_consultationData.setTitle(consultation.getTitle());
			_consultationData.setDescription(consultation.getDescription());
			_consultationData.setPublished(consultation.isPublished());
			return new ResponseEntity<>(consultationRepository.save(_consultationData), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/consultations/{id}")
	public ResponseEntity<HttpStatus> deleteConsultation(@PathVariable("id") long id) {
		try {
			consultationRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/consultations")
	public ResponseEntity<HttpStatus> deleteAllConsultations() {
		try {
			consultationRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/consultations/published")
	public ResponseEntity<List<Consultation>> findByPublished() {
		try {
			List<Consultation> consultations = consultationRepository.findByPublished(true);

			if (consultations.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(consultations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
