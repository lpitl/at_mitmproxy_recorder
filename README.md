# Mitmproxy Recorder

* [Mitmproxy](https://mitmproxy.org/)
* Java 8

Mitmproxy should be started separately with proxy.py plugin to redirect messages to this server, witch records them to cache

On server port run application with API:
* /last?key - find http/https message in message cache by url or part of url
* /clean - clean message cache
* /all?key - get all http/https messages from message cache by url or part of url
* /list?key - get string list like "GET http://example.org..." with information about all http/https messages from message cache by url or part of url

Swagger: http://<hostname>:<port><path>/swagger-ui.html#