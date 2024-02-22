package com.example.community.facility;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.community.UrlMapping.*;

@RestController
@RequestMapping(FACILITY)
public class FacilityController {
    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService){
        this.facilityService=facilityService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<FacilityDTO>> findAll() {
        List<FacilityDTO> facilityDTOS=facilityService.findAll();
        return new ResponseEntity<>(facilityDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(FILTERED_FACILITIES)
    public ResponseEntity<List<FacilityDTO>> findByType(@PathVariable String type) {
        List<FacilityDTO> facilityDTOS=facilityService.findByType(type);
        return new ResponseEntity<>(facilityDTOS, HttpStatus.OK);
    }




    @CrossOrigin
    @PostMapping
    public ResponseEntity<FacilityDTO> create(@Valid @RequestBody FacilityDTO facilityDTO) {
        FacilityDTO facility = facilityService.create(facilityDTO);
        return new ResponseEntity<>(facility, HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping
    public ResponseEntity<FacilityDTO> update(@Valid @RequestBody FacilityDTO facilityDTO) {
        FacilityDTO facility = facilityService.update(facilityDTO);
        return new ResponseEntity<>(facility, HttpStatus.OK);
    }


    @CrossOrigin
    @DeleteMapping(ID)
    public void delete(@PathVariable Long id) {
        facilityService.delete(id);
    }
}
