package com.mizutest.demo.service;

import java.util.List;

import com.mizutest.demo.dto.uploadVideoDto;
import com.mizutest.demo.entities.Uploads;



public interface UploadService {
	
	
	Uploads uploadVideo(uploadVideoDto uploadDto);

}
