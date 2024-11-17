package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.*;
import com.hotel.horizonstay.entity.*;
import com.hotel.horizonstay.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContractService {

    private static final Logger logger = LoggerFactory.getLogger(ContractService.class);

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private MarkupRepository markupRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private SupplementRepository supplementRepository;

    @Autowired
    private RoomAvailabilityRepository roomAvailabilityRepository;

    public HotelContractDTO addHotelContract(Long hotelID, HotelContractDTO contractDTO)
    {
        logger.info("Adding hotel contract for hotel ID: {}", hotelID);
        HotelContractDTO response = new HotelContractDTO();

        try
        {
            HotelContract contract = new HotelContract();
            Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);

            if (hotelOptional.isPresent())
            {
                contract.setHotel(hotelOptional.get());
            }
            else
            {
                response.setStatusCode(404);
                response.setMessage("Hotel not found");
                logger.warn("Hotel with ID: {} not found", hotelID);
                return response;
            }

            contract.setAddedDate(LocalDateTime.now());
            contract.setValidFrom(contractDTO.getValidFrom());
            contract.setValidTo(contractDTO.getValidTo());
            contract.setCancellationPolicy(contractDTO.getCancellationPolicy());
            contract.setPaymentPolicy(contractDTO.getPaymentPolicy());
            contract.setIsActive(true);
            contract = contractRepository.save(contract);

            response = convertToDTO(contract);
            response.setStatusCode(200);
            response.setMessage("Hotel contract added successfully");
            logger.info("Hotel contract added successfully for hotel ID: {}", hotelID);

        }
        catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while adding hotel contract for hotel ID: {}", hotelID, e);
        }

        return response;
    }

    public HotelContractDTO getContractById(Long contractID)
    {
        logger.info("Fetching contract with ID: {}", contractID);
        HotelContractDTO response = new HotelContractDTO();

        try
        {
            Optional<HotelContract> contract = contractRepository.findById(contractID);

            if (contract.isPresent())
            {
                response = convertToDTO(contract.get());
                response.setStatusCode(200);
                response.setMessage("Contract found successfully");
                logger.info("Contract with ID: {} found successfully", contractID);
            }
            else
            {
                response.setStatusCode(404);
                response.setMessage("Contract not found");
                logger.warn("Contract with ID: {} not found", contractID);
            }
        }
        catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while fetching contract with ID: {}", contractID, e);
        }
        return response;
    }

    public HotelContractDTO updateContract(Long contractID, HotelContractDTO contractDTO)
    {
        logger.info("Updating contract with ID: {}", contractID);
        HotelContractDTO response = new HotelContractDTO();

        try
        {
            Optional<HotelContract> contractOptional = contractRepository.findById(contractID);

            if (contractOptional.isPresent())
            {
                HotelContract contract = contractOptional.get();
                contract.setValidFrom(contractDTO.getValidFrom());
                contract.setValidTo(contractDTO.getValidTo());
                contract.setCancellationPolicy(contractDTO.getCancellationPolicy());
                contract.setPaymentPolicy(contractDTO.getPaymentPolicy());
                contract = contractRepository.save(contract);
                response = convertToDTO(contract);
                response.setStatusCode(200);
                response.setMessage("Contract updated successfully");
                logger.info("Contract with ID: {} updated successfully", contractID);
            }
            else
            {
                response.setStatusCode(404);
                response.setMessage("Contract not found");
                logger.warn("Contract with ID: {} not found", contractID);
            }
        }
        catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while updating contract with ID: {}", contractID, e);
        }

        return response;
    }

    public HotelContractDTO deleteContract(Long contractID)
    {
        logger.info("Deleting contract with ID: {}", contractID);
        HotelContractDTO response = new HotelContractDTO();

        try
        {
            contractRepository.deleteById(contractID);
            response.setStatusCode(200);
            response.setMessage("Contract deleted successfully");
            logger.info("Contract with ID: {} deleted successfully", contractID);
        }
        catch (Exception e)
        {
            response.setStatusCode(500);
            response.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while deleting contract with ID: {}", contractID, e);
        }

        return response;
    }

    public List<HotelContractDTO> getContractsByHotelId(Long hotelID)
    {
        logger.info("Fetching contracts for hotel with ID: {}", hotelID);
        List<HotelContractDTO> response;

        try
        {
            List<HotelContract> contracts = contractRepository.findByHotel_HotelID(hotelID);
            response = contracts.stream().map(this::convertToDTO).collect(Collectors.toList());
            logger.info("Fetched {} contracts for hotel with ID: {}", response.size(), hotelID);
        }
        catch (Exception e)
        {
            logger.error("Error occurred while fetching contracts for hotel with ID: {}", hotelID, e);
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

        return response;
    }

    private HotelContractDTO convertToDTO(HotelContract contract)
    {
        HotelContractDTO contractDTO = new HotelContractDTO();

        contractDTO.setId(contract.getId());
        contractDTO.setValidFrom(contract.getValidFrom());
        contractDTO.setValidTo(contract.getValidTo());
        contractDTO.setAddedDate(contract.getAddedDate());
        contractDTO.setCancellationPolicy(contract.getCancellationPolicy());
        contractDTO.setPaymentPolicy(contract.getPaymentPolicy());
        contractDTO.setHotelName(contract.getHotel().getHotelName());
        contractDTO.setIsActive(contract.getIsActive());
        contractDTO.setHotelLocation(contract.getHotel().getHotelCity() + ", " + contract.getHotel().getHotelCountry());

        return contractDTO;
    }

    public List<SearchResultDTO> searchHotelContracts(String location, String checkInDateStr, String checkOutDateStr, int adults, int children)
    {
        logger.info("Searching hotel contracts for location: {}, check-in date: {}, check-out date: {}, adults: {}, children: {}", location, checkInDateStr, checkOutDateStr, adults, children);
        List<SearchResultDTO> searchResults = new ArrayList<>();

        try
        {
            LocalDate checkInDate = LocalDate.parse(checkInDateStr);
            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);
            int totalPersons = adults + children;

            List<HotelContract> contractsPage = contractRepository.findContractsByLocationAndDateRange(location, checkInDate, checkOutDate);

            Map<Long, HotelContract> selectedContracts = new HashMap<>();

            for (HotelContract contract : contractsPage)
            {
                Long hotelId = contract.getHotel().getHotelID();

                if (!selectedContracts.containsKey(hotelId) || contract.getAddedDate().isAfter(selectedContracts.get(hotelId).getAddedDate()))
                {
                    selectedContracts.put(hotelId, contract);
                }
            }

            final long[] counter = {1};

            searchResults = selectedContracts.values().stream().map(contract -> {
                SearchResultDTO dto = new SearchResultDTO();
                Hotel hotel = contract.getHotel();

                dto.setHotelName(hotel.getHotelName());
                dto.setHotelID(hotel.getHotelID());
                dto.setHotelLocation(hotel.getHotelCity() + ", " + hotel.getHotelCountry());
                dto.setHotelDescription(hotel.getHotelDescription());
                dto.setHotelContactNumber(hotel.getHotelContactNumber());
                dto.setHotelEmail(hotel.getHotelEmail());
                dto.setHotelRating(hotel.getHotelRating());
                dto.setHotelImages(hotel.getHotelImages());

                dto.setCancellationPolicy(contract.getCancellationPolicy());
                dto.setPaymentPolicy(contract.getPaymentPolicy());
                dto.setValidFrom(contract.getValidFrom());
                dto.setValidTo(contract.getValidTo());

                dto.setNumber(counter[0]++);

                List<Season> seasons = seasonRepository.findSeasonsByContract(contract);

                Season highestMarkupSeason = null;
                double highestMarkupPercentage = 0.0;
                String highestMarkupName = "";

                for (Season season : seasons)
                {
                    List<Markup> markups = markupRepository.findMarkupsBySeason(season);

                    for (Markup markup : markups)
                    {
                        if (markup.getPercentage() > highestMarkupPercentage)
                        {
                            highestMarkupName = markup.getMarkupName();
                            highestMarkupPercentage = markup.getPercentage();
                            highestMarkupSeason = season;
                        }
                    }
                }

                if (highestMarkupSeason != null)
                {
                    Long seasonId = highestMarkupSeason.getId();

                    SeasonDTO seasonDTO = new SeasonDTO();
                    seasonDTO.setSeasonName(highestMarkupSeason.getSeasonName());
                    seasonDTO.setValidFrom(highestMarkupSeason.getValidFrom());
                    seasonDTO.setValidTo(highestMarkupSeason.getValidTo());
                    dto.setSeasonDTO(seasonDTO);

                    MarkupDTO markupDTO = new MarkupDTO();
                    markupDTO.setMarkupName(highestMarkupName);
                    markupDTO.setPercentage(highestMarkupPercentage);
                    dto.setMarkupDTO(markupDTO);

                    List<RoomTypeDTO> roomTypeDTOs = roomTypeRepository.findRoomTypesBySeason(highestMarkupSeason).stream()
                            .filter(roomType -> roomType.getMaxNumberOfPersons() >= totalPersons).map(roomType ->
                            {
                                RoomTypeDTO roomDTO = new RoomTypeDTO();
                                roomDTO.setRoomTypeID(roomType.getId());
                                roomDTO.setRoomTypeName(roomType.getRoomTypeName());
                                roomDTO.setMaxNumberOfPersons(roomType.getMaxNumberOfPersons());
                                roomDTO.setPrice(roomType.getPrice());
                                roomDTO.setRoomTypeImages(roomType.getRoomTypeImages());

                                int availableRooms = calculateAvailableRooms(roomType.getId(), checkInDate, checkOutDate);
                                roomDTO.setAvailableRooms(availableRooms);

                                return roomDTO;
                            }).collect(Collectors.toList());

                    dto.setRoomTypeDTO(roomTypeDTOs);

                    List<DiscountDTO> discountDTOs = discountRepository.findDiscountsBySeason(highestMarkupSeason).stream()
                            .map(discount ->
                            {
                                DiscountDTO discountDTO = new DiscountDTO();
                                discountDTO.setDiscountName(discount.getDiscountName());
                                discountDTO.setPercentage(discount.getPercentage());

                                return discountDTO;

                            }).collect(Collectors.toList());

                    dto.setDiscountDTO(discountDTOs);

                    List<SupplementDTO> supplementDTOs = supplementRepository.findSupplementsBySeason(highestMarkupSeason).stream()
                            .map(supplement ->
                            {
                                SupplementDTO supplementDTO = new SupplementDTO();
                                supplementDTO.setSupplementID(supplement.getId());
                                supplementDTO.setSupplementName(supplement.getSupplementName());
                                supplementDTO.setPrice(supplement.getPrice());
                                return supplementDTO;
                            }).collect(Collectors.toList());

                    dto.setSupplementDTOS(supplementDTOs);
                }

                dto.setStatusCode(200);
                dto.setMessage("Success");

                return dto;
            }).collect(Collectors.toList());

        }
        catch (DateTimeParseException e)
        {
            SearchResultDTO errorDTO = new SearchResultDTO();

            errorDTO.setStatusCode(400);
            errorDTO.setError("Invalid date format. Please use YYYY-MM-DD.");
            searchResults.add(errorDTO);
        }
        catch (Exception e)
        {
            SearchResultDTO errorDTO = new SearchResultDTO();

            errorDTO.setStatusCode(500);
            errorDTO.setError("An unexpected error occurred while searching for contracts.");
            searchResults.add(errorDTO);
            logger.error("Error occurred while searching for hotel contracts", e);
        }
        return searchResults;
    }

    private int calculateAvailableRooms(Long roomTypeId, LocalDate checkInDate, LocalDate checkOutDate)
    {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new IllegalArgumentException("RoomType not found"));
        int totalRooms = roomType.getNumberOfRooms();

        List<RoomAvailability> roomAvailabilities = roomAvailabilityRepository.findRoomAvailabilitiesByRoomTypeAndDateRange(roomTypeId, checkInDate, checkOutDate);
        int reservedRooms = roomAvailabilities.stream().mapToInt(RoomAvailability::getNumberOfRooms).sum();

        return totalRooms - reservedRooms;
    }

    public Map<String, Float> calculatePayableAmount(CalculationDTO bookingRequest)
    {
        logger.info("Calculating payable amount for booking request");
        Map<String, Float> result = new HashMap<>();

        try
        {
            LocalDate checkInDate = LocalDate.parse(bookingRequest.getCheckIn());
            LocalDate checkOutDate = LocalDate.parse(bookingRequest.getCheckOut());
            long numberOfNights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

            float totalAmount = 0.0f;

            for (RoomTypeDTO roomTypeDTO : bookingRequest.getRoomTypes())
            {
                float roomPrice = roomTypeDTO.getPrice();
                int numberOfRooms = roomTypeDTO.getNumberOfRooms();
                int numberOfPassengers = bookingRequest.getNoOfAdults() + bookingRequest.getNoOfChildren();
                float roomTotal = roomPrice * numberOfRooms * numberOfPassengers * numberOfNights;
                totalAmount += roomTotal;
            }

            for (SupplementDTO supplementDTO : bookingRequest.getSupplements())
            {
                float supplementCost = supplementDTO.getPrice() * supplementDTO.getQuantity();
                totalAmount += supplementCost;
            }

            float discountAmount = 0.0f;

            if (bookingRequest.getDiscountPercentage() != null)
            {
                discountAmount = totalAmount * bookingRequest.getDiscountPercentage() / 100;
                totalAmount -= discountAmount;
            }

            float markupPercentage = bookingRequest.getMarkupPercentage();
            totalAmount *= (1 + markupPercentage / 100);

            result.put("totalAmount", totalAmount);
            result.put("discountAmount", discountAmount);

            logger.info("Payable amount calculated successfully");
        }
        catch (DateTimeParseException e)
        {
            logger.error("Invalid date format. Please use YYYY-MM-DD.", e);
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }
        catch (Exception e)
        {
            logger.error("An error occurred while calculating the payable amount.", e);
            throw new RuntimeException("An error occurred while calculating the payable amount.");
        }

        return result;
    }

    public HotelContractDTO updateContractStatus(Long contractID) {
        logger.info("Updating contract status with ID: {}", contractID);
        HotelContractDTO response = new HotelContractDTO();

        try {
            Optional<HotelContract> contractOptional = contractRepository.findById(contractID);

            if (contractOptional.isPresent()) {
                HotelContract contract = contractOptional.get();

                if (contract.getIsActive()){
                    contract.setIsActive(false);
                }else{
                    contract.setIsActive(true);
                }

//                contract.setIsActive(!contract.getIsActive());
                contract = contractRepository.save(contract);
                response = convertToDTO(contract);
                response.setStatusCode(200);
                response.setMessage("Contract status updated successfully");
                logger.info("Contract status with ID: {} updated successfully", contractID);
            } else {
                response.setStatusCode(404);
                response.setMessage("Contract not found");
                logger.warn("Contract with ID: {} not found", contractID);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred: " + e.getMessage());
            logger.error("Error occurred while updating contract status with ID: {}", contractID, e);
        }

        return response;
    }
}