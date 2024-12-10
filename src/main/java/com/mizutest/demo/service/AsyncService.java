package com.mizutest.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.mizutest.demo.entities.Uploads;
import com.mizutest.demo.repositories.UploadRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class AsyncService {
	
	
	@Autowired
	private UploadRepository uploadRepository;
	
	@Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    
    // Time calculation: assume 1 MB takes 5 seconds
    private static final double TIME_PER_MB = 5;
	
	
	@Async("taskExecutor")
    public void simulateConversion(Uploads video) {
		
		System.out.println("processing the video conversion");

		
	    System.out.println("Video size: " + video.getSize());
	    System.out.println("Update type: " + video.getUpdateType());
	    
        double processingTime = video.getSize() * TIME_PER_MB; // in seconds
        double interval = processingTime / 4; // 25% progress intervals
        
        
        System.out.println("Processing time: " + processingTime + " seconds");
        System.out.println("Interval per step: " + interval + " seconds");

        for (int i = 1; i <= 4; i++) {
        	
    		System.out.println("Started video conversion current progress:"+video.getProgress()+"%");

            try {
                Thread.sleep((long) (interval * 1000));
                video.setProgress(i * 25);
                

                if (video.getUpdateType().equalsIgnoreCase("progress_bar")) {
                	
                    messagingTemplate.convertAndSend("/topic/progress/" + video.getId(), video.getProgress());
                } else if (video.getUpdateType().equalsIgnoreCase("email")) {
                    
                    if (video.getProgress()!=100) {
                    	
                    	String progressEmail = getProgressEmail(video.getId(), video.getProgress());
                        sendEmail(video.getEmail(), "Conversion Progress", progressEmail,true);

                    }

            }

                uploadRepository.save(video);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        if (video.getProgress()==100.0) {
        	
    		System.out.println("video conversion completed:"+video.getProgress()+"%");
    		System.out.println("downlaodable link:"+"http://example.com/download/" + video.getId());
    		
    		System.out.print("Sending an email to the user with emailId:"+video.getEmail());


  
        	
            String completionEmail = getCompletionEmail(video.getId(),"http://example.com/download/" + video.getId());
            sendEmail(video.getEmail(), "Conversion Completed", completionEmail,true);

          }
        
        uploadRepository.save(video);

     }
	
	
	private void sendEmail(String to, String subject, String body, boolean isHtml) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, isHtml); // Set isHtml to true for HTML content
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
	}

    public String loadTemplate(String templateName) {
        try {
            var resource = resourceLoader.getResource("classpath:templates/" + templateName);
            try (var reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load email template: " + templateName, e);
        }
    }

    public String getProgressEmail(int i, double d) {
        String template = loadTemplate("progress_email.html");
        return template.replace("{{jobId}}", String.valueOf(i))
                       .replace("{{progress}}", String.valueOf(d));
    }

    public String getCompletionEmail(int i, String downloadLink) {
        String template = loadTemplate("completion_email.html");
        return template.replace("{{jobId}}", String.valueOf(i))
                       .replace("{{downloadLink}}", downloadLink);
    }

}
