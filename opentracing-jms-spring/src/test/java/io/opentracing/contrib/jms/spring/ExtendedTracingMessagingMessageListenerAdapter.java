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
package io.opentracing.contrib.jms.spring;

import io.opentracing.Tracer;
import io.opentracing.contrib.jms.common.TracingMessageListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

public class ExtendedTracingMessagingMessageListenerAdapter extends
    TracingMessagingMessageListenerAdapter {

  protected ExtendedTracingMessagingMessageListenerAdapter(Tracer tracer, boolean traceInLog) {
    super(tracer,traceInLog);
  }

  @Override
  public void onMessage(final Message jmsMessage, final Session session) {
    TracingMessageListener listener = new TracingMessageListener(
        new MessageListener() {
          @Override
          public void onMessage(Message message) {
            try {
              jmsMessage.acknowledge();
            } catch (JMSException e) {

            }
          }
        }, tracer,traceInLog);
    listener.onMessage(jmsMessage);
  }

  @Override
  protected TracingMessagingMessageListenerAdapter newInstance() {
    return new ExtendedTracingMessagingMessageListenerAdapter(tracer,traceInLog);
  }
}
