package poc.elasticsearch.app.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.convert.TypeDescriptor;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class BeanMerger {

    /**
     * Shallow merge полей POJO by BeanUtils
     * Принцип слияния:
     * base берется как основа для результата, потом в результат копируются все not-null поля из overrides;
     * копирование - неглубокое (т.е. без вложенных доменных сущностей).
     *
     * @param base      - исходные данные, которые берутся за основу для результата
     * @param overrides - источник переопределяющих значений.
     */
    public static void shallowMerge(Object overrides, Object base) {
        log.debug("shallowMerge(): base = {}, overrides = {}", base, overrides);
        String[] excluded = getExcludedPropertyNames(overrides);
        log.debug("shallowMerge(): excluded from merge = {}", String.join(",", excluded));
        BeanUtils.copyProperties(overrides, base, excluded);
        log.debug("shallowMerge(): result = {}", base);
    }

    /**
     * Возвращает перечень имен полей, у которых значение = null
     * или тип - доменная сущность или массив доменных сущностей
     */
    private static String[] getExcludedPropertyNames(Object source) {
        final BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = beanWrapper.getPropertyDescriptors();
        Set<String> excludedNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            TypeDescriptor ptd = beanWrapper.getPropertyTypeDescriptor(pd.getName());
            Object propertyValue = beanWrapper.getPropertyValue(pd.getName());

            TypeDescriptor domainObjectTypeDescriptor = TypeDescriptor.valueOf(Object.class);

            // собираем в список исключаемых полей:
            if ((ptd == null) || // неизвестные поля
                (propertyValue == null) || // поля без значений
                (ptd.isAssignableTo(domainObjectTypeDescriptor)) || // доменные сущности
                ( // массивы доменных сущностей
                    (ptd.isArray() || ptd.isCollection()) &&
                        (ptd.getElementTypeDescriptor() != null) &&
                        ptd.getElementTypeDescriptor().isAssignableTo(domainObjectTypeDescriptor)
                )
            ) {
                excludedNames.add(pd.getName());
            }
        }
        String[] result = new String[excludedNames.size()];
        return excludedNames.toArray(result);
    }

    /**
     * Deep merge by Jackson через JSON
     * Принцип слияния:
     * base берется как основа для результата, потом в результат копируются все not-null поля из overrides;
     * копирование - глубокое (по всем вложенным объектам).
     *
     * @param base      - исходные данные, которые берутся за основу для результата
     * @param overrides - источник переопределяющих значений.
     */
    public static Object deepMerge(Object overrides, Object base) {
        assert overrides != null && base != null;

        log.debug("base: {}", base);
        log.debug("overrides: {}", overrides);

        try {
            // делаем копию, чтобы не испортить base
            ObjectMapper baseMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Object baseClone = baseMapper.readValue(baseMapper.writeValueAsString(base), base.getClass());

            ObjectReader baseReader = baseMapper.readerForUpdating(baseClone);
            ObjectMapper overridesMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JsonNode overridesNode = overridesMapper.valueToTree(overrides);
            Object updated = baseReader.readValue(overridesNode);

            log.debug("updated: {}", updated);

            return updated;
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }
}
