package aeroport.bf.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import aeroport.bf.dto.AccountDto;
import aeroport.bf.dto.JWTToken;
import aeroport.bf.dto.LoginResponse;
import aeroport.bf.dto.PasswordChangeDTO;
import aeroport.bf.dto.TokenRefreshRequest;
import aeroport.bf.dto.UserDto;
import aeroport.bf.repository.UserRepository;
import aeroport.bf.service.UserService;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tags(@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs"))
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * POST  /users  : Creates a new user.
     *
     * @param userDto
     * @return {@link UserDto}
     */
    @PostMapping("/")
    @Operation(summary = "Creating a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody final UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    /**
     * PUT  /users/:id  : Updates an existing User.
     *
     * @param userDto
     * @param id
     * @return {@link UserDto}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody final UserDto userDto, @PathVariable long id) {
        return ResponseEntity.ok(userService.updateUser(userDto, id));
    }


     /**
     * PUT  /users/:id  : Updates an existing User.
     *
     * @param userDto
     * @param id
     * @return {@link UserDto}
     */
    @PutMapping("/active-desactive-user/{id}")
    @Operation(summary = "Update an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "400", description = "${swagger.http-status.400}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<UserDto> activateDesactiveUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.activateDesactiveUser( id));
    }
    /**
     * GET / : get all users.
     *
     * @return {@link List<UserDto>}
     */
    @GetMapping("/")
    @Operation(summary = "Fetch all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.findAllUser(), HttpStatus.OK);
    }


    @GetMapping("/email")
    @Operation(summary = "Fetch all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "204", description = "${swagger.http-status.204}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<List<UserDto>> findUserByEmailExist() {
        return new ResponseEntity<>(userService.findUserByEmailExist(), HttpStatus.OK);
    }


    /**
     * GET /:id : get user.
     *
     * @param id
     * @return {@link List<UserDto>}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    public ResponseEntity<UserDto> findUser(@PathVariable final long id) {
        return ResponseEntity.ok(userService.findUser(id));
    }

   


    /**
     * Login.
     *
     * @param accountDto
     * @return void
     */
    @PostMapping("/login")
    @Operation(summary = "Login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${swagger.http-status.200}"),
            @ApiResponse(responseCode = "404", description = "${swagger.http-status.404}"),
            @ApiResponse(responseCode = "500", description = "${swagger.http-status.500}")
    })
    private ResponseEntity<LoginResponse> login(@Valid @RequestBody final AccountDto accountDto) {
        
        if (!userRepository.existsByDeletedFalseAndUsername(accountDto.getLogin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found.");
        }

        return userService.login(accountDto);
    }

    @GetMapping("/account")
    @Operation(summary = "Endpoint permettant à un utilisateur de se connecter au système.",
            tags = {"account", "login", "post"},
            responses = {@ApiResponse(responseCode = "200", description = "Si s'il existe un utilisateur connecté"),
                    @ApiResponse(responseCode = "400", description = "En cas d'erreur de validation des accès"),
                    @ApiResponse(responseCode = "404", description = "Au cas ou l'utilisateur n'est pas connecté"),
                    @ApiResponse(responseCode = "500", description = "En cas d'erreur inattendue")})
    private ResponseEntity<UserDto> getCurrentUser() {
        return new ResponseEntity<>(userService.getUserWithRoles(), HttpStatus.OK);
    }


    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
     //  System.out.println("==============ppppppp========");
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }
    
    @PostMapping(path = "/account/reunitialiser")
    public ResponseEntity<Boolean> reunitialise(@RequestBody PasswordChangeDTO passwordChangeDto) {
       
      return new ResponseEntity<>( userService.reunitialise(passwordChangeDto.getEmail(), passwordChangeDto.getNewPassword()), HttpStatus.OK) ;
    }
    

    @PostMapping("/users/refresh")
    @Operation(summary = "Refresh JWT token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed"),
            @ApiResponse(responseCode = "400", description = "Invalid refresh token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<JWTToken> refreshToken(@RequestBody TokenRefreshRequest request) {
        JWTToken newTokens = userService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(newTokens);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
       
      return new ResponseEntity<>( userService.deletUser(id), HttpStatus.OK) ;
    }

      @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        String result = userService.validateVerificationToken(token);
       /*  if (result.equals("valid")) {
            model.addAttribute("message", "Your account has been verified successfully.");
            return "verified";
        } else {
            model.addAttribute("message", "Invalid verification token.");
            return "verify-email";
        }*/
            return result;
    }
    

}
