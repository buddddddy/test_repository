package org.mdlp.web.rest.get_kiz_short_info;

import org.jetbrains.annotations.NotNull;
import org.mdlp.basestate.data.Kiz;
import org.mdlp.basestate.data.processor.RestProcessor;
import org.mdlp.core.mvc.ParentController;
import org.mdlp.service.DrugsService;
import org.mdlp.web.rest.ParentRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ParentController(ParentRestController.class)
@RequestMapping("/get_kiz_short_info")
public class GetKizShortInfoController {


    @NotNull
    private RestProcessor restProcessor;


    @Autowired
    public GetKizShortInfoController(@NotNull RestProcessor restProcessor) {
        this.restProcessor = restProcessor;
    }

    @NotNull
    @RequestMapping("")
    public RestShortKiz action(@NotNull @RequestParam(value = "kizId") String id) {
        RestShortKiz result = new RestShortKiz();

        Kiz kiz = restProcessor.getPublicControlKiz(id, false);
        if (kiz != null) {
            DrugsService.DrugInfo drugInfo = restProcessor.getDrugInfo(kiz.getGtin(), false);
            result.setId(id);
            result.setTitle(drugInfo != null ? drugInfo.getTitle() : "Нет данных");
        }

        return result;
    }

}
