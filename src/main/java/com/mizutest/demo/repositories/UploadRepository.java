package com.mizutest.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mizutest.demo.entities.Uploads;



@Repository
public interface UploadRepository extends JpaRepository<Uploads, Integer> {

	Uploads findById(Long id);

}
