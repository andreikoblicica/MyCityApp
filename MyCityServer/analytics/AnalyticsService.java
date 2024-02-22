package com.example.community.analytics;

import com.example.community.alert.AlertRepository;
import com.example.community.event.EventRepository;
import com.example.community.facility.FacilityRepository;
import com.example.community.institution.InstitutionRepository;
import com.example.community.institution.InvolvedInstitutionRepository;
import com.example.community.issue.IssueRepository;
import com.example.community.news.NewsRepository;
import com.example.community.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final AlertRepository alertRepository;
    private final EventRepository eventRepository;
    private final FacilityRepository facilityRepository;
    private final InstitutionRepository institutionRepository;
    private final IssueRepository issueRepository;
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;
    private final InvolvedInstitutionRepository involvedInstitutionRepository;

    public AnalyticsService(AlertRepository alertRepository, EventRepository eventRepository, FacilityRepository facilityRepository, InstitutionRepository institutionRepository, IssueRepository issueRepository, NewsRepository newsRepository, UserRepository userRepository, InvolvedInstitutionRepository involvedInstitutionRepository) {
        this.alertRepository = alertRepository;
        this.eventRepository = eventRepository;
        this.facilityRepository = facilityRepository;
        this.institutionRepository = institutionRepository;
        this.issueRepository = issueRepository;
        this.newsRepository = newsRepository;
        this.userRepository = userRepository;
        this.involvedInstitutionRepository=involvedInstitutionRepository;
    }

    public Analytics getAdminAnalyticsData(){
        return new Analytics(
                userRepository.countByUserRole("Institution User"),
                userRepository.countByUserRole("Regular User"),
                institutionRepository.count(),
                issueRepository.count(),
                facilityRepository.count(),
                eventRepository.count(),
                newsRepository.count(),
                alertRepository.count(),
                issueRepository.countByIssueStatus(),
                eventRepository.countByEventStatus(),
                issueRepository.countByIssueType(),
                eventRepository.countByEventType()
        );
    }

    public InstitutionAnalytics getInstitutionAnalyticsData(Long id, String name){
        return new InstitutionAnalytics(
                newsRepository.countNewsByInstitutionId(id),
                alertRepository.countAlertsByInstitutionId(id),
                involvedInstitutionRepository.countByInvolvedInstitutionName(name),
                involvedInstitutionRepository.countByIssueStatus(name),
                issueRepository.countByIssueTypeAndInstitution(name)
        );
    }

    public UserAnalytics getUserAnalytics(Long id) {
        return new UserAnalytics(
                issueRepository.countByUserId(id),
                eventRepository.countByUserId(id)
        );
    }
}
