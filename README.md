# hystrix-demo
Demo-ing Hystrix fault-tolerance features. Sets up Spring boot microservice that publishes metrics
to Prometheus, as well as a Graphene container to view the metrics.

## Getting Started
To get started, run `bash start.sh`.

### hystrix-demo endpoints
* Test circuit-breaker functionality against a rate-limited service:  http://localhost:8080/throttling
* Test timeout functionality against a slow service: http://localhost:8080/slow
* Test against a faulty (50% failing) service: http://localhost:8080/faulty

### Setting up Grafene
1. Go to http://localhost:3000
2. Log in with default username and password `admin`.
3. Click "Add your first data source" -> "Prometheus"
4. Under "HTTP" -> "URL", input `<container ip>:9090`
5. Click "Save & Test" at the bottom
6. To start exploring metrics, click "Explore" on the sidebar on the left

Metrics generated by `@Timed` should show up as "method_timed_seconds_count" while metrics
generated by `@Counted` should show up as `method_counted_total`.
