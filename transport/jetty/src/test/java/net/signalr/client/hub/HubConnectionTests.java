/*
 * Copyright 2014 Martin Tamme
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.signalr.client.hub;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.signalr.client.ConnectionListener;
import net.signalr.client.json.gson.GsonFactory;
import net.signalr.client.transport.jetty.WebSocketTransport;
import net.signalr.client.util.concurrent.Compose;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnit4.class)
public final class HubConnectionTests {

    private static final String URL = "https://localhost/signalr/";

    private static final String ACCESS_ID_NAME = "X-AccessId";

    private static final String ACCESS_ID_VALUE = "";

    private static final String HUB_NAME = "hub";

    private static final String JOIN_METHOD_NAME = "join";

    private static final String LEAVE_METHOD_NAME = "leave";

    private static final Logger logger = LoggerFactory.getLogger(HubConnectionTests.class);

    @Test
    public void test() throws InterruptedException, ExecutionException, IOException {
        final HubConnection connection = new HubConnection(URL, new WebSocketTransport(), new GsonFactory());

        connection.addHeader(ACCESS_ID_NAME, ACCESS_ID_VALUE);
        connection.addParameter("culture", "en");
        connection.addListener(new ConnectionListener() {
            @Override
            public void onReconnecting() {
                logger.info("onReconnecting");
            }

            @Override
            public void onReconnected() {
                logger.info("onReconnected");
            }

            @Override
            public void onSending(final String message) {
                logger.info("onSending: {}", message);
            }

            @Override
            public void onReceived(final String message) {
                logger.info("onReceived: {}", message);
            }

            @Override
            public void onError(final Throwable cause) {
                logger.info("onError: {}", cause);
            }

            @Override
            public void onConnectionSlow() {
                logger.info("onConnectionSlow");
            }

            @Override
            public void onConnecting() {
                logger.info("onConnecting");
            }

            @Override
            public void onConnected() {
                logger.info("onConnected");
            }

            @Override
            public void onDisconnecting() {
                logger.info("onDisconnecting");
            }

            @Override
            public void onDisconnected() {
                logger.info("onDisconnected");
            }
        });
        final HubProxy hubProxy = connection.getProxy(HUB_NAME);
        final Promise<Void> start = connection.start().then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                return hubProxy.invoke(JOIN_METHOD_NAME, Void.class, new int[] { 1 }, true);
            }
        });

        Promises.toFuture(start).get();

        System.in.read();

        final Promise<Void> stop = hubProxy.invoke(LEAVE_METHOD_NAME, Void.class, new int[] { 1 }).then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                return connection.stop();
            }
        });

        Promises.toFuture(stop).get();
    }
}
