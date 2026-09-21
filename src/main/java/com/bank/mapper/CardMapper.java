package com.bank.mapper;

import com.bank.dto.card.CardResponse;
import com.bank.entity.Card;
import com.bank.util.CardUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CardMapper {

    @Mapping(source = "owner.username", target = "ownerName")
    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    @Mapping(target = "id", source = "id")
    CardResponse toCardResponse(Card card);

    List<CardResponse> toCardResponses(List<Card> cards);

    @Named("maskCardNumber")
    default String maskCardNumber(String number) {
        return CardUtils.getMaskedCardNumber(number);
    }
}
