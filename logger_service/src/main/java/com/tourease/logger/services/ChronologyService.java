package com.tourease.logger.services;

import com.tourease.logger.models.custom.IndexVM;
import com.tourease.logger.models.enums.Type;
import com.tourease.logger.models.dto.ChronologyFilter;
import com.tourease.logger.models.dto.MessageLog;
import com.tourease.logger.models.collections.Chronology;
import com.tourease.logger.models.repositories.ChronologyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChronologyService {
    private final ChronologyRepository chronologyRepository;

    public void saveChronology(Chronology chronology) {
        chronologyRepository.save(chronology);
    }

    public IndexVM<MessageLog> gerChronology(ChronologyFilter filter) {
        Page<Chronology> page = chronologyRepository.findChronologyByFilter(
                filter.email(),
                filter.createdAfter() == null ? LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC)
                        : filter.createdAfter(),
                filter.createdBefore() == null ? LocalDateTime.now() : filter.createdBefore().plusDays(1),
                filter.type() == null ? "" : filter.type().getLabel(),
                PageRequest.of(filter.page(), filter.pageSize(), Sort.by(Sort.Direction.DESC, "createdOn")));

        return new IndexVM<>(page.map(MessageLog::new));
    }

    public Set<String> getTypes() {
        return Arrays.stream(Type.values()).map(Enum::name).collect(Collectors.toSet());
    }
}
