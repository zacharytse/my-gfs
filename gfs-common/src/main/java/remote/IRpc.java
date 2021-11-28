package remote;

public interface IRpc {
    Response send(String serverName, Request request);
}
