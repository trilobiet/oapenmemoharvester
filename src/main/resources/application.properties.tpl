
spring.jpa.properties.hibernate.jdbc.batch_size=100
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true

logging.level.root=INFO
logging.level.oapen.memoproject.harvester=INFO
logging.file.name=${user.home}/oapenmemo/logs/oapen_memo-harvester.log

spring.datasource.url=jdbc:mysql://localhost:3306/oapen_memo?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=*************
spring.datasource.password=*************

dbtests.enabled=FALSE

