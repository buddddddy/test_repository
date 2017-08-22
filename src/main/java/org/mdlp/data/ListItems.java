package org.mdlp.data;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 11.08.2017
 * @package org.mdlp.data
 */
@Data
public class ListItems<T> {

    @NotNull
    private final List<T> list;

    private final long total;

}