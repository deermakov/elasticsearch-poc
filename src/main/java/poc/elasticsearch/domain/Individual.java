package poc.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL) // сериализуем только not null поля, чтобы в BeanMerger.deepMerge() не затирать not-null поля в БД null'ами из DTO
public class Individual extends Party {
    private String fio;
}
