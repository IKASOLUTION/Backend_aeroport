package aeroport.bf.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonProperty;

import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.config.security.SecurityUtils;
import aeroport.bf.config.security.TestUserDetailsService;
import aeroport.bf.config.security.jwt.AuthTokenFilter;
import aeroport.bf.config.security.jwt.TokenProvider;
import aeroport.bf.domain.Authority;
import aeroport.bf.domain.MenuAction;
import aeroport.bf.domain.Trace;
import aeroport.bf.domain.User;
import aeroport.bf.dto.AccountDto;
import aeroport.bf.dto.JWTToken;
import aeroport.bf.dto.LoginResponse;
import aeroport.bf.dto.UserDto;
import aeroport.bf.dto.mapper.ProfilMapper;
import aeroport.bf.dto.mapper.UserMapper;
import aeroport.bf.repository.ProfilRepository;
import aeroport.bf.repository.TraceRepository;
import aeroport.bf.repository.UserRepository;
import aeroport.bf.service.util.RandomUtil;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
//@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final TestUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TraceRepository traceRepository;
    private final EmailService emailService;
    private final TraceService traceService;
    private final ProfilRepository profilRepository;
    private static final int EXPIRATION = 60 * 24;
   

    
    /**
     * Save user.
     *
     * @param userDto {@link aeroport.bf.dto.UserDto}
     * @return saved user object
     */
    private UserDto saveUser(final UserDto userDto) {
        String[] parts = userDto.getEmailAddress().split("@");
        userDto.setLogin(parts[0]);
        
        User user = mapper.toEntity(userDto);

        if(user.getProfil().getDeleted() == null) {
            
           user.setProfil(profilRepository.findOneByDeletedFalseAndId(user.getProfil().getId())); 
        }
        user.setDeleted(Boolean.FALSE);
        if(userDto.getId() == null) {
            String encryptedPassword = passwordEncoder.encode(userDto.getLogin());
        user.setPassword(encryptedPassword);
        user.setActivated(false);
        user.setPassChange(false);
        user.setActivationKey(RandomUtil.generateActivationKey());
       // user.setAgence(userDto.getAgence());
       // user.setProfil(userDto.getProfil());
        user.setNom(userDto.getNom());
        user.setUsername(userDto.getLogin());
        user.setPrenom(userDto.getPrenom());
        user.setEmail(userDto.getEmailAddress().toLowerCase());
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setExpiryDate(calculateExpiryDate());
       /*  ParametreMail parametreMail = mailRepository.findByCode(200L);
        String confirmationUrl = "http://localhost:8092/api/users/verify-email?token=" + token;
        emailService.sendNewMail(userDto.getEmailAddress(), parametreMail.getObjet(), 
        "Bonjour Mr/Mme "+userDto.getNom()+" "+userDto.getPrenom()+", <br/><br/>\r\nCi-dessous vos param&egrave;tres de connexion &agrave; l\'application ERELEVE :<br/><br/>\r\nIdentifiant = "+user.getUsername()+" <br/>\r\nMot de passe = "+pass+" <br/><br/>\r\nNB : Une modification de votre mot de passe &agrave; la premi&egrave;re connexion est recommand&eacute;e<br/><br/>\r\nCliquez sur le lieu ci-dessous pour acc&eacute;der l&rsquo;application :<br/><br/>\r\n<a href=\""+confirmationUrl+"\">RELEVE</a><br/><br/>\r\n<h2 style=\"color:red;\">Nous vous recommandons l\'utilisation du navigateur Google Chrome</h2><br/><br/>\r\nCordialement.<br/><br/>\r\nCE MAIL EST ISSU D&rsquo;UN AUTOMATE. NE PAS REPONDRE.', 'MAIL POUR ACTIVATION DE COMPTE UTILISATEUR.");
    */    
     } else {

            user = userRepository.findOneByDeletedFalseAndId(userDto.getId());
           //  user.setAgence(userDto.getAgence());
           // user.setProfil(userDto.getProfil());
            user.setNom(userDto.getNom());
            user.setPrenom(userDto.getPrenom());
            user.setEmail(userDto.getEmailAddress().toLowerCase());
            
           
        }

        User savedUser = userRepository.save(user);
        
         if(userDto.getId() !=null) {
            traceService.writeAuditEvent( EntityAuditAction.UPDATE , ObjetEntity.USER);
           } else {
            traceService.writeAuditEvent( EntityAuditAction.CREATE, ObjetEntity.USER);
           }      
        return mapper.toDto(savedUser);
    }


     private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }
    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            return "invalid";
        }
       if(user.getExpiryDate().getTime() < new Date().getTime()) {
            System.out.println("========1====22========"+user.getExpiryDate().getTime());
            return "Lien expiré";
        } 

        user.setActivated(true);

        userRepository.save(user);
        
        return "Compte activé avec succès";
    }


    public List<UserDto> findUserByEmailExist() {
        
        return mapper.toDtos(userRepository.findByEmailIsNotNullAndEmailNot(userRepository.findTop1ByDeletedFalseAndUsername(SecurityUtils.getCurrentUsername()).getEmail()));
    }

    public UserDto activateDesactiveUser(Long id) {
        User user= userRepository.findOneByDeletedFalseAndId(id);
        if(user.isActivated()) {
            user.setActivated(false);
            emailService.sendNewMail(user.getEmail(), "Désactivation du compte", "Bonjour,votre compte a été désactivé. Vous ne pour pouvez plus accéder à la plateforme");
            traceService.writeAuditEvent( EntityAuditAction.DESACTIVATION , ObjetEntity.USER);
        } else {
            user.setActivated(true);
            emailService.sendNewMail(user.getEmail(), "Activation du compte", "Bonjour,votre compte a été activé. Voici vos accès"+"\n"+
                           "  Login: " +user.getUsername()+  "\n"+" Password: "+user.getUsername()+  "\n" );
             
            traceService.writeAuditEvent( EntityAuditAction.ACTIVATION, ObjetEntity.USER);
        }
       return mapper.toDto(userRepository.save(user)) ;

    } 

    /**
     * Create new user.
     *
     * @param userDto {@link aeroport.bf.dto.UserDto}
     * @return created user object
     */
    public UserDto createUser(final UserDto userDto) {
        if (userRepository.existsByDeletedFalseAndEmailOrUsername(userDto.getEmailAddress(), userDto.getLogin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists");
        }

        


        return saveUser(userDto);
    }

    /**
     * Update existing user.
     *
     * @param userDto {@link aeroport.bf.dto.UserDto}
     * @return updated user object
     */
    public UserDto updateUser(final UserDto userDto, final long id) {
        if (!userRepository.existsByDeletedFalseAndId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No user exists with this ID : %d.", id));
        }

        if (Objects.isNull(userDto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Existing user cannot have null ID.");
        }

        userDto.setIsDeleted(false);
        return saveUser(userDto);
    }


    /**
     * Get user by id.
     *
     * @param id searched user id
     * @return found user object
     */
    public UserDto findUser(final long id) {
        if (!userRepository.existsByDeletedFalseAndId(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No user exists with this ID : %d", id));
        }

        return mapper.toDto(userRepository.findOneByDeletedFalseAndId(id));
    }

    /**
     * Fetch all user stored in DB.
     *
     * @return list of {@link aeroport.bf.dto.UserDto}
     */
    public List<UserDto> findAllUser() {
        System.out.println("========userCurent===================="+SecurityUtils.getCurrentUsername());
        // emailService.sendNewMail("dambresibiri@gmail.com", "Désactivation du compte", "Bonjour,votre compte a été désactivé. Vous ne pour pouvez plus accéder à la plateforme");
        List<User> users = userRepository.findAllByDeletedFalse().stream().filter(user->user.getUsername() !=null  &&!user.getUsername().equals(SecurityUtils.getCurrentUsername()) && !user.getUsername().equals("admin"))
        .collect(Collectors.toList());
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No data found, Please create at least one user before.");
        }
        return mapper.toDtos(users);
    }


    /**
     * Login.
     *
     * @param accountDto
     * @return Void
     */
    public ResponseEntity<LoginResponse> login(final AccountDto accountDto) {
        LoginResponse loginResponse = new LoginResponse();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountDto.getLogin(), accountDto.getPassword());

        log.debug("Auth token : {}", authenticationToken.getCredentials());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(accountDto.getLogin());
        Map<String, String> jwt = tokenProvider.generateTokenPair(userDetails,accountDto.getRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AuthTokenFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        boolean result = userRepository.existsByDeletedFalseAndUsername(accountDto.getLogin());
      //  SecurityUtils.getCurrentUsername();
        if (!result) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad credentials, Login or password incorrect.");
        }

        Optional<User> user= userRepository.findByDeletedFalseAndUsername(accountDto.getLogin());
        if(user.isPresent()) {
            if(!user.get().isActivated()) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Activé le compte");
  
            }
        }
       // traceService.writeAuditEvent(mapper.toDto(user.get()), EntityAuditAction.CONNEXION);

       loginResponse.setHeaders(httpHeaders);
       loginResponse.setBody(new JWTToken(jwt));

        
        return new ResponseEntity<>(loginResponse, HttpStatus.CREATED);   
     }


     public JWTToken refreshToken(String oldRefreshToken) {
        if (!tokenProvider.validateToken(oldRefreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
    
        String username = tokenProvider.extractUsername(oldRefreshToken);
    
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    
        Map<String,String> jwt = tokenProvider.generateTokenPair(userDetails, false);
    
    
        return new JWTToken(jwt);
    }
    

    public UserDto getUserWithRoles() {
        String username = SecurityUtils.getCurrentUsername();
        Optional<User> user= userRepository.findByDeletedFalseAndUsername(username);
      //  System.out.println("========================user======"+user);
        UserDto userDto = mapper.toDto(user.get());
        if(userDto.getAuthorities()==null || userDto.getAuthorities().isEmpty()) {
            userDto.setAuthorities(findAuthoritiesByUser( user.get()));
        } else {
            Set<String> list = userDto.getAuthorities();
            list.addAll(findAuthoritiesByUser( user.get()));
            userDto.setAuthorities(list);
        }
      //  Optional<User> user= userRepository.findByDeletedFalseAndUsername(accountDto.getLogin());
       
        return userDto;
    }

    public Set<String> findAuthoritiesByUser(User user) {
        Set<String> authorities = new HashSet<>();
        
        if(user.getProfil().getId() !=null) {
            for(MenuAction menu: user.getProfil().getMenus()) {
                String authority = menu.getMenuActionCode() ;
                authorities.add(authority);
            }
          //  authorities.add(i);
        }
        return authorities;
    }


    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByDeletedFalseAndUsername)
            .ifPresent(user -> {
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                user.setPassChange(true);
                emailService.sendNewMail(user.getEmail(), "Modification de mot de passe", "Bonsoir,Nous partageons avec vous vos nouveaux accès"+"\n"+
                               "  Login: " +user.getUsername()+  "\n"+" Password: "+newPassword+  "\n" );
              traceService.writeAuditEvent( EntityAuditAction.UPDATE, ObjetEntity.USER);
            });
    }
    

    public boolean reunitialise( String email, String newPass) {
        Optional<User> user = userRepository.findOneWithAuthoritiesByEmail(email);
        if(user.isPresent()) {
           // user.get().
           String encryptedPassword = passwordEncoder.encode(newPass);
           user.get().setPassChange(true);
           user.get().setPassword(encryptedPassword);
           userRepository.save(user.get());
           

           emailService.sendNewMail(user.get().getEmail(), "Réunitialisation du compte", "Bonsoir,Nous partageons avec vous vos nouveaux accès"+"\n"+
           "  Login: " +user.get().getUsername()+  "\n"+" Password: "+newPass+  "\n" );

           traceService.writeAuditEvent( EntityAuditAction.REUNITIALISATION , ObjetEntity.USER);
           return true;
        }
        return false;

    }


    public Boolean deletUser(Long id) {
        userRepository.findById(id).ifPresent(user-> {
            user.setDeleted(true);
            traceService.writeAuditEvent( EntityAuditAction.DELETE, ObjetEntity.USER);
        });
        return true;
    }
}