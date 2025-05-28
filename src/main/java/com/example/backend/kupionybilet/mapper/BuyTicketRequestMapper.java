package com.example.backend.kupionybilet.mapper;

import com.example.backend.kupionybilet.dto.BuyTicketRequestDTO;
import com.example.backend.kupionybilet.dto.NewTicketDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuyTicketRequestMapper {

    NewTicketDTO toNewTicketDTO(BuyTicketRequestDTO dto);
}

