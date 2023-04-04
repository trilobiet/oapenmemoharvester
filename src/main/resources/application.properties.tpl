
spring.jpa.properties.hibernate.jdbc.batch_size=100
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
# logging.level.org.hibernate.type=TRACE

logging.level.root=INFO
logging.level.oapen.memoproject.harvester=INFO
logging.file.name=${user.home}/oapenmemo/logs/oapen_memo-harvester.log

spring.datasource.url=jdbc:mysql://localhost:3306/oapen_memo?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=*************
spring.datasource.password=*************

# beware of trailing spaces!
app.path.harvestedfiles=${user.home}/Downloads/XOAI
app.path.oaipath=https://library.oapen.org/oai/request
app.path.downloads=${user.home}/oapenmemo/downloads
app.path.app-status=${user.home}/oapenmemo/app-state.properties
app.url.exportsurl=https://library.oapen.org/download-export?format=

dbtests.enabled=FALSE

