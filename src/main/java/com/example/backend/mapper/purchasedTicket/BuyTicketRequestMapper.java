package com.example.backend.mapper.purchasedTicket;

import com.example.backend.dto.purchasedTicket.BuyTicketRequestDTO;
import com.example.backend.dto.purchasedTicket.NewTicketDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuyTicketRequestMapper {

    NewTicketDTO toNewTicketDTO(BuyTicketRequestDTO dto);
}

