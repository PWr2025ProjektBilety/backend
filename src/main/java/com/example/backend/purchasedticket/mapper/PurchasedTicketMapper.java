package com.example.backend.purchasedticket.mapper;

import com.example.backend.purchasedticket.dto.PurchasedTicketTimeBasedDTO;
import com.example.backend.purchasedticket.dto.PurchasedTicketDTO;
import com.example.backend.purchasedticket.dto.PurchasedTicketSingleBasedDTO;
import com.example.backend.purchasedticket.dto.PurchasedTicketPeriodicDTO;
import com.example.backend.purchasedticket.model.PurchasedTicket;
import com.example.backend.purchasedticket.model.PurchasedTicketTimeBased;
import com.example.backend.purchasedticket.model.PurchasedTicketSingleRide;
import com.example.backend.purchasedticket.model.PurchasedTicketPeriodic;
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
            @Mapping(source = "purchaseDate", target = "purchaseDate"),
            @Mapping(source = "reduced", target = "reduced"),
            @Mapping(source = "finalPrice", target = "finalPrice"),
    })
    PurchasedTicketSingleBasedDTO toDto(PurchasedTicketSingleRide kupionyBiletJednorazowy);

    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "purchaseDate", target = "purchaseDate"),
            @Mapping(source = "reduced", target = "reduced"),
            @Mapping(source = "finalPrice", target = "finalPrice"),
    })
    PurchasedTicketPeriodicDTO toDto(PurchasedTicketPeriodic kupionyBiletOkresowy);

    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "purchaseDate", target = "purchaseDate"),
            @Mapping(source = "reduced", target = "reduced"),
            @Mapping(source = "finalPrice", target = "finalPrice"),
    })
    PurchasedTicketTimeBasedDTO toDto(PurchasedTicketTimeBased kupionyBiletCzasowy);

}
