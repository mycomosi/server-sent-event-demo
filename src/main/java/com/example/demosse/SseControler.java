package com.example.demosse;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SseControler
{
   private Logger logger = LoggerFactory.getLogger(SseControler.class);

   @Value("${server.port}")
   private String port;

   @RequestMapping(path = "/api/sse", method = RequestMethod.GET)
   public SseEmitter getInfiniteMessages() throws IOException
   {
      // SseEmitter emitter = new SseEmitter(15000L); //Override timeout to 15s
      SseEmitter emitter = new SseEmitter();
      logger.info("connection established on port " + port);

      emitter.onCompletion(() -> {
         logger.info("onCompletion");
      });

      emitter.onTimeout(() -> {
         logger.info("onTimeout");
      });

      // Sends a message with a specific "event:" field using the name method
      emitter.send(
         SseEmitter.event()
            .id(new Date().toString())
            .name("myEventType")
            .data(new Message("(" + port + " ) Message of type myEventType sent on : " + new Date().toString()), MediaType.APPLICATION_JSON));
      logger.info("Message of type myEventType sent");

      emitter.send(new Message("(" + port + " ) Message with no type sent on : " + new Date().toString()), MediaType.APPLICATION_JSON);
      logger.info("Message with no type sent");

      return emitter;
   }

   private static class Message
   {

      private final String content;

      public Message(String content)
      {
         this.content = content;
      }

      public String getContent()
      {
         return content;
      }
   }
}
