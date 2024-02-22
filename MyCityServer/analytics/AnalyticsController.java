package com.example.community.analytics;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import static com.example.community.UrlMapping.*;

@RestController
@RequestMapping
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService){
        this.analyticsService=analyticsService;
    }

    @CrossOrigin
    @GetMapping(ANALYTICS)
    public ResponseEntity<Analytics> getAdminAnalytics() {
        Analytics analytics=analyticsService.getAdminAnalyticsData();
        return new ResponseEntity<>(analytics, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(INSTITUTION_ANALYTICS)
    public ResponseEntity<InstitutionAnalytics> getInstitutionAnalytics(@PathVariable Long id, @PathVariable String name) {
        InstitutionAnalytics analytics=analyticsService.getInstitutionAnalyticsData(id,name);
        return new ResponseEntity<>(analytics, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(USER_ANALYTICS)
    public ResponseEntity<UserAnalytics> getUserAnalytics(@PathVariable Long id) {
        UserAnalytics analytics=analyticsService.getUserAnalytics(id);
        return new ResponseEntity<>(analytics, HttpStatus.OK);
    }
}
