This is just for various testing purposes.
Images are usually pushed to quay.io/jaeichle/api-endpoint

### Endpoints

* GET `/ping` - returns "PONG" - useful, if you just need something to test connectivity or simulate traffic flow
* GET `/status/<statuscode>` - returns `<statuscode>` - useful, to simulate specific http response statuses
* GET `/sleep/<duration>` - returns after the `<duration>` in seconds. useful to simulate longer running requests
* GET `/headers` - returns all request headers. has been proven useful not only in the context of Istio.
* GET `/pod` - returns the name of the pod , which should be injected via downward API in the deployment. useful for any kind of session-related testing.
