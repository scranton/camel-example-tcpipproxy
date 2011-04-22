/*
 * Copyright 2011 FuseSource
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.fusesource.examples.tcpipproxy;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CamelContextXmlTest extends CamelSpringTestSupport {

    // expected message bodies
    protected Object[] expectedBodies = {
            "<something id='1'>expectedBody1</something>",
            "<something id='2'>expectedBody2</something>"};

    // templates to send to input endpoints
    @Produce(uri = "mina:tcp://localhost:5000?sync=true&textline=true")
    protected ProducerTemplate inputEndpoint;

    // mock endpoints used to consume messages from the output endpoints and then perform assertions
    @EndpointInject(uri = "mock:output")
    protected MockEndpoint outputEndpoint;

    @Test
    public void testCamelRoute() throws Exception {
        // lets route from the output endpoints to our mock endpoints so we can assert expectations
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("mina:tcp://localhost:5001?sync=true&textline=true").to(outputEndpoint).to("log:echoServer");
            }
        });

        // define some expectations
        outputEndpoint.expectedBodiesReceivedInAnyOrder(expectedBodies);

        // send some messages to input endpoints
        for (Object expectedBody : expectedBodies) {
            inputEndpoint.sendBody(expectedBody);
        }

        assertMockEndpointsSatisfied();
    }

    @Override
    protected ClassPathXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

}
