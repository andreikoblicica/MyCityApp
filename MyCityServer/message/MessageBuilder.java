package com.example.community.message;



public class MessageBuilder {
    public static MessageDTO toDTO(Message message){
        return new MessageDTO(
                message.getIssue().getId(),
                 message.getSourceName(),
                message.getDestinationNames(),
                message.getMessage(),
                message.getTimestamp()
                );
    }

    public static Message toEntity(MessageDTO messageDTO){
        return new Message(messageDTO.getSourceName(),
                messageDTO.getDestinationNames(),
                messageDTO.getMessage(),
                messageDTO.getTimestamp());
    }
}
