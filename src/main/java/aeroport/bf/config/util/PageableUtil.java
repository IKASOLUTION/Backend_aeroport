package aeroport.bf.config.util;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import aeroport.bf.dto.SearchDto;


public class PageableUtil {
    
    public static Pageable fromSearchDto(SearchDto search) {
        return fromSearchDto(search, "dateFin", "DESC");
    }
    
    public static Pageable fromSearchDto(SearchDto search, String defaultSortBy, String defaultDirection) {
        int page = search.getPage() != null ? search.getPage() : 0;
        int size = search.getSize() != null ? search.getSize() : 10;
        String sortBy = search.getSortBy() != null ? search.getSortBy() : defaultSortBy;
        String sortDirection = search.getSortDirection() != null ? search.getSortDirection() : defaultDirection;
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return PageRequest.of(page, size, sort);
    }
}
