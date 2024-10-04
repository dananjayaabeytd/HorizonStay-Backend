package com.hotel.horizonstay.service;

import com.hotel.horizonstay.dto.HotelContractDTO;
import com.hotel.horizonstay.entity.Hotel;
import com.hotel.horizonstay.entity.HotelContract;
import com.hotel.horizonstay.repository.ContractRepository;
import com.hotel.horizonstay.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public HotelContractDTO addHotelContract(Long hotelID, HotelContractDTO contractDTO) {
        HotelContract contract = new HotelContract();
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelID);
        if (hotelOptional.isPresent()) {
            contract.setHotel(hotelOptional.get());
        } else {
            throw new IllegalArgumentException("Hotel not found");
        }
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
}