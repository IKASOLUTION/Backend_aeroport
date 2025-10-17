package aeroport.bf.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import aeroport.bf.config.audit.EntityAuditAction;
import aeroport.bf.config.audit.ObjetEntity;
import aeroport.bf.config.security.SecurityUtils;
import aeroport.bf.config.security.TestUserDetailsService;
import aeroport.bf.config.security.jwt.AuthTokenFilter;
import aeroport.bf.config.security.jwt.TokenProvider;
import aeroport.bf.domain.AbstractAuditEntity;
import aeroport.bf.domain.Authority;
import aeroport.bf.domain.EntityAuditEvent;
import aeroport.bf.domain.MenuAction;
import aeroport.bf.domain.Trace;
import aeroport.bf.domain.User;
import aeroport.bf.dto.AccountDto;
import aeroport.bf.dto.JWTToken;
import aeroport.bf.dto.TraceDto;
import aeroport.bf.dto.UserDto;
import aeroport.bf.dto.mapper.TraceMapper;
import aeroport.bf.dto.mapper.UserMapper;
import aeroport.bf.repository.TraceRepository;
import aeroport.bf.repository.UserRepository;
import aeroport.bf.service.util.RandomUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class TraceService {
    @Autowired
    private  TraceMapper mapper;
    @Autowired
    private  TraceRepository traceRepository;
   
    @Autowired
    private   UserRepository userRepository; 

    public List<TraceDto> findTraceByPeriode(LocalDate date1,LocalDate date2) {
        // List<Trace> traces = traceRepository.findByDateSaisieBetweenAndDeletedFalse(date1.plusDays(-1L), date2.plusDays(1L));
        // findDetail(traces.get(0).getId());
        System.out.println("==============date1=="+date1+ "============="+date2);
        System.out.println("==============date1=="+date1.atStartOfDay()+ "============="+date2.atStartOfDay());
        return mapper.toDtos(traceRepository.findByDateSaisieBetweenAndDeletedFalse(date1.atStartOfDay(), date2.atStartOfDay()));
    }

  
    @Async
    public Trace saveTrace( EntityAuditAction action, ObjetEntity objet ) {
        
        Trace trace = new Trace();
        trace.setUser(userRepository.findTop1ByDeletedFalseAndUsername(SecurityUtils.getCurrentUsername()));;
        trace.setObjet(objet.value());
        trace.setAction(action.value());
        trace.setDateSaisie(LocalDateTime.now()); 
        trace.setDeleted(false); 
        traceRepository.save(trace);
      
        return trace;
       
    }

    public TraceDto findDetail(Long id) {
        Optional<Trace> trace = traceRepository.findById(id);
       

        return mapper.toDto(trace.get());
    }
    
    

    @Async
    public void writeAuditEvent( EntityAuditAction action,  ObjetEntity objet) {
       // log.debug("-------------- Post {} audit  --------------", action.value());
        try {
            Trace auditedEntity = saveTrace( action, objet);
            if (auditedEntity != null) {
                traceRepository.save(auditedEntity);
            }
        } catch (Exception e) {
            log.error("Exception while persisting audit entity for {} error: {}", e);
        }
    }


}