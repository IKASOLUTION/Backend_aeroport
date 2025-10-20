package aeroport.bf.dto.mapper;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aeroport.bf.domain.Trace;
import aeroport.bf.dto.TraceDto;

/**
 * Mapper for the entity Site and its DTO SiteDTO.
 */
@Component
public class TraceMapper {
     @Autowired
    UserMapper mapper;
    public TraceDto toDto(Trace trace) {
        return TraceDto.builder()
                .id(trace.getId())
                .action(trace.getAction())
                .user(mapper.toDto(trace.getUser()))
                .dateSaisie(trace.getDateSaisie())
                .objet(trace.getObjet())
                .build();
    }

  

    public List<TraceDto> toDtos(List<Trace> traces) {
        return traces.stream().map(this::toDto).toList();
    }

   
}
