
spring.jpa.properties.hibernate.jdbc.batch_size=100
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
# logging.level.org.hibernate.type=TRACE

logging.level.root=INFO
logging.level.oapen.memoproject.harvester=INFO
logging.file.name=${user.home}/oapenmemo/logs/oapen_memo-harvester.log

spring.datasource.url=jdbc:mysql://localhost:3306/oapen_library?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=*************
spring.datasource.password=*************

# harvesting until days before now
app.harvest.daysBack = 7

# pick the right parser 
app.domain=doabooks 
app.path.oaipath=https://library.oapen.org/oai/request
app.path.app-status=${user.home}/oapenmemo/harvester-state.properties

dbwritetests.enabled=FALSE
dbreadtests.enabled=FALSE


