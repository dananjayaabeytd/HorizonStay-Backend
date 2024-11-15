package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.HotelDTO;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.helper.FileUploadUtil;
import com.hotel.horizonstay.helper.Validation;
import com.hotel.horizonstay.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping
public class HotelController {

    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    @Autowired
    private HotelService hotelService;

    private final Validation validation = new Validation();
    private final ErrorResponse error = new ErrorResponse();

    // Endpoint to get all hotels
    @GetMapping("admin/hotel/get-all")
    public ResponseEntity<HotelDTO> getAllHotels()
    {
        try
        {
            HotelDTO hotels = hotelService.getAllHotels();
            return ResponseEntity.ok(hotels);
        }
        catch (Exception e)
        {
            // Create error response in case of exception
            HotelDTO errorResponse = new HotelDTO();
            errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage("Error occurred while fetching hotels");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Endpoint to get a hotel by ID
    @GetMapping("admin/hotel/get/{hotelID}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long hotelID)
    {
        try
        {
            HotelDTO hotel = hotelService.getHotelById(hotelID);

            if (hotel != null)
            {
                return ResponseEntity.ok(hotel);
            }
            return error.createHotelErrorResponse("Hotel not found", HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return error.createHotelErrorResponse("Error occurred while fetching hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to add a new hotel
    @PostMapping(value = "admin/hotel/add",consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HotelDTO> addHotel(@RequestPart("hotel") HotelDTO hotelDTO,@RequestParam("files") MultipartFile[] files)
    {
        if (hotelDTO == null)
        {
            return error.createHotelErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
        }
        if (validation.isInvalidHotelData(hotelDTO))
        {
            return error.createHotelErrorResponse("Invalid hotel data", HttpStatus.BAD_REQUEST);
        }

        // Directory where the files will be stored
        String uploadDir = "/HotelImages/";

        try
        {
            // Save each file and get the filenames
            List<String> fileNames = Arrays.stream(files)
                    .map(file -> {
                        try {
                            // Get a clean file name
                            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                            // Save the file to the upload directory
                            FileUploadUtil.saveFile(uploadDir, fileName, file);
                            return fileName; // Return the filename after saving
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out null values if any file failed to save
                    .toList();

            hotelDTO.setHotelImages(fileNames);


            HotelDTO addedHotel = hotelService.addHotel(hotelDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedHotel);
        }
        catch (Exception e)
        {
            return error.createHotelErrorResponse("Error occurred while adding hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to update an existing hotel
    @PutMapping("admin/hotel/update/{hotelID}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable Long hotelID, @RequestBody HotelDTO hotelDTO)
    {
//        if (hotelDTO == null)
//        {
//            return error.createHotelErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
//        }
//        if (validation.isInvalidHotelData(hotelDTO))
//        {
//            return error.createHotelErrorResponse("Invalid hotel data", HttpStatus.BAD_REQUEST);
//        }
        try
        {
            HotelDTO updatedHotel = hotelService.updateHotel(hotelID, hotelDTO);
            if (updatedHotel != null)
            {
                return ResponseEntity.ok(updatedHotel);
            }
            return error.createHotelErrorResponse("Hotel not found for update", HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return error.createHotelErrorResponse("Error occurred while updating hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to delete a hotel by ID
    @DeleteMapping("admin/hotel/delete/{hotelID}")
    public ResponseEntity<HotelDTO> deleteHotel(@PathVariable Long hotelID)
    {
        try
        {
            hotelService.deleteHotel(hotelID);
            return ResponseEntity.noContent().build();
        } catch (Exception e)
        {
            return error.createHotelErrorResponse("Error occurred while deleting hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

        @PutMapping(value = "admin/hotel/update/v2/{hotelID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable Long hotelID, @RequestPart("hotel") HotelDTO hotelDTO, @RequestParam("files") MultipartFile[] files) {

//        if (hotelDTO == null)
//        {
//            return error.createHotelErrorResponse("Request body is null", HttpStatus.BAD_REQUEST);
//        }
//        if (validation.isInvalidHotelData(hotelDTO))
//        {
//            return error.createHotelErrorResponse("Invalid hotel data", HttpStatus.BAD_REQUEST);
//        }

        // Directory where the files will be stored
        String uploadDir = "/HotelImages/";

        logger.info("------------------------------------- Adding new hotel with name: {}", hotelDTO.getHotelName());

        try {
            // Save each file and get the filenames
            List<String> fileNames = Arrays.stream(files).map(file ->
                    {
                        try
                        {
                            if (file.getOriginalFilename() == null)
                            {
                                throw new RuntimeException("File upload error");
                            }

                            // Get a clean file name
                            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                            // Save the file to the upload directory
                            FileUploadUtil.saveFile(uploadDir, fileName, file);

                            return fileName;

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out null values if any file failed to save
                    .toList();

            hotelDTO.setHotelImages(fileNames);

            HotelDTO updatedHotel = hotelService.updateHotel(hotelID, hotelDTO);
            if (updatedHotel != null) {
                return ResponseEntity.ok(updatedHotel);
            }
            return error.createHotelErrorResponse("Hotel not found for update", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return error.createHotelErrorResponse("Error occurred while updating hotel", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}