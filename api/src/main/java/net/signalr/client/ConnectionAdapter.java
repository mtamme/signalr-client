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
