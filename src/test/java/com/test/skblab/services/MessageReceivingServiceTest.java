package com.test.skblab.services;

import com.test.skblab.database.entities.User;
import com.test.skblab.messaging.Message;
import com.test.skblab.messaging.MessageId;
import org.hibernate.HibernateException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Alexander Zubkov
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageReceivingServiceTest {

    @Mock
    private MessageProcessingService messageProcessingService;
    @Mock
    private MessageService<User> messageService;
    @Mock
    private MessagingService messagingService;
    @Mock
    private RandomService randomService;

    @InjectMocks
    private MessageReceivingService<User> messageReceivingService;

    private final Message<User> message;
    private final MessageId messageId;

    public MessageReceivingServiceTest() {
        User user = new User();
        message = new Message<>(user);
        messageId = new MessageId(UUID.randomUUID());
        message.setMessageId(messageId);
    }

    @Before
    public void setUp() {
        when(randomService.twoOfThree()).thenReturn(true);
    }

    @Test
    public void receive() throws TimeoutException {
        messageReceivingService.receive(message);
        verify(messageProcessingService).process(message);
        verify(messageService, never()).addMessage(message);
        assertThat(message.getMessageData().getApproval()).isTrue();
    }

    @Test
    public void receiveException() throws TimeoutException {
        when(messagingService.receive(messageId)).thenThrow(new TimeoutException());
        messageReceivingService.receive(message);
        verify(messagingService).receive(messageId);
        verify(randomService, never()).twoOfThree();
        verify(messageService).addMessage(message);
        assertThat(message.getMessageData().getApproval()).isNull();
    }

    @Test
    public void receiveProcessDatabaseException() throws TimeoutException {
        doThrow(new HibernateException("")).when(messageProcessingService).process(message);
        messageReceivingService.receive(message);
        verify(messageProcessingService).process(message);
        verify(messageService).addMessage(message);
        assertThat(message.getMessageData().getApproval()).isTrue();
    }

    @Test
    public void receiveProcessEmailException() throws TimeoutException {
        doThrow(new TimeoutException()).when(messageProcessingService).process(message);
        messageReceivingService.receive(message);
        verify(messageProcessingService).process(message);
        verify(messageService).addMessage(message);
        assertThat(message.getMessageData().getApproval()).isTrue();
    }

}