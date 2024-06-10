package com.tourease.configuration.models.collections;

import com.tourease.configuration.models.enums.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "configuration")
@Setter
@Getter
@NoArgsConstructor
public class Configuration {
    @Id
    private Field name;
    private String value;

    public Configuration(Field name, String value){
        this.name=name;
        this.value=value;
    }
}
