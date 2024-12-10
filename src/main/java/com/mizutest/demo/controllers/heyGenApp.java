package com.mizutest.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mizutest.demo.dto.Response;
import com.mizutest.demo.dto.uploadVideoDto;
import com.mizutest.demo.entities.Uploads;
import com.mizutest.demo.repositories.UploadRepository;
import com.mizutest.demo.service.UploadService;
import com.mizutest.demo.utilities.JsonValidator;
import com.networknt.schema.ValidationMessage;




@RestController
@RequestMapping("/api/demo")
public class heyGenApp {
	
	
	@Autowired
	private UploadService uploadService;
	
	
	@Autowired
	private UploadRepository uploadRepository;
	
	
    JsonValidator jsonValidator = new JsonValidator();  // Instantiate the validator

	
	@PostMapping("/uploadVideo")
	public ResponseEntity<Response> uploadVideo(@RequestBody uploadVideoDto uploadVideo){
		
		Response response= new Response();
		
		System.out.println("inside the controller");

		
		try {
            // Validate the incoming JSON request against the schema
            Set<ValidationMessage> validationErrors = jsonValidator.validateJson(uploadVideo);

            // If there are validation errors, return a BAD_REQUEST with the errors
            if (!validationErrors.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder();
                for (ValidationMessage validationError : validationErrors) {
                    errorMessage.append(validationError.getMessage()).append("; ");
                }
                response.setMessage("Validation error:make sure all the input fields are correctly entered");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            response.setMessage("Schema validation failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
		
		System.out.println("json validation succesful");

		
		Uploads savedCD=uploadService.uploadVideo(uploadVideo);

		
		if (savedCD != null) {

			response.setMessage("Video uploaded successfully with job id:"+savedCD.getId()+"<br> Total estimated time: " + (savedCD.getSize() * 5) + " seconds.<br>");
			response.setId(savedCD.getId());
			
            return new ResponseEntity<>(response,HttpStatus.OK);  // Return 200 OK if the CD is saved successfully
        } else {
        	
			response.setMessage("Unable to add the video");
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);  // Return 500 Internal Server Error if something goes wrong
        }
		
			
	}
	
	

	@GetMapping("/getStatus")
	public ResponseEntity<Map<String, String>> getStatus(@RequestParam("id") Long id) {
	    Map<String, String> response = new HashMap<>();
	    Uploads video = uploadRepository.findById(id);

	    if (video != null) {
	        if (video.getProgress() == 100) {
	            response.put("message", "Conversion complete. Download at: http://example.com/download/" + video.getId());
	        } else {
	            response.put("message", "Progress: " + video.getProgress() + "%");
	        }

	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        response.put("message", "Video with job id:" + id + " not found.");
	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
	}


}
