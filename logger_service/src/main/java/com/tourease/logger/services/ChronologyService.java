package com.tourease.logger.services;

import com.tourease.logger.models.Type;
import com.tourease.logger.models.dto.ChronologyFilter;
import com.tourease.logger.models.dto.MessageLog;
import com.tourease.logger.models.entities.Chronology;
import com.tourease.logger.models.repositories.ChronologyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChronologyService {
    private final ChronologyRepository chronologyRepository;

    public void saveChronology(Chronology chronology) {
        chronologyRepository.save(chronology);
    }

    public List<MessageLog> gerChronology(ChronologyFilter filter) {
        List<Chronology> chronologyList = chronologyRepository.findChronologyByFilter(filter.email(), filter.createdAfter(), filter.createdBefore());

        return filter.type() != null ?
                chronologyList.stream().filter(chronology -> Objects.equals(chronology.getLog(), filter.type().getLabel())).map(MessageLog::new).toList() :
                chronologyList.stream().map(MessageLog::new).toList();
    }

    public Set<String> getTypes() {
        return Arrays.stream(Type.values()).map(Enum::name).collect(Collectors.toSet());
    }
}
