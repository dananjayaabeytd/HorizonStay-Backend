package com.hotel.horizonstay.controller;

import com.hotel.horizonstay.dto.RoomTypeDTO;
import com.hotel.horizonstay.helper.ErrorResponse;
import com.hotel.horizonstay.helper.FileUploadUtil;
import com.hotel.horizonstay.service.RoomTypeService;
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
@RequestMapping("/roomtype")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    private final ErrorResponse error = new ErrorResponse();

    @PostMapping(value = "/{seasonID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomTypeDTO> addRoomTypeToSeason(@PathVariable Long seasonID, @RequestParam("files") MultipartFile[] files, @RequestPart("roomtype") RoomTypeDTO roomTypeDTO)
    {
        // Directory where the files will be stored
        String uploadDir = "/roomTypeImages/";

        try
        {
            // Validate roomTypeDTO
            if (roomTypeDTO.getRoomTypeName() == null || roomTypeDTO.getRoomTypeName().isEmpty())
            {
                return error.createRoomTypeErrorResponse("Invalid room type data", HttpStatus.BAD_REQUEST);
            }

            // Save each file and get the filenames
            List<String> fileNames = Arrays.stream(files)
                    .map(file -> {
                        try
                        {
                            // Get a clean file name
                            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                            // Save the file to the upload directory
                            FileUploadUtil.saveFile(uploadDir, fileName, file);
                            return fileName;
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            return null;
                        }

                    })
                    .filter(Objects::nonNull) // Filter out null values if any file failed to save
                    .toList();

            System.out.println("Uploading files to: " + uploadDir);
            roomTypeDTO.setRoomTypeImages(fileNames);

            RoomTypeDTO createdRoomType = roomTypeService.addRoomTypeToSeason(seasonID, roomTypeDTO);

            return new ResponseEntity<>(createdRoomType, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e)
        {
            return error.createRoomTypeErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
        catch (Exception e)
        {
            return error.createRoomTypeErrorResponse("Error occurred while adding room type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{roomTypeID}")
    public ResponseEntity<RoomTypeDTO> updateRoomType(@PathVariable Long roomTypeID, @RequestBody RoomTypeDTO roomTypeDTO)
    {
        try
        {
            RoomTypeDTO updatedRoomType = roomTypeService.updateRoomType(roomTypeID, roomTypeDTO);
            return new ResponseEntity<>(updatedRoomType, HttpStatus.OK);

        }
        catch (IllegalArgumentException e)
        {
            return error.createRoomTypeErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return error.createRoomTypeErrorResponse("Error occurred while updating room type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{roomTypeID}")
    public ResponseEntity<RoomTypeDTO> getRoomTypeById(@PathVariable Long roomTypeID)
    {
        try
        {
            RoomTypeDTO roomTypeDTO = roomTypeService.getRoomTypeById(roomTypeID);

            if (roomTypeDTO == null)
            {
                return error.createRoomTypeErrorResponse("Room type not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(roomTypeDTO, HttpStatus.OK);

        }
        catch (IllegalArgumentException e)
        {
            return error.createRoomTypeErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);

        }
        catch (Exception e)
        {
            return error.createRoomTypeErrorResponse("Error occurred while fetching room type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{roomTypeID}")
    public ResponseEntity<RoomTypeDTO> deleteRoomType(@PathVariable Long roomTypeID)
    {
        try
        {
            RoomTypeDTO roomTypeDTO= roomTypeService.deleteRoomType(roomTypeID);
            return new ResponseEntity<>(roomTypeDTO,HttpStatus.OK);
        }
        catch (IllegalArgumentException e)
        {
            return error.createRoomTypeErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e)
        {
            return error.createRoomTypeErrorResponse("Error occurred while deleting room type", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/season/{seasonID}")
    public ResponseEntity<List<RoomTypeDTO>> getRoomTypesBySeasonId(@PathVariable Long seasonID)
    {
        try
        {
            List<RoomTypeDTO> roomTypes = roomTypeService.getRoomTypesBySeasonId(seasonID);
            return ResponseEntity.ok(roomTypes);
        }
        catch (Exception e)
        {
            return error.createRoomtErrorResponseList("Error occurred while fetching room types", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}