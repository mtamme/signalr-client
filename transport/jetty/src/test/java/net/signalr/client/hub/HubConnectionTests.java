/*
 * Copyright © Martin Tamme
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
import net.signalr.client.util.concurrent.promise.Compose;
import net.signalr.client.util.concurrent.promise.Promise;
import net.signalr.client.util.concurrent.promise.Promises;

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

    private static final int[] ARGUMENTS = new int[] { 1 };

    private static final Logger LOGGER = LoggerFactory.getLogger(HubConnectionTests.class);

    @Test
    public void test() throws InterruptedException, ExecutionException, IOException {
        final HubConnection connection = new HubConnection(URL, new WebSocketTransport(), new GsonFactory());

        connection.addHeader(ACCESS_ID_NAME, ACCESS_ID_VALUE);
        connection.addParameter("culture", "en");
        connection.addConnectionListener(new ConnectionListener() {
            @Override
            public void onReconnecting() {
                LOGGER.info("onReconnecting");
            }

            @Override
            public void onReconnected() {
                LOGGER.info("onReconnected");
            }

            @Override
            public void onSending(final String message) {
                LOGGER.info("onSending: {}", message);
            }

            @Override
            public void onReceived(final String message) {
                LOGGER.info("onReceived: {}", message);
            }

            @Override
            public void onError(final Throwable cause) {
                LOGGER.info("onError: {}", cause);
            }

            @Override
            public void onConnectionSlow() {
                LOGGER.info("onConnectionSlow");
            }

            @Override
            public void onConnecting() {
                LOGGER.info("onConnecting");
            }

            @Override
            public void onConnected() {
                LOGGER.info("onConnected");
            }

            @Override
            public void onDisconnecting() {
                LOGGER.info("onDisconnecting");
            }

            @Override
            public void onDisconnected() {
                LOGGER.info("onDisconnected");
            }
        });
        final HubProxy proxy = connection.newHubProxy(HUB_NAME);
        final Promise<Void> start = connection.start().then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                return proxy.invoke(JOIN_METHOD_NAME, Void.class, ARGUMENTS, true);
            }
        });

        Promises.await(start);

        System.in.read();

        final Promise<Void> stop = proxy.invoke(LEAVE_METHOD_NAME, Void.class, ARGUMENTS).then(new Compose<Void, Void>() {
            @Override
            protected Promise<Void> doCompose(final Void value) throws Exception {
                return connection.stop();
            }
        });

        Promises.await(stop);
    }
}
