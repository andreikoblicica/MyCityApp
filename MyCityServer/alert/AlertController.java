package com.example.community.alert;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.firebase.messaging.Notification;

import javax.validation.Valid;
import java.util.List;

import static com.example.community.UrlMapping.*;
import static com.example.community.UrlMapping.ID;

@RestController
@RequestMapping(ALERT)
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService){
        this.alertService=alertService;
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity<List<AlertDTO>> findAll() {
        List<AlertDTO> alertDTOS=alertService.findAll();
        return new ResponseEntity<>(alertDTOS, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping(ID)
    public ResponseEntity<List<AlertDTO>> findByInstitutionId(@PathVariable Long id) {
        List<AlertDTO> alertDTOS=alertService.findByInstitutionId(id);
        return new ResponseEntity<>(alertDTOS, HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping
    public ResponseEntity<AlertDTO> create(@Valid @RequestBody AlertDTO alertDTO) {
        AlertDTO alert = alertService.create(alertDTO);
        sendNotification(alert);
        return new ResponseEntity<>(alert, HttpStatus.OK);
    }


    @CrossOrigin
    @DeleteMapping(ID)
    public void delete(@PathVariable Long id) {
        alertService.delete(id);
    }


    private void sendNotification(AlertDTO alertDTO) {
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(alertDTO.getTitle())
                        .setBody(new Gson().toJson(alertDTO))
                        .build())
                .setTopic("community_app_notification")
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
