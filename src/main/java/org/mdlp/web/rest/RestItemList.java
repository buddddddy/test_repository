package org.mdlp.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author PVBorisov (Pavel.V.Borisov@firstlinesoftware.com)
 * @version 21.06.2017
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RestItemList <T>{

    @JsonProperty("list")
    private List<T> list;

    @JsonProperty("total")
    private long total;

    @JsonProperty("lazyMode")
    private boolean lazyMode = false;
}
