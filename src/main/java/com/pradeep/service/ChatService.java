package com.pradeep.service;

import com.pradeep.domain.Logs;
import com.pradeep.repository.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatService {

    @Autowired
    private LogsRepository logsRepository;

    /**
     *It returns the message details based on the passed userId
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public List<com.pradeep.dto.Logs> getChatLogsForUser(@PathVariable String userId) {
        List<Logs> logs = logsRepository.findByUserId(userId);
        List<com.pradeep.dto.Logs> logsDto = new ArrayList<>();

        for (Logs log : logs) {
            com.pradeep.dto.Logs logDto = new com.pradeep.dto.Logs();
            logDto.setMessageId(log.getMessageId());
            logDto.setUserId(log.getUserId());
            logDto.setMessage(log.getMessage());
            logDto.setMessageTime(log.getMessageTime());
            logDto.setIsSent(log.getIsSent());

            logsDto.add(logDto);
        }

        return logsDto;
    }

    /**
     * It saves the message details to database
     * @param userId
     * @param logs
     * @return
     */
    @PostMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public String saveLogs(@PathVariable String userId, @RequestBody  com.pradeep.dto.Logs logs) {

        String messageId = userId + "_" + Instant.now();
        Logs logsToSave = new Logs();
        logsToSave.setUserId(userId);
        logsToSave.setMessageTime(Timestamp.from(Instant.now()));
        logsToSave.setMessageId(messageId);
        logsToSave.setIsSent(logs.getIsSent());
        logsToSave.setMessage(logs.getMessage());

        logsRepository.save(logsToSave);

        return messageId;
    }

    /**
     * It deletes the all messages of the user that is passed
     *
     * @param userId
     * @return
     */
    @DeleteMapping("/{userId}")
    public String deleteLogsByUser(@PathVariable String userId) {
        logsRepository.deleteByUserId(userId);
        return "deleted successfully!";
    }

    /**
     * It deletes the message based on messageId passed.
     *
     * @param userId
     * @param messageId
     * @return
     */
    @DeleteMapping("/{userId}/{messageId}")
    public String deleteLogsByMessageId(@PathVariable String userId, @PathVariable String messageId) {
        Logs logs = logsRepository.findById(messageId).orElse(null);
        if (logs == null) {
            return "MessageId not found";
        } else if (!logs.getUserId().equals(userId)) {
            return "MessageId not valid";
        }

        logsRepository.deleteById(messageId);
        return "deleted successfully!";
    }
}
