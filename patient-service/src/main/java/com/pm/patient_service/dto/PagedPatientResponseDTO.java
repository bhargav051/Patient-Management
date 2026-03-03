package com.pm.patient_service.dto;

import java.util.List;

public class PagedPatientResponseDTO {
	private List<PatientResponseDTO> patientResponseDTO;
	private int page;
	private int size;
	private int totalPage;
	private int totalElements;

	public PagedPatientResponseDTO(List<PatientResponseDTO> patientResponseDTO, int page, int size, int totalPage,
			int totalElements) {
		super();
		this.patientResponseDTO = patientResponseDTO;
		this.page = page;
		this.size = size;
		this.totalPage = totalPage;
		this.totalElements = totalElements;
	}

	public List<PatientResponseDTO> getPatientResponseDTO() {
		return patientResponseDTO;
	}

	public void setPatientResponseDTO(List<PatientResponseDTO> patientResponseDTO) {
		this.patientResponseDTO = patientResponseDTO;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(int totalElements) {
		this.totalElements = totalElements;
	}

}
