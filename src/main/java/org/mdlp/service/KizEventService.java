package org.mdlp.service;

import org.mdlp.web.rest.get_events_list.RestKizEvent;

import java.util.List;

/**
 * Short description text.
 * <p>
 * Long detailed description text for the specific class file.
 *
 * @author SSukhanov
 * @version 07.06.2017
 * @package org.mdlp.service
 */
public interface KizEventService {

    List<RestKizEvent> getKizEvents(KizService.KizEventsFilter filter, boolean async);

    List<RestKizEvent> getKizEvents(String kizId, boolean async);


}
