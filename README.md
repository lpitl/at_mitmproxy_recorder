# Mitmproxy Recorder

* [Mitmproxy](https://mitmproxy.org/)
* Java 8

Mitmproxy should be started separately with proxy.py plugin to redirect messages to this server, witch records them to cache

On server port run application with API:
* /last?key - find http/https message by url or part of url
* /clean - clean message cache

Swagger: http://<hostname>:<port>/swagger-ui.html#