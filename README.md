# signalr-client

An extensible asynchronous SignalR client for Java.

## Persistent Connections API

```java
final PersistentConnection connection = new PersistentConnection(
        "http://localhost/signalr",
        new WebSocketTransport(),
        new JacksonFactory());

connection.addListener(new ConnectionAdapter() {
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
final HubProxy proxy = connection.getProxy("hub");

proxy.register("update", Update.class, new HubCallback<Update>() {
    @Override
    public void onInvoke(final Update update) {
        System.out.println(update);
    }
});

Promises.await(connection.start());

proxy.invoke("joinUpdateGroup", Void.class);
```

## Extensibility

# Copyright

Copyright 2014 Martin Tamme. See LICENSE for details.
