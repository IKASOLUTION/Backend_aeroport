package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Notification;
import aeroport.bf.dto.NotificationDto;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Site and its DTO NotificationDto.
 */

@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDto, Notification> {}
