package com.tourease.logger.services;

import com.tourease.logger.models.dto.ChronologyFilter;
import com.tourease.logger.models.dto.MessageLog;
import com.tourease.logger.models.entities.Chronology;
import com.tourease.logger.models.repositories.ChronologyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChronologyService {
    private final ChronologyRepository chronologyRepository;

    public void saveChronology(Chronology chronology){
        chronologyRepository.save(chronology);
    }

    public List<MessageLog> gerChronology(ChronologyFilter filter){
        List<Chronology> chronologyList = chronologyRepository.findChronologyByFilter(filter.email(), filter.createdAfter(), filter.createdBefore());

        return  chronologyList.stream().map(MessageLog::new).toList();
    }
}
