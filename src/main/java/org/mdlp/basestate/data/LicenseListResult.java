package org.mdlp.basestate.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PVBorisov on 20.06.2017.
 * emailto: Pavel.V.Borisov@firstlinesoftware.com
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LicenseListResult {

    @NotNull
    @JsonProperty("INN")
    private String INN;

    // Адреса мест осуществления деятельности
    @NotNull
    @JsonProperty("LICENSES")
    private List<License> licenses = new ArrayList<>();
}
