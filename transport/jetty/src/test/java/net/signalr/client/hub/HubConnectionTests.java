/*
 * Copyright © Martin Tamme
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.signalr.client.hub;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import net.signalr.client.ConnectionHandler;
import net.signalr.client.json.gson.GsonFactory;
import net.signalr.client.transport.jetty.WebSocketTransport;
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
        final HubProxy hubProxy = connection.getProxy(HUB_NAME);
        final Promise<Void> start = connection.start(new ConnectionHandler() {
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

        Promises.toFuture(start).get();

        hubProxy.invoke(JOIN_METHOD_NAME, Void.class, new int[] { 1 }, true);

        System.in.read();

        hubProxy.invoke(LEAVE_METHOD_NAME, Void.class, new int[] { 1 });

        final Promise<Void> stop = connection.stop();

        Promises.toFuture(stop).get();
    }
}
