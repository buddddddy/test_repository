package org.mdlp.basestate.data.processor.impl;

import org.mdlp.basestate.data.Kiz;
import org.mdlp.basestate.data.processor.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Created by SSuvorov on 30.03.2017.
 */
@Component
class RestProcessorImpl implements RestProcessor {
    protected static final Logger LOG = LoggerFactory.getLogger(RestProcessorImpl.class);
    @Autowired
    private DrugProcessor drugProcessor;
    @Autowired
    private ContractorProcessor contractorProcessor;
    @Autowired
    private StatisticProcessor statisticProcessor;
    @Autowired
    private KizProcessor kizProcessor;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
//    @Autowired
//    private ExecutorService executorService;

    @Override
    public Documents process(Documents documents, Documents result) {
        if (documents != null) {
            try {
                if (documents.getRegisterEndPacking() != null) {
                    return drugProcessor.process(documents.getRegisterEndPacking(), result);
                }
                if (documents.getMoveOrder() != null) {
                    return drugProcessor.process(documents.getMoveOrder(), result);
                }
                if (documents.getReceiveOrder() != null) {
                    return drugProcessor.process(documents.getReceiveOrder(), result);
                }
                if (documents.getMoveInfo() != null) {
                    return drugProcessor.process(documents.getMoveInfo(), result);
                }
                if (documents.getRetailSale() != null) {
                    return drugProcessor.process(documents.getRetailSale(), result);
                }
                if (documents.getRegisterProductEmission() != null) {
                    return drugProcessor.process(documents.getRegisterProductEmission(), result);
                }
                if (documents.getRegisterControlSamples() != null) {
                    return drugProcessor.process(documents.getRegisterControlSamples(), result);
                }
                if (documents.getMoveOwner() != null) {
                    return drugProcessor.process(documents.getMoveOwner(), result);
                }
                if (documents.getReceiveOwner() != null) {
                    return drugProcessor.process(documents.getReceiveOwner(), result);
                }
                if (documents.getReceiveImporter() != null) {
                    return drugProcessor.process(documents.getReceiveImporter(), result);
                }
                if (documents.getRecipe() != null) {
                    return drugProcessor.process(documents.getRecipe(), result);
                }
                if (documents.getHealthCare() != null) {
                    return drugProcessor.process(documents.getHealthCare(), result);
                }
                if (documents.getUnitPack() != null) {
                    return drugProcessor.process(documents.getUnitPack(), result);
                }
                if (documents.getUnitExtract() != null) {
                    return drugProcessor.process(documents.getUnitExtract(), result);
                }
                if (documents.getUnitUnpack() != null) {
                    return drugProcessor.process(documents.getUnitUnpack(), result);
                }
                if (documents.getForeignEmission() != null) {
                    return drugProcessor.process(documents.getForeignEmission(), result);
                }
                if (documents.getWithdrawal() != null) {
                    return drugProcessor.process(documents.getWithdrawal(), result);
                }
                if (documents.getForeignShipment() != null) {
                    return drugProcessor.process(documents.getForeignShipment(), result);
                }
                if (documents.getForeignImport() != null) {
                    return drugProcessor.process(documents.getForeignImport(), result);
                }
                if (documents.getFtsData() != null) {
                    return drugProcessor.process(documents.getFtsData(), result);
                }
                if (documents.getMovePlace() != null) {
                    return drugProcessor.process(documents.getMovePlace(), result);
                }
                if (documents.getMoveDestruction() != null) {
                    return drugProcessor.process(documents.getMoveDestruction(), result);
                }
                if (documents.getDestruction() != null) {
                    return drugProcessor.process(documents.getDestruction(), result);
                }
                if (documents.getReexport() != null) {
                    return drugProcessor.process(documents.getReexport(), result);
                }
                if (documents.getRelabeling() != null) {
                    return drugProcessor.process(documents.getRelabeling(), result);
                }
                if (documents.getUnitAppend() != null) {
                    return drugProcessor.process(documents.getUnitAppend(), result);
                }
                if (documents.getQueryKizInfo() != null) {
                    return kizProcessor.processContainmentHierarchyRequest(documents.getQueryKizInfo(), drugProcessor);
                }
                if (documents.getRecall() != null) {
                    return drugProcessor.process(documents.getRecall(), result);
                }
            } catch (Exception e) {
                LOG.error("error processing document", e);
                return null;
            }
        }
        return null;
    }

    @Override
    public Kiz getPublicControlKiz(String kizId, boolean async) {
        return executeCommand(async, new AsyncCommand<Kiz>() {
            @Override
            public Kiz execute() throws IOException {
                return kizProcessor.getPublicControlKiz(kizId);
            }
        });
    }

    @Override
    public KizService.KizEvent getCurrentStateKizEvent(String kizId, boolean async) {
        return executeCommand(async, new AsyncCommand<KizService.KizEvent>() {
            @Override
            public KizService.KizEvent execute() throws IOException {
                return kizProcessor.getCurrentKizState(kizId);
            }
        });
    }

    @Override
    public KizService.KizEvents getKizEvents(String kizId, boolean async) {
        return executeCommand(async, new AsyncCommand<KizService.KizEvents>() {
            @Override
            public KizService.KizEvents execute() throws IOException {
                return kizProcessor.getKizEvents(kizId);
            }
        });
    }

    @Override
    public ListItems<KizService.KizInfo> getKizs(KizService.KizInfosFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<ListItems<KizService.KizInfo>>() {
            @Override
            public ListItems<KizService.KizInfo> execute() throws IOException {
                return kizProcessor.getKizs(filter);
            }
        });
    }

    @Override
    public KizService.KizEvents getKizEvents(KizService.KizEventsFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<KizService.KizEvents>() {
            @Override
            public KizService.KizEvents execute() throws IOException {
                return kizProcessor.getKizEvents(filter);
            }
        });
    }

    @Override
    public ListItems<MembersService.MemberInfo> getContractors(ContractorType type, boolean async) {
        return executeCommand(async, new AsyncCommand<ListItems<MembersService.MemberInfo>>() {
            @Override
            public ListItems<MembersService.MemberInfo> execute() {
                return contractorProcessor.getContractors(type);
            }
        });
    }

    @Override
    public ListItems<MembersService.MemberInfo> getContractors(RegistrationFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<ListItems<MembersService.MemberInfo>>() {
            @Override
            public ListItems<MembersService.MemberInfo> execute() {
                return contractorProcessor.getContractors(filter, async);
            }
        });
    }

    @Override
    public MembersService.MemberInfos getContractorsByRegion(String federalCode, int offset, int pageLimit, boolean async) {
        RegistrationFilter filter = new RegistrationFilter();

        filter.setPaginationOffset(offset);
        filter.setPaginationLimit(pageLimit);
        filter.setFederalCode(federalCode);
        filter.setQueryId(UUID.randomUUID().toString());
        return executeCommand(async, new AsyncCommand<MembersService.MemberInfos>() {
            @Override
            public MembersService.MemberInfos execute() {
                return contractorProcessor.getContractorsByRegion(filter);
            }
        });
    }

    @Override
    public MembersService.MemberInfo getContractor(String id, boolean async) {
        return executeCommand(async, new AsyncCommand<MembersService.MemberInfo>() {
            @Override
            public MembersService.MemberInfo execute() {
                return contractorProcessor.getContractor(id);
            }
        });
    }

    @Override
    public MembersService.MemberInfo getContractorByInn(String inn, boolean async) {
        return executeCommand(async, new AsyncCommand<MembersService.MemberInfo>() {
            @Override
            public MembersService.MemberInfo execute() {
                return contractorProcessor.getContractorByInn(inn);
            }
        });
    }

    @Override
    public DrugsService.DrugInfos getDrugInfos(boolean async) {
        return executeCommand(async, new AsyncCommand<DrugsService.DrugInfos>() {
            @Override
            public DrugsService.DrugInfos execute() {
                return drugProcessor.getDrugInfos();
            }
        });
    }

    @Override
    public DrugsService.DrugInfo getDrugInfo(String id, boolean async) {
        return executeCommand(async, new AsyncCommand<DrugsService.DrugInfo>() {
            @Override
            public DrugsService.DrugInfo execute() {
                return drugProcessor.getDrugInfo(id);
            }
        });
    }

    @Override
    public SummaryStatistic[] getSummaryStatistics(StatisticService.StatisticFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<SummaryStatistic[]>() {
            @Override
            public SummaryStatistic[] execute() {
                return statisticProcessor.getSummaryStatistic(filter);
            }
        });
    }

    @Override
    public DetailedStatistic[] getDetailedStatistics(StatisticService.StatisticFilter filter, boolean async) {
        return executeCommand(async, new AsyncCommand<DetailedStatistic[]>() {
            @Override
            public DetailedStatistic[] execute() {
                return statisticProcessor.getDetailedStatistic(filter);
            }
        });
    }

    @Override
    public Future<DetailedStatistic[]> getAsyncDetailedStatistics(StatisticService.StatisticFilter filter, boolean async) {
        return executeAsyncCommand(async, new AsyncCommand<DetailedStatistic[]>() {
            @Override
            public DetailedStatistic[] execute() {
                return statisticProcessor.getDetailedStatistic(filter);
            }
        });
    }

    @Override
    public Future<SummaryStatistic[]> getAsyncSummaryStatistics(StatisticService.StatisticFilter filter, boolean async) {
        return executeAsyncCommand(async, new AsyncCommand<SummaryStatistic[]>() {
            @Override
            public SummaryStatistic[] execute() {
                return statisticProcessor.getSummaryStatistic(filter);
            }
        });
    }

    public <T> T executeCommand(boolean async, AsyncCommand<T> command) {
        if (async) {
            taskExecutor.execute(() -> {
                try {
                    command.execute();
                } catch (Exception e) {
                    LOG.error("failed to execute async task", e);
                }
            });
            return null;
        } else {
            try {
                return command.execute();
            } catch (Exception e) {
                LOG.error("failed to execute command", e);
            }
            return null;
        }
    }

    public <T> Future executeAsyncCommand(boolean async, AsyncCommand<T> command) {
        return taskExecutor.submit(
                () -> {
                    try {
                        return command.execute();
                    } catch (Exception e) {
                        LOG.error("failed to execute async task", e);
                    }
                    return null;
                });
    }

    private abstract class AsyncCommand<T> {
        public abstract T execute() throws IOException;
    }
}
