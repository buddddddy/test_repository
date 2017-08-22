package org.mdlp.basestate.data.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    // Глобальный уникальный идентификатор адресного объекта
    @JsonProperty("aoguid")
    private String aoguid;

    // Глобальный уникальный идентификатор дома
    @JsonProperty("houseguid")
    private String houseguid;

    // Квартира
    @JsonProperty("room")
    private String room;

    @JsonProperty("PLACE")
    private String place;
}
