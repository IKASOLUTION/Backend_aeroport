package aeroport.bf.dto.mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import aeroport.bf.domain.User;
import aeroport.bf.dto.UserDto;
import java.util.List;

@Component
public class UserMapper {
    @Autowired
    ProfilMapper profilMapper;
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getUsername())
                .emailAddress(user.getEmail())
                .createUser(user.getCreatedBy())
                .dateCreation(user.getCreatedDate())
                .lastModifyUser(user.getLastModifiedBy())
                .dateLastModification(user.getLastModifiedDate())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .activated(user.isActivated())
                .isDeleted(user.getDeleted())
                // .agence(user.getAgence())
                .profil(user.getProfil())
                .passChange(user.getPassChange())
               // .filiale(user.getFiliale())
                .build();
    }

    public User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getLogin())
                .email(userDto.getEmailAddress())
               // .agence(userDto.getAgence())
                .profil(userDto.getProfil())
                // .filiale(userDto.getFiliale())
                .build();
    }

    public UserDto toTrace(User userDto) {
        return UserDto.builder()
                .login(userDto.getUsername())
                .emailAddress(userDto.getEmail())
              //  .agence(userDto.getAgence())
                .profil(userDto.getProfil())
              //  .filiale(userDto.getFiliale())
                .build();
    }

    public List<UserDto> toDtos(List<User> users) {
        return users.stream().map(this::toDto).toList();
    }

    public List<User> toEntities(List<UserDto> userDtos) {
        return userDtos.stream().map(this::toEntity).toList();
    }
}
