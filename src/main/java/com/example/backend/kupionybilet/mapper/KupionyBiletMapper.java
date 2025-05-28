package com.example.backend.kupionybilet.mapper;

import com.example.backend.kupionybilet.dto.KupionyBiletCzasowyDTO;
import com.example.backend.kupionybilet.dto.KupionyBiletDTO;
import com.example.backend.kupionybilet.dto.KupionyBiletJednorazowyDTO;
import com.example.backend.kupionybilet.dto.KupionyBiletOkresowyDTO;
import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.kupionybilet.model.KupionyBiletCzasowy;
import com.example.backend.kupionybilet.model.KupionyBiletJednorazowy;
import com.example.backend.kupionybilet.model.KupionyBiletOkresowy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.SubclassMapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface KupionyBiletMapper {

    @SubclassMapping(source = KupionyBiletJednorazowy.class, target = KupionyBiletJednorazowyDTO.class)
    @SubclassMapping(source = KupionyBiletOkresowy.class, target = KupionyBiletOkresowyDTO.class)
    @SubclassMapping(source = KupionyBiletCzasowy.class, target = KupionyBiletCzasowyDTO.class)
    KupionyBiletDTO toDto(KupionyBilet kupionyBilet);


    @Mappings({
            @Mapping(source = "kod", target = "code"),
            @Mapping(source = "dataZakupu", target = "purchaseDate"),
            @Mapping(source = "czyUlgowy", target = "reduced"),
            @Mapping(source = "koncowaCena", target = "finalPrice"),
    })
    KupionyBiletJednorazowyDTO toDto(KupionyBiletJednorazowy kupionyBiletJednorazowy);

    @Mappings({
            @Mapping(source = "kod", target = "code"),
            @Mapping(source = "dataZakupu", target = "purchaseDate"),
            @Mapping(source = "czyUlgowy", target = "reduced"),
            @Mapping(source = "koncowaCena", target = "finalPrice"),
    })
    KupionyBiletOkresowyDTO toDto(KupionyBiletOkresowy kupionyBiletOkresowy);

    @Mappings({
            @Mapping(source = "kod", target = "code"),
            @Mapping(source = "dataZakupu", target = "purchaseDate"),
            @Mapping(source = "czyUlgowy", target = "reduced"),
            @Mapping(source = "koncowaCena", target = "finalPrice"),
    })
    KupionyBiletCzasowyDTO toDto(KupionyBiletCzasowy kupionyBiletCzasowy);

}
