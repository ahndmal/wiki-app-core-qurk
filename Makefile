dockerRun:
	docker run -it --rm=true \
        --name postgres-quarkus -e POSTGRES_USER=quarkus \
        -e POSTGRES_PASSWORD=quarkus -e POSTGRES_DB=fruits \
        -p 5432:5432 postgres:14.1
javaRun:
	java \
       -Dquarkus.datasource.reactive.url=postgresql://167.235.52.214/welt \
       -Dquarkus.datasource.username=dev \
       -Dquarkus.datasource.password=possum! \
       -jar target/quarkus-app/quarkus-run.jar