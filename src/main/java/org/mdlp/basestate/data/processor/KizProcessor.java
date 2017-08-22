package org.mdlp.basestate.data.processor;

import org.mdlp.basestate.data.Kiz;
import org.mdlp.data.ListItems;
import org.mdlp.service.KizService;
import org.mdlp.wsdl.Documents;
import org.mdlp.wsdl.QueryKizInfo;

import java.io.IOException;

/**
 * Created by SSuvorov on 11.04.2017.
 */
public interface KizProcessor {

    Kiz getPublicControlKiz(String kizId) throws IOException;

    KizService.KizEvent getCurrentKizState(String kizId) throws IOException;

    KizService.KizEvents getKizEvents(String kizId) throws IOException;

    ListItems<KizService.KizInfo> getKizs(KizService.KizInfosFilter filter) throws IOException;

    KizService.KizEvents getKizEvents(KizService.KizEventsFilter filter) throws IOException;

    Documents processContainmentHierarchyRequest(QueryKizInfo queryKizInfo, DrugProcessor drugProcessor) throws IOException;
}
