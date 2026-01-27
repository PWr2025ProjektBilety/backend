package com.example.backend.mapper.purchasedTicket;

import com.example.backend.dto.purchasedTicket.PurchasedTicketTimeBasedDTO;
import com.example.backend.dto.purchasedTicket.PurchasedTicketDTO;
import com.example.backend.dto.purchasedTicket.PurchasedTicketSingleBasedDTO;
import com.example.backend.dto.purchasedTicket.PurchasedTicketPeriodicDTO;
import com.example.backend.model.purchasedTicket.PurchasedTicket;
import com.example.backend.model.purchasedTicket.PurchasedTicketTimeBased;
import com.example.backend.model.purchasedTicket.PurchasedTicketSingleRide;
import com.example.backend.model.purchasedTicket.PurchasedTicketPeriodic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface PurchasedTicketMapper {

    @SubclassMapping(source = PurchasedTicketSingleRide.class, target = PurchasedTicketSingleBasedDTO.class)
    @SubclassMapping(source = PurchasedTicketPeriodic.class, target = PurchasedTicketPeriodicDTO.class)
    @SubclassMapping(source = PurchasedTicketTimeBased.class, target = PurchasedTicketTimeBasedDTO.class)
    PurchasedTicketDTO toDto(PurchasedTicket purchasedTicket);


    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "code", target = "qrPayload"),
            @Mapping(source = "purchaseDate", target = "purchaseDate"),
            @Mapping(source = "reduced", target = "reduced"),
            @Mapping(source = "finalPrice", target = "finalPrice"),
    })
    PurchasedTicketSingleBasedDTO toDto(PurchasedTicketSingleRide kupionyBiletJednorazowy);

    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "code", target = "qrPayload"),
            @Mapping(source = "purchaseDate", target = "purchaseDate"),
            @Mapping(source = "reduced", target = "reduced"),
            @Mapping(source = "finalPrice", target = "finalPrice"),
    })
    PurchasedTicketPeriodicDTO toDto(PurchasedTicketPeriodic kupionyBiletOkresowy);

    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "code", target = "qrPayload"),
            @Mapping(source = "purchaseDate", target = "purchaseDate"),
            @Mapping(source = "reduced", target = "reduced"),
            @Mapping(source = "finalPrice", target = "finalPrice"),
    })
    PurchasedTicketTimeBasedDTO toDto(PurchasedTicketTimeBased kupionyBiletCzasowy);

}
