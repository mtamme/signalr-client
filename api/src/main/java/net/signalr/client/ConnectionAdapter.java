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

package net.signalr.client;

/**
 * Represents a connection adapter.
 */
public class ConnectionAdapter implements ConnectionListener {

    @Override
    public void onConnecting() {
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onReconnecting() {
    }

    @Override
    public void onReconnected() {
    }

    @Override
    public void onDisconnecting() {
    }

    @Override
    public void onDisconnected() {
    }

    @Override
    public void onConnectionSlow() {
    }

    @Override
    public void onError(final Throwable cause) {
    }

    @Override
    public void onSending(final String message) {
    }

    @Override
    public void onReceived(final String message) {
    }
}
