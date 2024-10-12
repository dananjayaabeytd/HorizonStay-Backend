package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.*;
import com.hotel.horizonstay.entity.*;
import com.hotel.horizonstay.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContractService {

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

    public HotelContractDTO addHotelContract(Long hotelID, HotelContractDTO contractDTO) {
        HotelContract contract = new HotelContract();
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);
        if (hotelOptional.isPresent()) {
            contract.setHotel(hotelOptional.get());
        } else {
            throw new IllegalArgumentException("Hotel not found");
        }
        contract.setAddedDate(LocalDateTime.now());
        contract.setValidFrom(contractDTO.getValidFrom());
        contract.setValidTo(contractDTO.getValidTo());
        contract.setCancellationPolicy(contractDTO.getCancellationPolicy());
        contract.setPaymentPolicy(contractDTO.getPaymentPolicy());
        contract = contractRepository.save(contract);
        return convertToDTO(contract);
    }

    public HotelContractDTO getContractById(Long contractID) {
        Optional<HotelContract> contract = contractRepository.findById(contractID);
        return contract.map(this::convertToDTO).orElseThrow(() -> new IllegalArgumentException("Contract not found"));
    }

    public HotelContractDTO updateContract(Long contractID, HotelContractDTO contractDTO) {
        Optional<HotelContract> contractOptional = contractRepository.findById(contractID);
        if (contractOptional.isPresent()) {
            HotelContract contract = contractOptional.get();
            contract.setValidFrom(contractDTO.getValidFrom());
            contract.setValidTo(contractDTO.getValidTo());
            contract.setCancellationPolicy(contractDTO.getCancellationPolicy());
            contract.setPaymentPolicy(contractDTO.getPaymentPolicy());
            contract = contractRepository.save(contract);
            return convertToDTO(contract);
        } else {
            throw new IllegalArgumentException("Contract not found");
        }
    }

    public HotelContractDTO deleteContract(Long contractID) {
        contractRepository.deleteById(contractID);
        HotelContractDTO response = new HotelContractDTO();
        response.setStatusCode(200);
        response.setMessage("Contract deleted successfully");
        return response;
    }



    public List<HotelContractDTO> getContractsByHotelId(Long hotelID) {
        List<HotelContract> contracts = contractRepository.findByHotel_HotelID(hotelID);
        return contracts.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private HotelContractDTO convertToDTO(HotelContract contract) {
        HotelContractDTO contractDTO = new HotelContractDTO();
        contractDTO.setId(contract.getId());
        contractDTO.setValidFrom(contract.getValidFrom());
        contractDTO.setValidTo(contract.getValidTo());
        contractDTO.setCancellationPolicy(contract.getCancellationPolicy());
        contractDTO.setPaymentPolicy(contract.getPaymentPolicy());
        contractDTO.setHotelName(contract.getHotel().getHotelName());
        contractDTO.setHotelLocation(contract.getHotel().getHotelCity() + ", " + contract.getHotel().getHotelCountry());
        return contractDTO;
    }

    public List<SearchResultDTO> searchHotelContracts(String location, String checkInDateStr, String checkOutDateStr, int adults, int children) {
        List<SearchResultDTO> searchResults = new ArrayList<>();
        try {
            // Parse input dates
            LocalDate checkInDate = LocalDate.parse(checkInDateStr);
            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);
            int totalPersons = adults + children;

            // Retrieve contracts matching location and date range
            List<HotelContract> contracts = contractRepository.findContractsByLocationAndDateRange(location, checkInDate, checkOutDate);

            // Select the latest contract per hotel based on addedDate
            Map<Long, HotelContract> selectedContracts = new HashMap<>();
            for (HotelContract contract : contracts) {
                Long hotelId = contract.getHotel().getHotelID();
                if (!selectedContracts.containsKey(hotelId) || contract.getAddedDate().isAfter(selectedContracts.get(hotelId).getAddedDate())) {
                    selectedContracts.put(hotelId, contract);
                }
            }

            // Initialize a counter for unique numbers
            final long[] counter = {1};

            searchResults = selectedContracts.values().stream().map(contract -> {
                SearchResultDTO dto = new SearchResultDTO();
                Hotel hotel = contract.getHotel();

                // Populate hotel information
                dto.setHotelName(hotel.getHotelName());
                dto.setHotelID(hotel.getHotelID());
                dto.setHotelLocation(hotel.getHotelCity() + ", " + hotel.getHotelCountry());
                dto.setHotelDescription(hotel.getHotelDescription());
                dto.setHotelContactNumber(hotel.getHotelContactNumber());
                dto.setHotelEmail(hotel.getHotelEmail());
                dto.setHotelRating(hotel.getHotelRating());
                dto.setHotelImages(hotel.getHotelImages());

                // Set contract-related policies
                dto.setCancellationPolicy(contract.getCancellationPolicy());
                dto.setPaymentPolicy(contract.getPaymentPolicy());
                dto.setValidFrom(contract.getValidFrom());
                dto.setValidTo(contract.getValidTo());

                // Assign a unique number to the result
                dto.setNumber(counter[0]++);

                // Retrieve all seasons associated with the contract
                List<Season> seasons = seasonRepository.findSeasonsByContract(contract);

                // Find the season with the highest markup percentage
                Season highestMarkupSeason = null;
                double highestMarkupPercentage = 0.0;
                String highestMarkupName = "";

                for (Season season : seasons) {
                    List<Markup> markups = markupRepository.findMarkupsBySeason(season);
                    for (Markup markup : markups) {
                        if (markup.getPercentage() > highestMarkupPercentage) {
                            highestMarkupName = markup.getMarkupName();
                            highestMarkupPercentage = markup.getPercentage();
                            highestMarkupSeason = season;
                        }
                    }
                }

                if (highestMarkupSeason != null) {
                    // Store the season ID
                    Long seasonId = highestMarkupSeason.getId();

                    // Populate season details
                    SeasonDTO seasonDTO = new SeasonDTO();
                    seasonDTO.setSeasonName(highestMarkupSeason.getSeasonName());
                    seasonDTO.setValidFrom(highestMarkupSeason.getValidFrom());
                    seasonDTO.setValidTo(highestMarkupSeason.getValidTo());
                    dto.setSeasonDTO(seasonDTO);

                    // Populate markup information
                    MarkupDTO markupDTO = new MarkupDTO();
                    markupDTO.setMarkupName(highestMarkupName);
                    markupDTO.setPercentage(highestMarkupPercentage);
                    dto.setMarkupDTO(markupDTO);

                    // Retrieve room types associated with the season
                    List<RoomTypeDTO> roomTypeDTOs = roomTypeRepository.findRoomTypesBySeason(highestMarkupSeason).stream()
                            .map(roomType -> {
                                RoomTypeDTO roomDTO = new RoomTypeDTO();
                                roomDTO.setRoomTypeName(roomType.getRoomTypeName());
                                roomDTO.setMaxNumberOfPersons(roomType.getMaxNumberOfPersons());
                                roomDTO.setPrice(roomType.getPrice());
                                roomDTO.setRoomTypeImages(roomType.getRoomTypeImages());
                                return roomDTO;
                            }).collect(Collectors.toList());
                    dto.setRoomTypeDTO(roomTypeDTOs);

                    // Retrieve discounts associated with the season
                    List<DiscountDTO> discountDTOs = discountRepository.findDiscountsBySeason(highestMarkupSeason).stream()
                            .map(discount -> {
                                DiscountDTO discountDTO = new DiscountDTO();
                                discountDTO.setDiscountName(discount.getDiscountName());
                                discountDTO.setPercentage(discount.getPercentage());
                                return discountDTO;
                            }).collect(Collectors.toList());
                    dto.setDiscountDTO(discountDTOs);

                    // Retrieve supplements associated with the season
                    List<SupplementDTO> supplementDTOs = supplementRepository.findSupplementsBySeason(highestMarkupSeason).stream()
                            .map(supplement -> {
                                SupplementDTO supplementDTO = new SupplementDTO();
                                supplementDTO.setSupplementName(supplement.getSupplementName());
                                supplementDTO.setPrice(supplement.getPrice());
                                return supplementDTO;
                            }).toList();
                    dto.setSupplementDTOS(supplementDTOs);
                }

                // Set success status
                dto.setStatusCode(200);
                dto.setMessage("Success");

                return dto;
            }).collect(Collectors.toList());

        } catch (DateTimeParseException e) {
            SearchResultDTO errorDTO = new SearchResultDTO();
            errorDTO.setStatusCode(400);
            errorDTO.setError("Invalid date format. Please use YYYY-MM-DD.");
            searchResults.add(errorDTO);
        } catch (Exception e) {
            SearchResultDTO errorDTO = new SearchResultDTO();
            errorDTO.setStatusCode(500);
            errorDTO.setError("An unexpected error occurred while searching for contracts.");
            searchResults.add(errorDTO);
        }
        return searchResults;
    }
}