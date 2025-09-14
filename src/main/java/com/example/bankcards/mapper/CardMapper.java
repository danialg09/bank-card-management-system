package com.example.bankcards.mapper;

import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.CardUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {

    @Mapping(source = "owner.username", target = "ownerName")
    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    CardResponse toCardResponse(Card card);

    List<CardResponse> toCardResponses(List<Card> cards);

    @Named("maskCardNumber")
    default String maskCardNumber(String number) {
        return CardUtils.getMaskedCardNumber(number);
    }
}
