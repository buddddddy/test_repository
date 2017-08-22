package org.mdlp.data.document;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

public enum Operation {

    /**
     * 200 - Результат фиксации сведений в ИС «Маркировка»
     */
    result(0, 200),

    /**
     * 110 - Регистрация субъектов обращения в ИС «Маркировка»
     */
    subject_register(0, 110),

    /**
     * 311 - Регистрация в ИС «Маркировка» сведений о завершении этапа окончательной упаковки
     */
    register_end_packing(11, 311, 312, 313, 411, 511),

    /**
     * 312 - Регистрация в ИС «Маркировка» сведений об отборе образцов лекарственных препаратов
     */
    register_control_samples(0, 312, 313, 411, 511),

    /**
     * 313 - Регистрация в ИС «Маркировка» сведений о выпуске готовой продукции
     */
    register_product_emission(12, 313, 411, 511),

    register_foreign_product_emission(22, 321),
    receive_importer(28, 341),
    recipe(305, 521),
    // 331 -Регистрация сведений в ИС «Маркировка» о отгрузке лекарственных препаратов в Российскую Федерацию со склада производителя
    foreign_shipment(23, 331),
    // 332 - Регистрация сведений в ИС «Маркировка» о загрузке сведений о ввозе лекарственных препаратов в Российскую Федерацию
    foreign_import(24, 332),
    //335 - Регистрация Импортером в ИС «Маркировка» сведений о результатах таможенного оформления
    fts_data(25, 335),
    // 250 - Регистрация сведений в ИС «Маркировка» об отзыве/отмене ранее совершенной операции
    recall(40, 250),
    // 431 - Регистрация сведений в ИС «Маркировка» о перемещении лекарственных препаратов между различными адресами мест осуществления деятельности
    move_place(33, 431),
    //541 - Регистрация сведений в ИС «Маркировка» о передаче лекарственных препаратов на уничтожение
    move_destruction(307, 541),
    // 542 - Регистрация сведений в ИС «Маркировка» о факте уничтожения лекарственных препаратов
    destruction(308, 542),
    // 551 - Регистрация сведений в ИС «Маркировка» об оформлении таможенной процедуры реэкспорта
    reexport(52, 551),
    // 811 - Регистрация сведений в ИС «Маркировка» о переупаковке и перемаркировке лекарственных препаратов
    relabeling(65, 811),

    /**
     * 411 - Регистрация сведений в ИС «Маркировка» об отгрузке лекарственных препаратов со склада Продавца (при наличии SGTIN)
     */
    move_order_s(31, 411, 413),
    move_owner(17, 381),
    receive_owner(18, 382),
    control_samples(301, 312),
    /**
     * 410 - Регистрация сведений в ИС «Маркировка» об отгрузке лекарственных препаратов (без указания SGTIN)
     */
    move_info(30, 410),

    /**
     * 413 - Регистрация сведений в ИС «Маркировка» о приеме лекарственных препаратов на склад Покупателя (при наличии SGTIN)
     */
    receive_order_s(32, 413, 511),

    /**
     * 414 - Отмена отправителем/ получателем заявки на отгрузку маркированной продукции. Для любого типа отгрузки.
     */
    move_order_cancel(0, 414),

    /**
     * 511 - Подача сведений о розничной продаже маркированной продукции
     */
    retail_sale(51, 511),
    /**
     * 552 - Загрузка сведений о выводе из оборота лекарственных препаратов с учетом различных типов вывода
     */
    withdrawal(52, 552),

    health_care(306, 531);


    private final int coreNumber;

    private final int operationNumber;

    @NotNull
    private final int[] permittedOperationNumbers;

    Operation(int coreNumber, int operationNumber, @NotNull int... permittedOperationNumbers) {
        this.operationNumber = operationNumber;
        this.coreNumber = coreNumber;
        this.permittedOperationNumbers = permittedOperationNumbers;
    }

    public static Operation findByCoreNumber(int coreNumber) {
        for(Operation v : values()){
            if( v.coreNumber == coreNumber){
                return v;
            }
        }
        return null;
    }

    @NotNull
    public List<Operation> getPermittedNextOperations() {
        List<Operation> result = new ArrayList<>();
        for (int permittedOperationNumber : permittedOperationNumbers) {
            for (Operation operation : Operation.values()) {
                if (operation.operationNumber == permittedOperationNumber) {
                    result.add(operation);
                }
            }
        }
        return result;
    }

    @Converter(autoApply = true)
    public static class AttributeConverterImpl implements AttributeConverter<Operation, String> {
        @NotNull
        @Override
        public String convertToDatabaseColumn(@NotNull Operation attribute) {
            return attribute.name();
        }

        @Override
        public Operation convertToEntityAttribute(@Nullable String dbData) {
            return null != dbData ? Operation.valueOf(dbData) : null;
        }
    }

}
