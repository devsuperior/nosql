# Workshop Spring Boot Cassandra DevSuperior

Este é o material de apoio que disponibilizamos para nossos alunos.

### Modelo conceitual

https://docs.google.com/document/d/1oj4e9iaNefOIwKgaLVZ7_mteEXxoVb8wpBs7VcUe4sE/edit?usp=sharing

### Visão geral: column store database

https://database.guide/what-is-a-column-store-database/

### Container Docker do Cassandra para desenvolvimento
```
docker run -d -p 9042:9042 --name cassandra1 cassandra:3.11.10
```
```
docker exec -it cassandra1 bash
```
### cqlsh
```
describe keyspaces

CREATE KEYSPACE testdb WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

use testdb;

describe tables

CREATE TABLE post(id uuid, moment timestamp, body text, author varchar, PRIMARY KEY (id));

INSERT INTO post (id, moment, body, author) VALUES (uuid(), '2021-02-26T10:00:00Z', 'Bom dia!', 'Bob');
INSERT INTO post (id, moment, body, author) VALUES (uuid(), '2021-02-27T10:00:00Z', 'Partiu viagem', 'Maria');
INSERT INTO post (id, moment, body, author) VALUES (uuid(), '2021-02-27T10:00:00Z', 'Que dia bonito!', 'Maria');

SELECT * FROM post;

SELECT * FROM post WHERE author = 'Maria' ALLOW FILTERING;

CREATE CUSTOM INDEX body_idx ON post (body) USING 'org.apache.cassandra.index.sasi.SASIIndex' WITH OPTIONS = {'mode': 'CONTAINS', 'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.NonTokenizingAnalyzer','case_sensitive': 'false'};

SELECT * FROM post WHERE body LIKE '%MORNING%';
```
### Postman collection

https://www.getpostman.com/collections/63e680aa487c491140a3

### Trechos de código

Arquivo properties
```
spring.data.cassandra.contact-points=localhost
spring.data.cassandra.keyspace-name=productsdb
spring.data.cassandra.port=9042

spring.data.cassandra.local-datacenter=datacenter1
```

Classe CassandraConfig

```java
@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

	@Value("${spring.data.cassandra.keyspace-name}")
	private String keyspace;

	@Value("${spring.data.cassandra.contact-points}")
	private String contactPoint;

	@Value("${spring.data.cassandra.port}")
	private int port;
	
	@Value("${spring.data.cassandra.local-datacenter}")
	private String localDatacenter;

	@Override
	public String getContactPoints() {
		return contactPoint;
	}

	@Override
	protected int getPort() {
		return port;
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.CREATE_IF_NOT_EXISTS;
	}

	@Override
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(keyspace).ifNotExists()
				.with(KeyspaceOption.DURABLE_WRITES, true).withSimpleReplication(3L));
	}

	@Override
	protected String getLocalDataCenter() {
		return localDatacenter;
	}

	@Override
	protected String getKeyspaceName() {
		return keyspace;
	}

	@Override
	public String[] getEntityBasePackages() {
		return new String[] { "com.devsuperior.meuprojeto.model.entities" };
	}
}
```
StandardError

```java
public class StandardError implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
}
```
ResourceExceptionHandler
```java
@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError error = new StandardError();
		error.setError("Not found");
		error.setMessage(e.getMessage());
		error.setPath(request.getRequestURI());
		error.setStatus(status.value());
		error.setTimestamp(Instant.now());
	
		return ResponseEntity.status(status).body(error);
	}
}
```
Seed da base de dados
```java
@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@PostConstruct
	public void init() {
		
		departmentRepository.deleteAll();
		productRepository.deleteAll();

		Department d1 = new Department(UUID.randomUUID(), "Livros");
		Department d2 = new Department(UUID.randomUUID(), "Computadores");
		Department d3 = new Department(UUID.randomUUID(), "Jogos");

		departmentRepository.saveAll(Arrays.asList(d1, d2, d3));

		Product p1 = new Product(UUID.randomUUID(), d1.getName(), 180.90, Instant.parse("2021-02-25T10:00:00Z"), "O Senhor dos anéis", "Um conjunto de amigos se unem para combater uma grande ameaça na Terra Média");
		p1.getProps().add(new Prop("Páginas", "1055", PropType.PRODUCT));
		p1.getProps().add(new Prop("Edição", "5", PropType.PRODUCT));

		Product p2 = new Product(UUID.randomUUID(), d1.getName(), 78.00, Instant.parse("2021-02-26T11:00:00Z"), "O Código da Vinci", "Um grande mistério se desenrola em Paris sobre a busca do Santro Graal");
		p2.getProps().add(new Prop("Páginas", "325", PropType.PRODUCT));
		p2.getProps().add(new Prop("Edição", "3", PropType.PRODUCT));	

		Product p3 = new Product(UUID.randomUUID(), d2.getName(), 3490.00, Instant.parse("2021-02-26T12:00:00Z"), "PC Gamer", "Computador especial para quem deseja performance nos seus games");
		p3.getProps().add(new Prop("Memória", "16GB", PropType.PRODUCT));
		p3.getProps().add(new Prop("CPU", "Core i9", PropType.PRODUCT));
		p3.getProps().add(new Prop("Garantia", "1 ano", PropType.CONDITION));

		Product p4 = new Product(UUID.randomUUID(), d2.getName(), 2497.90, Instant.parse("2021-02-26T13:00:00Z"), "Desktop PC", "O equilíbro entre curso e performance para sua comodidade");
		p4.getProps().add(new Prop("Memória", "8GB", PropType.PRODUCT));
		p4.getProps().add(new Prop("CPU", "Core i5", PropType.PRODUCT));
		p4.getProps().add(new Prop("Garantia", "1 ano", PropType.CONDITION));

		Product p5 = new Product(UUID.randomUUID(), d3.getName(), 239.00, Instant.parse("2021-02-27T14:00:00Z"), "The Last of Us 2", "Um mundo pós-apocalíptico enfrente uma grande ameaça novamente");
		p5.getProps().add(new Prop("Ano", "2021", PropType.PRODUCT));

		productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
	}
}
```

## Recursos adicionais

### Introdução NoSQL

[![Image](https://img.youtube.com/vi/c6h5eR0TvfU/mqdefault.jpg "Vídeo no Youtube")](https://youtu.be/c6h5eR0TvfU)

### Documentação Cassandra

https://cassandra.apache.org/doc/latest

### Spring Data Cassandra

https://docs.spring.io/spring-data/cassandra/docs/current/reference/html

### Tutorial no Youtube

https://www.youtube.com/watch?v=s1xc1HVsRk0&list=PLalrWAGybpB-L1PGA-NfFu2uiWHEsdscD

### Projeto exemplo

https://github.com/rahul-ghadge/spring-boot-cassandra-crud

### Localização dos volumes Docker no Windows

https://stackoverflow.com/questions/43181654/locating-data-volumes-in-docker-desktop-windows

### Vídeo sobre tratamento de exceções

[![Image](https://img.youtube.com/vi/MAv7xgnSD-s/mqdefault.jpg "Vídeo no Youtube")](https://youtu.be/MAv7xgnSD-s)
