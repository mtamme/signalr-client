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

import org.junit.Test;

import net.signalr.client.ConnectionHandler;
import net.signalr.client.json.gson.GsonFactory;
import net.signalr.client.transport.jetty.WebSocketTransport;
import net.signalr.client.util.concurrent.Promise;
import net.signalr.client.util.concurrent.Promises;

public final class HubConnectionTests {

    private static final String URL = "https://localhost/signalr/";

    private static final String ACCESS_ID_NAME = "X-AccessId";

    private static final String ACCESS_ID_VALUE = "";

    private static final String HUB_NAME = "hub";

    private static final String JOIN_METHOD_NAME = "join";

    private static final String LEAVE_METHOD_NAME = "leave";

    private static void log(final String format, final Object... args) {
        final String line = String.format(format, args);

        System.out.printf("[%s] %s\n", Thread.currentThread().getName(), line);
        System.out.flush();
    }

    @Test
    public void test() throws InterruptedException, ExecutionException, IOException {
        final HubConnection connection = new HubConnection(URL, new WebSocketTransport(), new GsonFactory());

        connection.addHeader(ACCESS_ID_NAME, ACCESS_ID_VALUE);
        connection.addQueryParameter("culture", "en");
        final HubProxy hubProxy = connection.getProxy(HUB_NAME);
        final Promise<Void> start = connection.start(new ConnectionHandler() {
            @Override
            public void onReconnecting() {
                log("onReconnecting");
            }

            @Override
            public void onReconnected() {
                log("onReconnected");
            }

            @Override
            public void onSending(final String message) {
                log("onSending: %s", message);
            }

            @Override
            public void onReceived(final String message) {
                log("onReceived: %s", message);
            }

            @Override
            public void onError(final Throwable cause) {
                log("onError: %s", cause);
            }

            @Override
            public void onConnectionSlow() {
                log("onConnectionSlow");
            }

            @Override
            public void onConnecting() {
                log("onConnecting");
            }

            @Override
            public void onConnected() {
                log("onConnected");
            }

            @Override
            public void onDisconnecting() {
                log("onDisconnecting");
            }

            @Override
            public void onDisconnected() {
                log("onDisconnected");
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