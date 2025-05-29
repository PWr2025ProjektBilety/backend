package com.example.backend.purchasedticket.mapper;

import com.example.backend.purchasedticket.dto.BuyTicketRequestDTO;
import com.example.backend.purchasedticket.dto.NewTicketDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuyTicketRequestMapper {

    NewTicketDTO toNewTicketDTO(BuyTicketRequestDTO dto);
}

