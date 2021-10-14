#!/bin/bash


truncate_id() {
    # Truncates container id to 12 characters
    read docker_id
    echo ${docker_id:0:12}
}


# Build server Docker image
./gradlew build
docker build -t abstractowl/hystrix-demo .

echo
echo

# Run server container
SERVER_ID=$(docker run --rm -d -p 8080:8080 abstractowl/hystrix-demo | truncate_id)
SERVER_IP=$(docker inspect $SERVER_ID | jq -r '.[0].NetworkSettings.IPAddress')
echo "hystrix-demo container ($SERVER_ID) running with ip $SERVER_IP"
echo "-> bound to localhost at: http://127.0.0.1:8080"
echo


# Run Prometheus
mkdir -p prometheus

cat << EOF > prometheus/prometheus.yml
global:
  scrape_interval: 10s

scrape_configs:
  - job_name: 'spring_micrometer'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['$SERVER_IP:8080']
EOF

PROM_ID=$(docker run --rm -d -p 9090:9090 -v $PWD/prometheus:/etc/prometheus prom/prometheus | truncate_id)
PROM_IP=$(docker inspect $PROM_ID | jq -r '.[0].NetworkSettings.IPAddress')
echo "prometheus container ($PROM_ID) running with ip $PROM_IP"
echo "-> bound to localhost at: http://127.0.0.1:9090"
echo


# Run Graphene
GRAFENE_ID=$(docker run --rm -d -p 3000:3000 grafana/grafana | truncate_id)
GRAFENE_IP=$(docker inspect $GRAFENE_ID | jq -r '.[0].NetworkSettings.IPAddress')
echo "grafene container ($GRAFENE_ID) running with ip $GRAFENE_IP"
echo "-> bound to localhost at: http://127.0.0.1:3000"
echo

echo "to kill processes, run:"
echo "docker kill $SERVER_ID $PROM_ID $GRAFENE_ID"
