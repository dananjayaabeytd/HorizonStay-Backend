package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.HotelContractDTO;
import com.hotel.horizonstay.dto.SearchResultDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelContractService {
    public void deleteContract(Long contractID) {
    }

    public List<HotelContractDTO> getContractsByHotelId(Long hotelID) {
    return null;
    }

    public HotelContractDTO getContractById(Long contractID) {
    return null;
    }

    public HotelContractDTO addHotelContract(Long hotelID, HotelContractDTO contractDTO) {
    return null;
    }

    public HotelContractDTO updateContract(Long contractID, HotelContractDTO contractDTO) {
    return null;
    }

    public List<SearchResultDTO> searchHotelContracts(String location, String checkInDate, String checkOutDate, int adults, int children) {
    return null;
    }
}
