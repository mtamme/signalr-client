# signalr-client

An extensible asynchronous SignalR client for Java.

<span style="color:red">
*This is currently a prototype and therefore not applicable for production use!*
</span>

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
final Promise<Void> promise = connection.start();

Promises.await(promise);
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
final HubProxy proxy = connection.newHubProxy("hub");

proxy.register("update", Update.class, new HubCallback<Update>() {
    @Override
    public void onInvoke(final Update update) {
        System.out.println(update);
    }
});
final Promise<Void> promise = connection.start().then(new Compose<Void, Void>() {
    @Override
    protected Promise<Void> doCompose(final Void value) throws Exception {
        return proxy.invoke("joinUpdateGroup", Void.class);
    }
});

Promises.await(promise);
```

## Extensibility

# Copyright

Copyright © Martin Tamme. See LICENSE for details.
