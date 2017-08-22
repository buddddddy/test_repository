package org.mdlp.basestate.data.processor;

import org.mdlp.basestate.data.*;
import org.mdlp.data.ListItems;
import org.mdlp.service.DrugsService;
import org.mdlp.wsdl.*;

import java.util.List;

/**
 * Created by SSuvorov on 11.04.2017.
 */
public interface DrugProcessor {
    Documents process(RegisterEndPacking document, Documents result);

    Documents process(MoveOrder document, Documents result);

    Documents process(ReceiveOrder document, Documents result);

    Documents process(MoveInfo moveInfo, Documents result);

    Documents process(RetailSale document, Documents result);

    Documents process(Withdrawal document, Documents result);

    Documents process(RegisterProductEmission document, Documents result);

    Documents process(RegisterControlSamples document, Documents result);

    Documents process(MoveOwner document, Documents result);

    Documents process(ReceiveOwner document, Documents result);

    Documents process(ReceiveImporter document, Documents result);

    Documents process(Recipe document, Documents result);

    Documents process(HealthCare document, Documents result);

    Documents process(UnitPack document, Documents result);

    Documents process(UnitUnpack document, Documents result);

    Documents process(UnitExtract document, Documents result);

    Documents process(ForeignEmission document, Documents result);

    Documents process(ForeignShipment foreignShipment, Documents result);

    Documents process(ForeignImport foreignImport, Documents result);

    Documents process(FtsData ftsData, Documents result);

    Documents process(MovePlace movePlace, Documents result);

    Documents process(MoveDestruction moveDestruction, Documents result);

    Documents process(Destruction destruction, Documents result);

    Documents process(Reexport reexport, Documents result);

    Documents process(Relabeling relabeling, Documents result);

    Documents process(UnitAppend unitAppend, Documents result);

    Documents process(Recall recall, Documents result);

    DrugsService.DrugInfos getDrugInfos();

    DrugsService.DrugInfo getDrugInfo(String id);

    ListItems<DrugInfoGs1> getDrugInfoGs1List(DrugInfoGs1Filter filter, boolean async);

    DrugInfoGs1 getDrugInfoGs1(String gtin, boolean async);

    ListItems<DrugInfoEsklp> getDrugInfoEsklpList(DrugInfoEsklpFilter filter, boolean async);

    DrugInfoEsklp getDrugInfoEsklp(String regNum, boolean async);
}
