package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	private LocalDate getParsedDateOrDefault(String date, LocalDate defaultDate, boolean isStartDate) {
		if (date.isBlank()) {
			return isStartDate ? defaultDate.minusYears(1L) : defaultDate;
		}
		return LocalDate.parse(date);
	}

	public Page<SaleMinDTO> getReport(String minDate, String maxDate, String name, Pageable pageable) {
		LocalDate defaultData = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate initialDate = getParsedDateOrDefault(minDate, defaultData, true);
		LocalDate finalDate = getParsedDateOrDefault(maxDate, defaultData, false);

		Page<Sale> result = repository.searchSale(initialDate, finalDate, name, pageable);
		return result.map(SaleMinDTO::new);
	}

	public List<SummaryDTO> getSummary(String minDate, String maxDate) {
		LocalDate defaultData = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		LocalDate initialDate = getParsedDateOrDefault(minDate, defaultData, true);
		LocalDate finalDate = getParsedDateOrDefault(maxDate, defaultData, false);

		return repository.searchSale2(initialDate, finalDate);
	}
}
