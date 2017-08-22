package org.mdlp.basestate.data.processor;

import org.mdlp.basestate.data.Kiz;
import org.mdlp.basestate.data.PagedResult;
import org.mdlp.basestate.data.statistic.DetailedStatistic;
import org.mdlp.basestate.data.statistic.RegistrationFilter;
import org.mdlp.basestate.data.statistic.SummaryStatistic;
import org.mdlp.data.ListItems;
import org.mdlp.service.DrugsService;
import org.mdlp.service.KizService;
import org.mdlp.service.MembersService;
import org.mdlp.service.StatisticService;
import org.mdlp.wsdl.Documents;
import org.mdlp.wsdl.Result;

import java.util.concurrent.Future;

/**
 * Created by SSuvorov on 30.03.2017.
 */
public interface RestProcessor {
    Documents process(Documents document, Documents result);

    Kiz getPublicControlKiz(String kizId, boolean async);

    KizService.KizEvent getCurrentStateKizEvent(String kizId, boolean async);

    KizService.KizEvents getKizEvents(String kizId, boolean async);

    ListItems<KizService.KizInfo> getKizs(KizService.KizInfosFilter filter, boolean async);

    KizService.KizEvents getKizEvents(KizService.KizEventsFilter filter, boolean async);

    ListItems<MembersService.MemberInfo> getContractors(ContractorType type, boolean async);

    ListItems<MembersService.MemberInfo> getContractors(RegistrationFilter filter, boolean async);

    MembersService.MemberInfos getContractorsByRegion(String federalCode, int offset, int pageLimit, boolean async);

    MembersService.MemberInfo getContractor(String id, boolean async);

    MembersService.MemberInfo getContractorByInn(String inn, boolean async);

    DrugsService.DrugInfos getDrugInfos( boolean async);

    DrugsService.DrugInfo getDrugInfo(String id, boolean async);

    SummaryStatistic[] getSummaryStatistics(StatisticService.StatisticFilter filter, boolean async);

    DetailedStatistic[] getDetailedStatistics(StatisticService.StatisticFilter filter, boolean async);

    Future<DetailedStatistic[]> getAsyncDetailedStatistics(StatisticService.StatisticFilter filter, boolean async);

    Future<SummaryStatistic[]> getAsyncSummaryStatistics(StatisticService.StatisticFilter filter, boolean async);


}
