/*
 * Copyright 2017-2019 The OpenTracing Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.opentracing.contrib.jms;


import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.jms.common.SpanJmsDecorator;
import io.opentracing.contrib.jms.common.TracingMessageUtils;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

/**
 * Tracing decorator for JMS MessageProducer
 */
public class TracingMessageProducer implements MessageProducer {

  private final MessageProducer messageProducer;
  private final Tracer tracer;

  public TracingMessageProducer(MessageProducer messageProducer, Tracer tracer) {
    this.messageProducer = messageProducer;
    this.tracer = tracer;
  }

  @Override
  public boolean getDisableMessageID() throws JMSException {
    return messageProducer.getDisableMessageID();
  }

  @Override
  public void setDisableMessageID(boolean value) throws JMSException {
    messageProducer.setDisableMessageID(value);
  }

  @Override
  public boolean getDisableMessageTimestamp() throws JMSException {
    return messageProducer.getDisableMessageTimestamp();
  }

  @Override
  public void setDisableMessageTimestamp(boolean value) throws JMSException {
    messageProducer.setDisableMessageTimestamp(value);
  }

  @Override
  public int getDeliveryMode() throws JMSException {
    return messageProducer.getDeliveryMode();
  }

  @Override
  public void setDeliveryMode(int deliveryMode) throws JMSException {
    messageProducer.setDeliveryMode(deliveryMode);
  }

  @Override
  public int getPriority() throws JMSException {
    return messageProducer.getPriority();
  }

  @Override
  public void setPriority(int defaultPriority) throws JMSException {
    messageProducer.setPriority(defaultPriority);
  }

  @Override
  public long getTimeToLive() throws JMSException {
    return messageProducer.getTimeToLive();
  }

  @Override
  public void setTimeToLive(long timeToLive) throws JMSException {
    messageProducer.setTimeToLive(timeToLive);
  }

  @Override
  public Destination getDestination() throws JMSException {
    return messageProducer.getDestination();
  }

  @Override
  public void close() throws JMSException {
    messageProducer.close();
  }

  @Override
  public void send(Message message) throws JMSException {
    Span span = TracingMessageUtils.buildAndInjectSpan(getDestination(), message, tracer);
    try {
      messageProducer.send(message);
    } catch (Throwable e) {
      SpanJmsDecorator.onError(e, span);
      throw e;
    } finally {
      span.finish();
    }
  }

  @Override
  public void send(Message message, int deliveryMode, int priority, long timeToLive)
      throws JMSException {
    Span span = TracingMessageUtils.buildAndInjectSpan(getDestination(), message, tracer);
    try {
      messageProducer.send(message, deliveryMode, priority, timeToLive);
    } catch (Throwable e) {
      SpanJmsDecorator.onError(e, span);
      throw e;
    } finally {
      span.finish();
    }
  }

  @Override
  public void send(Destination destination, Message message) throws JMSException {
    Span span = TracingMessageUtils.buildAndInjectSpan(destination, message, tracer);
    try {
      messageProducer.send(destination, message);
    } catch (Throwable e) {
      SpanJmsDecorator.onError(e, span);
      throw e;
    } finally {
      span.finish();
    }
  }

  @Override
  public void send(Destination destination, Message message, int deliveryMode, int priority,
      long timeToLive) throws JMSException {
    Span span = TracingMessageUtils.buildAndInjectSpan(destination, message, tracer);
    try {
      messageProducer.send(destination, message, deliveryMode, priority, timeToLive);
    } catch (Throwable e) {
      SpanJmsDecorator.onError(e, span);
      throw e;
    } finally {
      span.finish();
    }
  }
}
