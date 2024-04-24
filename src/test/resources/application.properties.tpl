
spring.jpa.properties.hibernate.jdbc.batch_size=100
spring.jpa.properties.hibernate.jdbc.batch_versioned_data=true
# logging.level.org.hibernate.type=TRACE

logging.level.root=INFO
logging.level.oapen.memoproject.harvester=INFO
logging.file.name=${user.home}/oapenmemo/logs/oapen_memo-harvester.log

spring.datasource.url=jdbc:mysql://localhost:3306/oapen_library?reconnect=true&rewriteBatchedStatements=true
spring.datasource.username=*******
spring.datasource.password=*******

# beware of trailing spaces!
app.path.oaipath=https://library.oapen.org/oai/request
app.path.app-status=${user.home}/oapenmemo/harvester-state.properties
app.path.exportsdir=${user.home}/oapenmemo/downloads
app.url.exports={MARCXML:'https://library.oapen.org/download-export?format=marcxml',ONIX:'https://library.oapen.org/download-export?format=onix',RIS:'https://library.oapen.org/download-export?format=ris',KBART:'https://library.oapen.org/download-export?format=tsv'}

dbwritetests.enabled=FALSE
dbreadtests.enabled=FALSE

