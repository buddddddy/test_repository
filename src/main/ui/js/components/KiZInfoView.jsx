/**
 * Created by PBorisov on 24.03.2017.
 */

import React, { PropTypes, Component }          from 'react';
import { appMenu }                              from '../actions/common_utils.js';
import { AppMainMenu, KiZSubMenu }              from './CommonMainMenu.jsx';
import { CommonInfoBlock, CommonInfoBlockItem } from './CommonInfoBlock.jsx';

export default class KiZInfoView extends Component {
    render () {
        const { kizInfo } = this.props;
        const fullInfo = kizInfo;
        const isEmpty  = !Object.keys(kizInfo).length;
        const ch = (name) => { return !!String((fullInfo[name] == null) ? '' : fullInfo[name]).length; };
        return (
            <div className="mdlp-main-view-container">
                <AppMainMenu menuKey="KIZ_LIST" breadcrumbs={ [{
                    title : !isEmpty ? <span className="mdlp-breadcrumbs-kiz-title found">{ kizInfo.id }</span>
                        : <span className="mdlp-breadcrumbs-kiz-title not_found"></span>
                }] }/>
                {
                    (!isEmpty) && (
                        <div className="mdlp-main-view-workspace">
                            <div className="mdlp-main-view-workspace-leftbar">
                                <KiZSubMenu menuKey="KIZ_INFO" kizId={ kizInfo.id } />
                            </div>
                            <div className="mdlp-main-view-workspace-area">
                                <h2 className="mdlp-inner">{ kizInfo.lpName }</h2>
                                {
                                    (ch('ownerName') || ch('INN') || ch('KPP')) && (
                                        <CommonInfoBlock title="Текущий владелец товара">
                                            <CommonInfoBlockItem title="Наименование" value={fullInfo.ownerName}/>
                                            <CommonInfoBlockItem title="ИНН"          value={fullInfo.INN}/>
                                            <CommonInfoBlockItem title="КПП"          value={fullInfo.KPP}/>
                                        </CommonInfoBlock>
                                    )
                                }
                                {
                                    (ch('SGTIN') || ch('producer') || ch('producerINN') || ch('packerINN') || ch('orderType')
                                    || ch('seriesNumber') || ch('producerKPP') || ch('packerKPP')) && (
                                        <CommonInfoBlock title="Информация по КиЗ">
                                            <CommonInfoBlockItem title="SGTIN"                         value={fullInfo.SGTIN}/>
                                            <CommonInfoBlockItem title="Производитель"                 value={fullInfo.producer}/>
                                            <CommonInfoBlockItem title="ИНН производителя / импортера" value={fullInfo.producerINN}/>
                                            <CommonInfoBlockItem title="ИНН упаковщика"                value={fullInfo.packerINN}/>
                                            <CommonInfoBlockItem title="Тип производственного заказа"  value={fullInfo.orderType}/>
                                            <CommonInfoBlockItem delimiter={true}/>
                                            <CommonInfoBlockItem title="Номер серии"                   value={fullInfo.seriesNumber}/>
                                            <CommonInfoBlockItem title="КПП производителя / импортера" value={fullInfo.producerKPP}/>
                                            <CommonInfoBlockItem title="КПП упаковщика"                value={fullInfo.packerKPP}/>
                                            <CommonInfoBlockItem title="Третичная упаковка"            value={fullInfo.sscc}/>
                                        </CommonInfoBlock>
                                    )
                                }
                                {
                                    (ch('GTIN') || ch('lpName') || ch('expirationDate') || ch('dosageForm') || ch('registrationCertificate')
                                    || ch('tradeName') || ch('dosage') || ch('packageCompleteness')) && (
                                        <CommonInfoBlock title="Информация по лекарственному препарату">
                                            <CommonInfoBlockItem title="GTIN"                          value={fullInfo.GTIN}/>
                                            <CommonInfoBlockItem title="Наименование"                  value={fullInfo.lpName}/>
                                            <CommonInfoBlockItem title="Срок годности"                 value={fullInfo.expirationDate}/>
                                            <CommonInfoBlockItem delimiter={true}/>
                                            <CommonInfoBlockItem title="Лекарственная форма"           value={fullInfo.dosageForm}/>
                                            <CommonInfoBlockItem title="Регистрационное удостоверение" value={fullInfo.registrationCertificate}/>
                                            <CommonInfoBlockItem title="Торговое наименование"         value={fullInfo.tradeName}/>
                                            <CommonInfoBlockItem delimiter={true}/>
                                            <CommonInfoBlockItem title="Дозировка"                     value={fullInfo.dosage}/>
                                            <CommonInfoBlockItem title="Комплектность упаковки"        value={fullInfo.packageCompleteness}/>
                                        </CommonInfoBlock>
                                    )
                                }
                            </div>
                        </div>
                    )
                }
            </div>
        );
    }
}
