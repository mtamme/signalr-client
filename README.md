# signalr-client

An extensible asynchronous SignalR client for Java.

## Persistent Connections API

```java
final PersistentConnection connection = new PersistentConnection(
        "http://localhost/signalr",
        new WebSocketTransport(),
        new JacksonFactory());

connection.addConnectionListener(new ConnectionAdapter() {
    @Override
    public void onReceived(final String message) {
        System.out.println(message);
    }
});

Promises.await(connection.start());

final Scanner scanner = new Scanner(System.in);

while (scanner.hasNextLine()) {
    final String line = scanner.nextLine();

    connection.send(line);
}
```

## Hubs API

```java
final HubConnection connection = new HubConnection(
        "http://localhost/signalr",
        new WebSocketTransport(),
        new GsonFactory());
final HubProxy proxy = connection.getHubProxy("hub");

proxy.register("update", Update.class, new HubCallback<Update>() {
    @Override
    public void onInvoke(final Update update) {
        System.out.println(update);
    }
});
final Promise<Void> start = connection.start().then(new Compose<Void, Void>() {
    @Override
    protected Promise<Void> doCompose(final Void value) throws Exception {
        return proxy.invoke("joinUpdateGroup", Void.class);
    }
});

Promises.await(start);
```

## Extensibility

# Copyright

Copyright © Martin Tamme. See LICENSE for details.
