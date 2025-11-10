package aeroport.bf.dto.mapper;

import aeroport.bf.domain.Aeroport;
import aeroport.bf.domain.Pays;
import aeroport.bf.domain.Ville ;
import aeroport.bf.dto.AeroportDto;
import aeroport.bf.dto.PaysDto;
import aeroport.bf.dto.UserDto;
import aeroport.bf.dto.VilleDto;
import aeroport.bf.repository.VilleRepository;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for the entity Site and its DTO CompagnieDto.
 */

@Component
public class AeroportMapper {
   
    @Autowired
    private VilleRepository villeRepository;
    public AeroportDto toDto(Aeroport aeroport) {
        return AeroportDto.builder()
                .id(aeroport.getId())
                .nomAeroport(aeroport.getNomAeroport())
                .statutAeroport(aeroport.getStatutAeroport())
                .isDeleted(aeroport.getDeleted())
                .pays(aeroport.getPays())
                .ville(aeroport.getVille())  // ✅ Renvoyer l'objet complet
                .villeId(aeroport.getVille() != null ? aeroport.getVille().getId() : null)
                .mailResponsable(aeroport.getMailResponsable())
                .adresse(aeroport.getAdresse())
                .nomResponsable(aeroport.getNomResponsable())
                .prenomResponsable(aeroport.getPrenomResponsable())
                .code_oaci(aeroport.getCode_oaci())
                .siteWeb(aeroport.getSiteWeb())
                .telephone(aeroport.getTelephone())
                .telephoneResponsable(aeroport.getTelephoneResponsable())
                .typeAeroport(aeroport.getTypeAeroport())
                .latitude(aeroport.getLatitude())
                .longitude(aeroport.getLongitude())
                .build();
    }

  public Aeroport toEntity(AeroportDto aeroportDto) {
        Ville ville = null;
        if (aeroportDto.getVilleId() != null) {
            // ✅ Récupérer la ville depuis la base de données
            ville = villeRepository.findById(aeroportDto.getVilleId())
                    .orElseThrow(() -> new RuntimeException("Ville non trouvée avec l'ID: " + aeroportDto.getVilleId()));
        }
        
        return Aeroport.builder()
                .id(aeroportDto.getId())
                .nomAeroport(aeroportDto.getNomAeroport())
                .statutAeroport(aeroportDto.getStatutAeroport())
                .pays(aeroportDto.getPays())
                .ville(ville)  // ✅ Assigner la ville récupérée
                 .mailResponsable(aeroportDto.getMailResponsable())
                .adresse(aeroportDto.getAdresse())
                .nomResponsable(aeroportDto.getNomResponsable())
                .prenomResponsable(aeroportDto.getPrenomResponsable())
                .code_oaci(aeroportDto.getCode_oaci())
                .siteWeb(aeroportDto.getSiteWeb())
                .telephone(aeroportDto.getTelephone())
                .telephoneResponsable(aeroportDto.getTelephoneResponsable())
                .typeAeroport(aeroportDto.getTypeAeroport())
                .latitude(aeroportDto.getLatitude())
                .longitude(aeroportDto.getLongitude())
                .build();
    }

   

    public List<AeroportDto> toDtos(List<Aeroport> aeroports) {
        return aeroports.stream().map(this::toDto).toList();
    }

    public List<Aeroport> toEntities(List<AeroportDto> aeroportDtos) {
        return aeroportDtos.stream().map(this::toEntity).toList();
    }
}
