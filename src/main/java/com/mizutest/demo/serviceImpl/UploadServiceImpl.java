package com.mizutest.demo.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.mizutest.demo.dto.uploadVideoDto;
import com.mizutest.demo.entities.Uploads;
import com.mizutest.demo.repositories.UploadRepository;
import com.mizutest.demo.service.AsyncService;
import com.mizutest.demo.service.UploadService;



@Service
public class UploadServiceImpl implements UploadService {
	
	
	@Autowired
	private UploadRepository uploadRepository;
	
	@Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private AsyncService asyncService;
    
    
    // Time calculation: assume 1 MB takes 5 seconds
    private static final double TIME_PER_MB = 5;

	@Override
	public Uploads uploadVideo(uploadVideoDto uploadDto) {
		

		
		Uploads  uploadEntity=new Uploads();
		
		uploadEntity.setName(uploadDto.getName());
		uploadEntity.setSize(uploadDto.getSize());
		uploadEntity.setUpdateType(uploadDto.getUpdateType());
		uploadEntity.setLanguage(uploadDto.getLanguage());
		uploadEntity.setProgress(0);
		/*uploadEntity.setDownloadUrl(null);*/
		
		System.out.println("adding video metadata to the database:"+uploadEntity.toString());

		
		if (uploadDto.getEmail()==null){ uploadEntity.setEmail(null);}
		else {uploadEntity.setEmail(uploadDto.getEmail());}
		
		Uploads returnEntity=uploadRepository.save(uploadEntity);
		
		System.out.println("video metadata added to the database successfully:"+uploadEntity.toString());

		asyncService.simulateConversion(returnEntity);
		
		return returnEntity;
		
	}
	
}
