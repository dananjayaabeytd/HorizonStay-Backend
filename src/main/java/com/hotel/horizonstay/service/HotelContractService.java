//package com.hotel.horizonstay.service;
//
//import com.hotel.horizonstay.dto.*;
//import com.hotel.horizonstay.entity.*;
//import com.hotel.horizonstay.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeParseException;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class HotelContractService
//{
//
//    @Autowired
//    private HotelContractRepository contractRepository;
//
//    @Autowired
//    private HotelRepository hotelRepository;
//
//    @Autowired
//    private SeasonRepository seasonRepository;
//
//    @Autowired
//    private RoomTypeRepository roomTypeRepository;
//
//    @Autowired
//    private DiscountRepository discountRepository;
//
//    @Autowired
//    private MarkupRepository markupRepository;
//
//    @Autowired
//    private SupplementRepository supplementRepository;
//
//    // Convert Entity to DTO
//    private HotelContractDTO mapToDTO(HotelContract contract)
//    {
//        HotelContractDTO res = new HotelContractDTO();
//
//        try
//        {
//            // Map contract fields
//            res.setValidFrom(contract.getValidFrom());
//            res.setId(contract.getId());
//            res.setValidTo(contract.getValidTo());
//            res.setCancellationPolicy(contract.getCancellationPolicy());
//            res.setPaymentPolicy(contract.getPaymentPolicy());
//
//            // Map nested entities: Seasons, RoomTypes, Supplements, Discounts, Markups
//            List<SeasonDTO> seasonDTOList = contract.getSeasons().stream().map(this::mapSeasonToDTO).collect(Collectors.toList());
//            res.setSeasons(seasonDTOList);
//
//            // Success response
//            res.setStatusCode(200);
//            res.setMessage("Contract mapped to DTO successfully.");
//        } catch (Exception e)
//        {
//            res.setStatusCode(500);
//            res.setMessage("Error mapping contract to DTO: " + e.getMessage());
//        }
//
//        return res;
//    }
//
//    private SeasonDTO mapSeasonToDTO(Season season)
//    {
//        SeasonDTO res = new SeasonDTO();
//
//        try
//        {
//            res.setSeasonName(season.getSeasonName());
//            res.setValidFrom(season.getValidFrom());
//            res.setValidTo(season.getValidTo());
//
//            // Map RoomTypes, Supplements, Discounts, Markups
//            res.setRoomTypes(season.getRoomTypes().stream().map(this::mapRoomTypeToDTO).collect(Collectors.toList()));
//            res.setSupplements(season.getSupplements().stream().map(this::mapSupplementToDTO).collect(Collectors.toList()));
//            res.setDiscounts(season.getDiscounts().stream().map(this::mapDiscountToDTO).collect(Collectors.toList()));
//            res.setMarkups(season.getMarkups().stream().map(this::mapMarkupToDTO).collect(Collectors.toList()));
//
//            // Success response
//            res.setStatusCode(200);
//            res.setMessage("Season mapped to DTO successfully.");
//
//        } catch (Exception e)
//        {
//            res.setStatusCode(500);
//            res.setMessage("Error mapping season to DTO: " + e.getMessage());
//        }
//        return res;
//    }
//
//    private RoomTypeDTO mapRoomTypeToDTO(RoomType roomType)
//    {
//        RoomTypeDTO res = new RoomTypeDTO();
//
//        try
//        {
//            res.setRoomTypeName(roomType.getRoomTypeName());
//            res.setNumberOfRooms(roomType.getNumberOfRooms());
//            res.setMaxNumberOfPersons(roomType.getMaxNumberOfPersons());
//            res.setPrice(roomType.getPrice());
//            res.setRoomTypeImages(roomType.getRoomTypeImages());
//
//            // Success response
//            res.setStatusCode(200);
//            res.setMessage("Room type mapped to DTO successfully.");
//
//        } catch (Exception e)
//        {
//            res.setStatusCode(500);
//            res.setMessage("Error mapping room type to DTO: " + e.getMessage());
//        }
//        return res;
//    }
//
//    private SupplementDTO mapSupplementToDTO(Supplement supplement)
//    {
//        SupplementDTO res = new SupplementDTO();
//
//        try
//        {
//            res.setSupplementName(supplement.getSupplementName());
//            res.setPrice(supplement.getPrice());
//
//            // Success response
//            res.setStatusCode(200);
//            res.setMessage("Supplement mapped to DTO successfully.");
//
//        } catch (Exception e)
//        {
//            res.setStatusCode(500);
//            res.setMessage("Error mapping supplement to DTO: " + e.getMessage());
//        }
//        return res;
//    }
//
//    private DiscountDTO mapDiscountToDTO(Discount discount)
//    {
//        DiscountDTO res = new DiscountDTO();
//
//        try
//        {
//            res.setDiscountName(discount.getDiscountName());
//            res.setPercentage(discount.getPercentage());
//
//            // Success response
//            res.setStatusCode(200);
//            res.setMessage("Discount mapped to DTO successfully.");
//
//        } catch (Exception e)
//        {
//            res.setStatusCode(500);
//            res.setMessage("Error mapping discount to DTO: " + e.getMessage());
//        }
//        return res;
//    }
//
//    private MarkupDTO mapMarkupToDTO(Markup markup)
//    {
//        MarkupDTO res = new MarkupDTO();
//
//        try
//        {
//            res.setMarkupName(markup.getMarkupName());
//            res.setPercentage(markup.getPercentage());
//
//            // Success response
//            res.setStatusCode(200);
//            res.setMessage("Markup mapped to DTO successfully.");
//        } catch (Exception e)
//        {
//            res.setStatusCode(500);
//            res.setMessage("Error mapping markup to DTO: " + e.getMessage());
//        }
//        return res;
//    }
//
//    // Convert DTO to Entity
//    private HotelContract mapToEntity(HotelContractDTO dto)
//    {
//        HotelContract contract = new HotelContract();
//
//        try
//        {
//            contract.setValidFrom(dto.getValidFrom());
//            contract.setValidTo(dto.getValidTo());
//            contract.setCancellationPolicy(dto.getCancellationPolicy());
//            contract.setPaymentPolicy(dto.getPaymentPolicy());
//
//            // Success response (set on the DTO if needed)
//            dto.setStatusCode(200);
//            dto.setMessage("DTO mapped to entity successfully.");
//        } catch (Exception e)
//        {
//            dto.setStatusCode(500);
//            dto.setMessage("Error mapping DTO to contract entity: " + e.getMessage());
//        }
//        return contract;
//    }
//
//    private Season mapSeasonToEntity(SeasonDTO dto)
//    {
//        Season season = new Season();
//
//        try
//        {
//            season.setSeasonName(dto.getSeasonName());
//            season.setValidFrom(dto.getValidFrom());
//            season.setValidTo(dto.getValidTo());
//
//            // Success response
//            dto.setStatusCode(200);
//            dto.setMessage("Season DTO mapped to entity successfully.");
//        } catch (Exception e)
//        {
//            dto.setStatusCode(500);
//            dto.setMessage("Error mapping Season DTO to entity: " + e.getMessage());
//        }
//        return season;
//    }
//
//    private RoomType mapRoomTypeToEntity(RoomTypeDTO dto)
//    {
//        RoomType roomType = new RoomType();
//
//        try
//        {
//            roomType.setRoomTypeName(dto.getRoomTypeName());
//            roomType.setNumberOfRooms(dto.getNumberOfRooms());
//            roomType.setMaxNumberOfPersons(dto.getMaxNumberOfPersons());
//            roomType.setPrice(dto.getPrice());
//            roomType.setRoomTypeImages(dto.getRoomTypeImages());
//
//            // Success response
//            dto.setStatusCode(200);
//            dto.setMessage("RoomType DTO mapped to entity successfully.");
//        } catch (Exception e)
//        {
//            dto.setStatusCode(500);
//            dto.setMessage("Error mapping RoomType DTO to entity: " + e.getMessage());
//        }
//        return roomType;
//    }
//
//    private Supplement mapSupplementToEntity(SupplementDTO dto)
//    {
//        Supplement supplement = new Supplement();
//        try
//        {
//            supplement.setSupplementName(dto.getSupplementName());
//            supplement.setPrice(dto.getPrice());
//
//            // Success response
//            dto.setStatusCode(200);
//            dto.setMessage("Supplement DTO mapped to entity successfully.");
//
//        } catch (Exception e)
//        {
//            dto.setStatusCode(500);
//            dto.setMessage("Error mapping Supplement DTO to entity: " + e.getMessage());
//        }
//        return supplement;
//    }
//
//    private Discount mapDiscountToEntity(DiscountDTO dto)
//    {
//        Discount discount = new Discount();
//
//        try
//        {
//            discount.setDiscountName(dto.getDiscountName());
//            discount.setPercentage(dto.getPercentage());
//
//            // Success response
//            dto.setStatusCode(200);
//            dto.setMessage("Discount DTO mapped to entity successfully.");
//
//        } catch (Exception e)
//        {
//            dto.setStatusCode(500);
//            dto.setMessage("Error mapping Discount DTO to entity: " + e.getMessage());
//        }
//        return discount;
//    }
//
//    private Markup mapMarkupToEntity(MarkupDTO dto)
//    {
//        Markup markup = new Markup();
//
//        try
//        {
//            markup.setMarkupName(dto.getMarkupName());
//            markup.setPercentage((float) dto.getPercentage());
//
//            // Success response
//            dto.setStatusCode(200);
//            dto.setMessage("Markup DTO mapped to entity successfully.");
//        } catch (Exception e)
//        {
//            dto.setStatusCode(500);
//            dto.setMessage("Error mapping Markup DTO to entity: " + e.getMessage());
//        }
//        return markup;
//    }
//
//    // Add Hotel Contract with Exception Handling
//    @Transactional
//    public HotelContractDTO addHotelContract(Long hotelId, HotelContractDTO contractDTO)
//    {
//        try
//        {
//            LocalDate validFrom = contractDTO.getValidFrom();
//            LocalDate validTo = contractDTO.getValidTo();
//
//            // Retrieve the hotel
//            Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new IllegalArgumentException("Hotel not found"));
//
//            // Check if a contract already exists for this hotel and date range
//            HotelContract existingContract = contractRepository.findByHotelAndValidFromAndValidTo(hotel, validFrom, validTo);
//
//            if (existingContract != null)
//            {
//                throw new IllegalArgumentException("Contract already exists for this hotel within the specified date range.");
//            }
//
//            // Map DTO to entity and set foreign key relationship
//            HotelContract contract = mapToEntity(contractDTO);
//            contract.setHotel(hotel);
//            HotelContract savedContract = contractRepository.save(contract);
//
//            // Iterate over seasons and save them
//            for (SeasonDTO seasonDTO : contractDTO.getSeasons())
//            {
//                // Check if the season's dates are within the contract's valid range
//                validateSeasonDates(seasonDTO, validFrom, validTo);
//
//                Season season = mapSeasonToEntity(seasonDTO);
//                season.setContract(savedContract);  // Foreign key to HotelContract
//                Season savedSeason = seasonRepository.save(season);
//
//                // Save room types, supplements, discounts, markups for each season
//                saveSeasonEntities(seasonDTO, savedSeason);
//            }
//
//            contractDTO.setMessage("Contract added successfully.");
//            return contractDTO;
//
//        } catch (IllegalArgumentException e)
//        {
//            contractDTO.setMessage("Error: " + e.getMessage());
//            return contractDTO;  // Add status field if necessary and set error status
//        } catch (Exception e)
//        {
//            contractDTO.setMessage("An unexpected error occurred while adding the contract.");
//            return contractDTO;
//        }
//    }
//
//    // Helper function to handle season-related entities
//    private void saveSeasonEntities(SeasonDTO seasonDTO, Season savedSeason)
//    {
//        for (RoomTypeDTO roomTypeDTO : seasonDTO.getRoomTypes())
//        {
//            RoomType roomType = mapRoomTypeToEntity(roomTypeDTO);
//            roomType.setSeason(savedSeason);  // Foreign key to Season
//            roomTypeRepository.save(roomType);
//        }
//
//        for (SupplementDTO supplementDTO : seasonDTO.getSupplements())
//        {
//            Supplement supplement = mapSupplementToEntity(supplementDTO);
//            supplement.setSeason(savedSeason);  // Foreign key to Season
//            supplementRepository.save(supplement);
//        }
//
//        for (DiscountDTO discountDTO : seasonDTO.getDiscounts())
//        {
//            Discount discount = mapDiscountToEntity(discountDTO);
//            discount.setSeason(savedSeason);  // Foreign key to Season
//            discountRepository.save(discount);
//        }
//
//        for (MarkupDTO markupDTO : seasonDTO.getMarkups())
//        {
//            Markup markup = mapMarkupToEntity(markupDTO);
//            markup.setSeason(savedSeason);  // Foreign key to Season
//            markupRepository.save(markup);
//        }
//    }
//
//    // Get Contract by ID with Exception Handling
//    public HotelContractDTO getContractById(Long contractId)
//    {
//        try
//        {
//            HotelContract contract = contractRepository.findById(contractId).orElseThrow(() -> new IllegalArgumentException("Contract not found"));
//            return mapToDTO(contract);
//
//        } catch (IllegalArgumentException e)
//        {
//            // Return appropriate error message in DTO
//            HotelContractDTO contractDTO = new HotelContractDTO();
//            contractDTO.setMessage("Error: " + e.getMessage());
//            return contractDTO;
//
//        } catch (Exception e)
//        {
//            // Handle unexpected errors
//            HotelContractDTO contractDTO = new HotelContractDTO();
//            contractDTO.setMessage("An unexpected error occurred while retrieving the contract.");
//            return contractDTO;
//        }
//    }
//
//    // Delete Contract with Exception Handling
//    public void deleteContract(Long contractId)
//    {
//        try
//        {
//            if (!contractRepository.existsById(contractId))
//            {
//                throw new IllegalArgumentException("Contract not found.");
//            }
//            contractRepository.deleteById(contractId);
//            // You can also return a DTO with success message if needed
//
//        } catch (IllegalArgumentException e)
//        {
//            // Handle specific error
//            System.out.println("Error: " + e.getMessage());
//
//        } catch (Exception e)
//        {
//            // Handle unexpected error
//            System.out.println("An unexpected error occurred while deleting the contract.");
//        }
//    }
//
//    @Transactional
//    public HotelContractDTO updateContract(Long contractId, HotelContractDTO dto)
//    {
//        try
//        {
//            // Retrieve the existing contract
//            HotelContract existingContract = contractRepository.findById(contractId).orElseThrow(() -> new IllegalArgumentException("Contract not found"));
//
//            // Update contract details
//            LocalDate validFrom = dto.getValidFrom();
//            LocalDate validTo = dto.getValidTo();
//
//            existingContract.setValidFrom(validFrom);
//            existingContract.setValidTo(validTo);
//            existingContract.setCancellationPolicy(dto.getCancellationPolicy());
//            existingContract.setPaymentPolicy(dto.getPaymentPolicy());
//
//            // Retrieve the existing seasons and remove them (to replace with updated ones)
//            seasonRepository.deleteAll(existingContract.getSeasons());
//            existingContract.getSeasons().clear();
//
//            // Iterate over the updated seasons and validate
//            for (SeasonDTO seasonDTO : dto.getSeasons())
//            {
//                // Check if the season's dates are within the contract's valid range
//                validateSeasonDates(seasonDTO, validFrom, validTo);
//
//                // Map season DTO to entity and set foreign key to contract
//                Season season = mapSeasonToEntity(seasonDTO);
//                season.setContract(existingContract);  // Foreign key to HotelContract
//
//                // Save the updated season
//                Season savedSeason = seasonRepository.save(season);
//
//                // Update room types, supplements, discounts, markups for the season
//                saveSeasonEntities(seasonDTO, savedSeason);
//            }
//
//            // Save the updated contract with new season details
//            HotelContract updatedContract = contractRepository.save(existingContract);
//
//            dto.setMessage("Contract Updated Successfully.");
//            return dto;
//
//        } catch (IllegalArgumentException e)
//        {
//            dto.setMessage("Error: " + e.getMessage());
//            return dto;  // Add status field if necessary and set error status
//        } catch (Exception e)
//        {
//            dto.setMessage("An unexpected error occurred while updating the contract.");
//            return dto;
//        }
//    }
//    public List<SearchResultDTO> searchHotelContracts(String location, String checkInDateStr, String checkOutDateStr, int adults, int children) {
//        try {
//            LocalDate checkInDate = LocalDate.parse(checkInDateStr);
//            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);
//            int totalPersons = adults + children;
//
//            List<HotelContract> contracts = contractRepository.findContractsByLocationAndDateRange(location, checkInDate, checkOutDate);
//
//            // Select one contract per hotel
//            Map<Long, HotelContract> selectedContracts = new HashMap<>();
//            for (HotelContract contract : contracts) {
//                selectedContracts.putIfAbsent(contract.getHotel().getHotelID(), contract);
//            }
//
//            // Initialize a counter for unique numbers
//            final long[] counter = {1};
//
//            return selectedContracts.values().stream().map(contract -> {
//                SearchResultDTO dto = new SearchResultDTO();
//                Hotel hotel = contract.getHotel();
//                dto.setHotelName(hotel.getHotelName());
//                dto.setHotelID(hotel.getHotelID());
//                dto.setHotelLocation(hotel.getHotelCity() + ", " + hotel.getHotelCountry());
//                dto.setHotelDescription(hotel.getHotelDescription());
//                dto.setHotelContactNumber(hotel.getHotelContactNumber());
//                dto.setHotelEmail(hotel.getHotelEmail());
//                dto.setHotelRating(hotel.getHotelRating());
//                dto.setHotelImages(hotel.getHotelImages());
//
//                // Set cancellation policy and payment policy
//                dto.setCancellationPolicy(contract.getCancellationPolicy());
//                dto.setPaymentPolicy(contract.getPaymentPolicy());
//
//                // Assign a temporary unique number
//                dto.setNumber(counter[0]++);
//
//                // Find the season with the highest markup percentage
//                List<Season> matchingSeasons = seasonRepository.findActiveSeasonsByContract(contract, checkInDate, checkOutDate);
//                Season highestMarkupSeason = matchingSeasons.stream()
//                        .max(Comparator.comparing(Season::getHighestMarkupPercentage))
//                        .orElse(null);
//
//                if (highestMarkupSeason != null) {
//                    SeasonDTO seasonDTO = mapSeasonToDTO(highestMarkupSeason);
//                    List<RoomTypeDTO> filteredRoomTypes = seasonDTO.getRoomTypes().stream()
//                            .filter(roomType -> roomType.getMaxNumberOfPersons() >= totalPersons)
//                            .collect(Collectors.toList());
//                    seasonDTO.setRoomTypes(filteredRoomTypes);
//                    dto.setSeasons(Collections.singletonList(seasonDTO));
//                }
//
//                return dto;
//            }).collect(Collectors.toList());
//
//        } catch (DateTimeParseException e) {
//            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
//        } catch (Exception e) {
//            throw new RuntimeException("An unexpected error occurred while searching for contracts.");
//        }
//    }
//
//    // Calculate Payable Amount with Exception Handling
//    public Map<String, Float> calculatePayableAmount(CalculationDTO bookingRequest)
//    {
//        try
//        {
//            LocalDate checkInDate = LocalDate.parse(bookingRequest.getCheckIn());
//            LocalDate checkOutDate = LocalDate.parse(bookingRequest.getCheckOut());
//            long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
//
//            float totalAmount = 0.0f;
//
//            // Calculate room type cost
//            for (RoomTypeDTO roomTypeDTO : bookingRequest.getRoomType())
//            {
//                float roomPrice = roomTypeDTO.getPrice();
//                int numberOfRooms = roomTypeDTO.getNumberOfRooms();
//                int numberOfPassengers = bookingRequest.getNoOfAdults() + bookingRequest.getNoOfChildren();
//                float roomTotal = roomPrice * numberOfRooms * numberOfPassengers * numberOfNights;
//                totalAmount += roomTotal;
//            }
//
//            // Calculate supplement costs
//            for (SupplementDTO supplementDTO : bookingRequest.getSupplements())
//            {
//                float supplementCost = supplementDTO.getPrice() * supplementDTO.getQuantity();
//                totalAmount += supplementCost;
//            }
//
//            // Apply discount if available
//            float discountAmount = 0.0f;
//
//            if (bookingRequest.getDiscountPercentage() != null)
//            {
//                discountAmount = totalAmount * bookingRequest.getDiscountPercentage() / 100;
//                totalAmount -= discountAmount;
//            }
//
//            // Apply markup
//            float markupPercentage = bookingRequest.getMarkupPercentage();
//            totalAmount *= (1 + markupPercentage / 100);
//
//            // Return totalAmount and discountAmount as a map
//            Map<String, Float> result = new HashMap<>();
//            result.put("totalAmount", totalAmount);
//            result.put("discountAmount", discountAmount);
//
//            return result;
//
//        } catch (DateTimeParseException e)
//        {
//            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
//
//        } catch (Exception e)
//        {
//            throw new RuntimeException("An error occurred while calculating the payable amount.");
//        }
//    }
//
//    // Helper method to validate season dates
//    private void validateSeasonDates(SeasonDTO seasonDTO, LocalDate contractStartDate, LocalDate contractEndDate)
//    {
//        LocalDate seasonStartDate = seasonDTO.getValidFrom();
//        LocalDate seasonEndDate = seasonDTO.getValidTo();
//
//        // Check if season dates are within the contract's start and end dates
//        if (seasonStartDate.isBefore(contractStartDate) || seasonEndDate.isAfter(contractEndDate))
//        {
//            throw new IllegalArgumentException("Season dates (" + seasonStartDate + " to " + seasonEndDate + ") must lie within the contract dates (" + contractStartDate + " to " + contractEndDate + ").");
//        }
//    }
//
//    public List<HotelContractDTO> getContractsByHotelId(Long hotelId) {
//        List<HotelContract> contracts = contractRepository.findByHotel_HotelID(hotelId);
//        return contracts.stream().map(this::mapToDTO).collect(Collectors.toList());
//    }
//
//}
